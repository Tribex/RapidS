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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
     * @param windowElement The window XML element.
     * @param engine The JavaScript engine to run scripts in.
     */
    public void loadWindow(Element windowElement, ScriptEngine engine) {
        String windowID = (String) engine.call("__widgetTypes.window.loader", windowElement);

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
                            engine.eval("program.getElementById('"+windowID+"').widget.setTitle('"+headElement.getTextContent()+"');", "GUI Process");
                        }
                    }
                }
            } else {
                Utilities.showError("Error: A window should have one head element, and one head element only. \n\n"
                        + " Continuing, program may become unstable.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utilities.showError("Unable to properly initialize a Swing GUI. File may be corrupt or incorrectly formatted. \n\n"+e.fillInStackTrace());
        }

        //Open the window if it is the top-level window.
        if (windowID.equals("__INIT__")) {
            engine.eval("program.getElementById('__INIT__').widget.setVisible(true);", "GUI Process");
        }
        //XXX: END WINDOW CREATION :XXX\\
    }

}
