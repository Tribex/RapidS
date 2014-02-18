/*
    RapidS - Web style development for the desktop.
    Copyright (C) 2014 TribeX

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package us.derfers.tribex.rapids.GUI.Swing;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.ScriptEngine;
import us.derfers.tribex.rapids.Utilities;

/**
 * The Main Swing GUI creator.
 * Delegates the creation of widgets, their layout, creating widget interactivity, and styling of widgets.
 * @author Nateowami, TribeX
 */
public class GUI {

    /**
     * Starts the JFrame, sets a GridBagLayout TODO: allow layouts to be configurable, Sets the Window Title,
     * loads Styles, and runs loadInComposite to load widgets.
     *
     * @param escapedFile The escaped file to load from.
     * @param engine The JavaScript engine to run scripts in.
     * @param parent Optional parent JFrame or Composite. Will be elaborated on soon.
     * @param clearWidgets Wether or not to clear all widgets out of the parent before loading more into it.
     */
    public void loadWindow(Element windowElement, ScriptEngine engine, Boolean setVisible) {
        //XXX: Initialization :XXX\\
        String windowID = "";

        final JFrame window = new JFrame();

        Container windowPanel = window.getContentPane();

        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        if (windowElement.hasAttribute("id") && windowElement.getAttributeNode("id").getTextContent().equals("__INIT__")) {
            setVisible = true;
            windowID = "__INIT__";
            window.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        } else if (windowElement.getAttributeNode("id") == null) {
            Utilities.showError("ERROR: Window does not have an id. Unable to recover.");
            System.exit(0);
        } else {
            windowID = windowElement.getAttributeNode("id").getTextContent();
            window.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    window.setVisible(false);
                }
            });
        }

        //TODO: Flexible layout types
        //Create a gridbaglayout
        windowPanel.setLayout(new GridBagLayout());

        JMenuBar menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);

        engine.call("__widgetOps.storeWidget", windowID, windowElement, window);

        try {
            //XXX: HEAD :XXX\\
            //Get Window information from the Head Element

            //Parse link information in the header
            NodeList headElements = windowElement.getElementsByTagName("head");

            //Make sure we have a valid amount of head elements. (1 allowed, no more, no less).
            if (headElements.getLength() == 1) {
                NodeList headElementList = headElements.item(0).getChildNodes();

                //Loop through the children of the Head element
                for (int counter=0; counter < headElementList.getLength(); counter++) {

                    Node headNode = headElementList.item(counter);

                    //Make sure we have real element nodes
                    if (headNode.getNodeType() == Node.ELEMENT_NODE) {
                        final Element headElement = (Element) headNode;
                        //Set the shell title with the title or window_title node
                        if (headElement.getNodeName().equals("title") || headElement.getNodeName().equals("window_title")) {
                            window.setTitle(headElement.getTextContent());
                        }
                    }
                }
            } else {
                Utilities.showError("Error: A window should have one head element, and one head element only. \n\n"
                        + " Continuing, program may become unstable.");
            }

            //Set the window title to "Untitled Window" if no title has been set yet.
            if (window.getTitle().equals("")) {
                window.setTitle("Untitled Window");
            }

            //XXX: BODY : XXX\\
            //Make sure that there is only one body tag per window.
            NodeList bodyElements = windowElement.getElementsByTagName("body");
            if (bodyElements.getLength() == 1) {
                //Loop through all children of the body element and add them
                loadInComposite((JComponent) windowPanel, bodyElements.item(0), windowID);
            } else {
                Utilities.showError("Error: A window should have one body element, and one body element only. \n\n"
                        + " Program exiting, fatal error.");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utilities.showError("Unable to properly initialize a Swing GUI. File may be corrupt or incorrectly formatted. \n\n"+e.fillInStackTrace());
        }
        //Fit the window to the elements in it.
        window.pack();

        //Loading the JS must be done ABSOLUTELY LAST before the setVisible() call, or some properties will be missed.

        //Open the window
        window.setVisible(setVisible);
        //XXX: END WIDGET CREATION :XXX\\
    }

    /**
     * Loads all XML widgets into the parent composite.
     * @param parentComposite Any widget that can accept children
     * @param node The body or any other composite node.
     * @param engine The JavaScript engine
     */
    public static void loadInComposite(JComponent parentComposite, Node node, String id) {
        //Get the JavaScript object widgetTypes from the ScriptEngine scope.
        Scriptable widgetTypes = (Scriptable) Main.loader.engine.scope.get("__widgetTypes", Main.loader.engine.scope);

        //Get Widgets from the parent Element
        NodeList bodyElementList = node.getChildNodes();
        //Loop through all children of the root element.
        for (int counter=0; counter < bodyElementList.getLength(); counter++) {

            //Isolate the node
            Node widgetNode = bodyElementList.item(counter);

            //Make sure it is a proper element.
            if (widgetNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element widgetElement = (Element) widgetNode;

                //If the tagname is found in widgetTypes
                if (widgetTypes.get(widgetElement.getNodeName(),
                        Main.loader.engine.scope).getClass().getName().equals("org.mozilla.javascript.NativeObject")) {

                    //Make sure the JavaScript widgetType object is really a widget Type
                    if (((NativeObject) widgetTypes.get(widgetElement.getNodeName(), Main.loader.engine.scope)).get("element").toString().equals(widgetElement.getNodeName())) {
                        //Run the JavaScript function to draw and display the widget
                        Main.loader.engine.call("__widgetTypes."+widgetElement.getNodeName()+".loader", parentComposite, widgetElement, id);
                    }

                    //If the widget is not found in widgetTypes
                } else {
                    Utilities.showError("Error: Unknown widget type: "+widgetElement.getNodeName());
                }

            }

        }
        //position and draw the widgets
        parentComposite.doLayout();
    }

}
