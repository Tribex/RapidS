/*
    RapidS - Web style development for the desktop.
    Copyright (C) 2014 TribeX Software Development

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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.mozilla.javascript.NativeObject;

import us.derfers.tribex.rapids.Globals;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.ScriptEngine;
import us.derfers.tribex.rapids.Utilities;

/**
 * Provides Swing Widget Operations: Style loading and event listeners
 * Major TODO: Redesign needed
 * @author TribeX
 *
 */
public class WidgetOps {
    private static ScriptEngine engine = Main.loader.engine;
    //Widget styles
    /**
     * Returns a widget styled with all the styles specified in its element, class.
     * @param widget The original widget
     * @param id The ID of the original widget
     * @return
     * @return The styled widget
     */
    public static void getWidgetStyles(String id) {
        //Get the widget data for the id of the widget.
        NativeObject widgetData = (NativeObject) ((NativeObject) engine.scope.get("__widgetList", engine.scope)).get(id, engine.scope);

        JComponent widget = (JComponent) widgetData.get("widget");

        //TODO: Move to this as soon as it is ready:
        //NativeObject widgetData = engine.scope.get(id, engine.scope);

        //If the element is specified (should be, but just to make sure)
        if (widgetData.get("element").toString() != null && Globals.stylesMap.get(widgetData.get("element").toString()) != null) {
            //Get the styles for the element
            String widgetIdentifier = widgetData.get("element").toString();
            Map<String, String> styles = Globals.stylesMap.get(widgetIdentifier);

            //Load the widget styles.
            widget = loadWidgetStyles((JComponent) widgetData.get("widget"), styles, widgetIdentifier);
        }

        //If the class is specified
        if (widgetData.get("class") != null && Globals.stylesMap.get("."+widgetData.get("class"))!= null) {
            //Get the styles for the class
            String widgetIdentifier = widgetData.get("class").toString();

            Map<String, String> styles = Globals.stylesMap.get("."+widgetIdentifier);

            //Load the widget styles.
            widget = loadWidgetStyles(widget, styles, widgetIdentifier);
        }

        //If the id is specified (It ought to be, but just to be sure)
        if (Globals.stylesMap.get("#"+id)!= null) {
            String widgetIdentifier = "#"+id;
            //Get the styles for the ids
            Map<String, String> styles = Globals.stylesMap.get(widgetIdentifier);
            widget.setName(id);

            //Load the widget styles.
            widget = loadWidgetStyles(widget, styles, widgetIdentifier);
        }
    }


    /**
     * Loads and applies styles for widgets.
     * @param widget The widget to apply styles to.
     * @param styles A Map of styles for that widget
     * @return The widget with all the styles applied.
     */
    public static JComponent loadWidgetStyles(JComponent widget, Map<String, String> styles, String widgetIdentifier) {

        //Create a map of styles from the JavaScript object styles.widgetStyles
        HashMap<String, Object> widgetStyleTypes = Main.loader.engine.getMap("widgetStyles", "__styleList");

        //If there are styles for this identifier
        if (styles != null && !styles.isEmpty()) {

            //Iterate through them
            for (int i = 0; i < styles.size(); i++) {
                //Get the style name
                String style = (String) styles.keySet().toArray()[i];

                //If there is a style by this name
                if (widgetStyleTypes.containsKey(style)) {

                    try {
                        //Attempt to apply it.
                        widget = (JComponent) Main.loader.engine.call("__styleList.widgetStyles."+style+".apply", widget, styles.get(style));
                    } catch (Exception e) {
                        //Show an error if it is invalid
                        Utilities.showError("Invalid CSS: '"+widgetIdentifier+" {"+style+" = "+styles.get(style)+";}'. "
                                + "\n\n Error: "+e.getMessage()+"\n\n Program may not behave as expected.");
                    }

                //If this style does not exist
                } else {
                    //FIXME: Doesn't handle layout style types correctly.
                    //Utilities.showError("CSS style '"+style+"' (from: "+widgetIdentifier+") does not exist! \nProgram may not behave as expected.");
                }
            }
        }
        return widget;
    }


    /**
     * Facilitates adding of event listeners to XML Widgets.
     * @param type The type of event listener to add
     * @param widget The widget to bind the listener to.
     * @param value The function to be evaluated by JavaScript.
     * @param engine The JavaScript engine.
     * @return True on success, False on failure.
     */
    public static boolean addMethodListener(String type, final JComponent widget, final String value, final ScriptEngine engine) {
        //Add event listener
        try {
            if (type.equals("onclick")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent arg0) {
                        try {
                            engine.eval(value);
                        } catch (Exception e1) {
                            Utilities.showError("Bad JavaScript: "+value);
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmouseover")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseEntered(MouseEvent arg0) {
                        try {
                            engine.eval(value);
                        } catch (Exception e1) {
                            Utilities.showError("Bad JavaScript: "+value);
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmouseout")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseExited(MouseEvent arg0) {
                        try {
                            engine.eval(value);
                        } catch (Exception e1) {
                            Utilities.showError("Bad JavaScript: "+value);
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmousedown")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent arg0) {
                        try {
                            engine.eval(value);
                        } catch (Exception e1) {
                            Utilities.showError("Bad JavaScript: "+value);
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmouseup")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseReleased(MouseEvent arg0) {
                        try {
                            engine.eval(value);
                        } catch (Exception e1) {
                            Utilities.showError("Bad JavaScript: "+value);
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onselection")) {
                ((JSpinner) widget).addChangeListener(new ChangeListener(){
                    @Override
                    public void stateChanged(ChangeEvent arg0) {
                        engine.eval(value);

                    }
                });

            }

        } catch (Exception e) {
            Utilities.debugMsg("Error adding a "+type+" event listener to "+widget.getName()+" with the value of '"+value+"'.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
