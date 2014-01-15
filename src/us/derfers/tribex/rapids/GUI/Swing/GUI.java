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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.Globals;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.ScriptEngine;
import us.derfers.tribex.rapids.Utilities;
import us.derfers.tribex.rapids.parsers.CSSParser;

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
        String id = "";

        final JFrame window = new JFrame();

        Container windowPanel = window.getContentPane();

        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        if (windowElement.hasAttribute("id") && windowElement.getAttributeNode("id").getTextContent().equals("init")) {
            setVisible = true;
            id = "__INIT__";
            window.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        } else if (windowElement.getAttributeNode("id") == null) {
            Utilities.showError("ERROR: Window does not have an id. Unable to recover.");
            System.exit(0);
        } else {
            id = windowElement.getAttributeNode("id").getTextContent();
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



        //Create a subMap for holding the Window and any properties of it
        Map<String, Object> windowMap = new HashMap<String, Object>();

        windowMap.put(id, window);

        Main.loader.XMLObjects.put(id, windowMap);


        try {
            //XXX: HEAD :XXX\\
            //Get Window information from the Head Element
            NodeList headElementList = windowElement.getElementsByTagName("head").item(0).getChildNodes();

            //Loop through the children of the Head element
            for (int counter=0; counter < headElementList.getLength(); counter++) {

                Node headNode = headElementList.item(counter);

                //Make sure we have real element nodes
                if (headNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element headElement = (Element) headNode;
                    //Set the shell title with the title or window_title node
                    if (headElement.getNodeName().equals("title") || headElement.getNodeName().equals("window_title")) {
                        window.setTitle(headElement.getTextContent());

                        //Parse link information in the header
                    } else if (headElement.getNodeName().equals("link")) {
                        parseLinks(headElement, engine);
                    }
                }
            }
            //XXX: BODY : XXX\\
            //Loop through all children of the body element and add them

            loadInComposite((JComponent) windowPanel, windowElement.getElementsByTagName("body").item(0), engine);

        } catch (Exception e) {
            e.printStackTrace();
            Utilities.showError("Unable to properly initialize a Swing GUI. File may be corrupt or incorrectly formatted. \n\n"+e.getMessage());
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
    public static void loadInComposite(JComponent parentComposite, Node node, ScriptEngine engine) {

        //Create a map of widgetTypes from the JavaScript object widgets.widgetTypes
        HashMap<String, Object> widgetTypes = engine.getMap("widgetTypes", "widgets");

        //Create and populate the list of registered widgets
        ArrayList<String> registeredWidgets = new ArrayList<String>();
        for (String widgetType : widgetTypes.keySet()) {
            registeredWidgets.add(widgetType);
        }

        //Get Widgets from the parent Element
        NodeList bodyElementList = node.getChildNodes();
        //Loop through all children of the root element.
        for (int counter=0; counter < bodyElementList.getLength(); counter++) {

            Node widgetNode = bodyElementList.item(counter);

            if (widgetNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element widgetElement = (Element) widgetNode;

                //If this element exists in the list of registered widgets
                if (registeredWidgets.contains(widgetElement.getNodeName())) {
                    //Run the JavaScript function to draw and display the widget
                    engine.call("widgets.widgetTypes."+widgetElement.getNodeName()+".loader", parentComposite, widgetElement, engine);
                }
            }

        }

        //position and draw the widgets
        parentComposite.doLayout();

    }

    //Parse <link> tags
    private boolean parseLinks(Element linkElement, ScriptEngine engine) {

        //Check and see if the <link> tag contains a rel and href attribute
        if (linkElement.getAttributeNode("rel") != null && linkElement.getAttribute("href") != null) {
            //If it links to a stylesheet
            if (linkElement.getAttributeNode("rel").getTextContent().equals("stylesheet")) {

                //Check and see if the file exists
                if (this.loadStyles(null, linkElement.getAttributeNode("href").getTextContent()) == false) {
                    Utilities.debugMsg("Error: invalid file in link tag pointing to "+linkElement.getAttributeNode("href").getTextContent());
                    return false;
                };

                //If it links to a script
            } else if (linkElement.getAttributeNode("rel").getTextContent().equals("script")) {

                //Check and see if the file exists
                try {
                    //Run script in file
                    engine.eval(new java.io.FileReader(linkElement.getAttributeNode("href").getTextContent()));
                    return true;

                } catch (FileNotFoundException e) {
                    Utilities.debugMsg("Error: invalid file in link tag pointing to "+linkElement.getAttributeNode("href").getTextContent());
                    e.printStackTrace();
                    return false;
                } catch (DOMException e) {
                    Utilities.debugMsg("Error: Improperly formatted XML");
                    e.printStackTrace();
                    return false;
                } catch (Exception e) {
                    Utilities.debugMsg("Error: invalid script in file "+linkElement.getAttributeNode("href").getTextContent());
                    e.printStackTrace();
                    return false;
                }
            } else {
                //Attempt to load as a .rsm file
                Main.loader.loadAll((Utilities.EscapeScriptTags(linkElement.getAttributeNode("href").getNodeValue())), engine);
            }

        } else {
            Utilities.showError("Warning: <link> tags must contain a href attribute and a rel attribute. Skipping tag.");
        }
        return false;
    }

    //Style loading method
    public boolean loadStyles(String content, String file) {
        //If the user has specified loading from a string, not file
        if (file == null) {
            //Create a new CSSParser
            CSSParser parser = new CSSParser(content);

            //Put all the content of the parsed CSS into the stylesMap
            Globals.stylesMap.putAll(parser.parseAll());
            return true;
            //If the user has specified loading from a file, not string
        } else if (content == null) {
            //Attempt to get the style information from the file
            try {
                //Load the file into the string toParse
                String toParse = FileUtils.readFileToString(new File(file));

                //Create a new CSSParser
                CSSParser parser = new CSSParser(toParse);

                //Put all the content of the parsed CSS into the stylesMap
                Globals.stylesMap.putAll(parser.parseAll());
                return true;
            } catch (IOException e) {
                Utilities.showError("Error: Invalid CSS formatting in file: "+file);
                return false;
            }


        }
        //In case something went wrong that was not caught
        return false;
    }



}
