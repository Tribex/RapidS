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

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    /**
     * Facilitates adding of event listeners to XML Widgets.
     * @param type The type of event listener to add
     * @param widget The widget to bind the listener to.
     * @param value The function to be evaluated by JavaScript.
     * @param engine The JavaScript engine.
     * @return True on success, False on failure.
     */
    public static boolean addMethodListener(final String type, final JComponent widget, final String value) {
        final ScriptEngine engine = Main.loader.engine;
        //Add event listener
        try {
            if (type.equals("onclick")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent arg0) {
                        try {
                            engine.eval(value, "RapidS "+type+" event.");
                        } catch (Exception e1) {
                            Utilities.showError("JavaScript error: ("+type+" event in "+widget.getName()+"): "+e1.getMessage());
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmouseover")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseEntered(MouseEvent arg0) {
                        try {
                            engine.eval(value, "RapidS "+type+" event.");
                        } catch (Exception e1) {
                            Utilities.showError("JavaScript error: ("+type+" event in "+widget.getName()+"): "+e1.getMessage());
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmouseout")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseExited(MouseEvent arg0) {
                        try {
                            engine.eval(value, "RapidS "+type+" event.");
                        } catch (Exception e1) {
                            Utilities.showError("JavaScript error: ("+type+" event in "+widget.getName()+"): "+e1.getMessage());
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmousedown")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent arg0) {
                        try {
                            engine.eval(value, "RapidS "+type+" event.");
                        } catch (Exception e1) {
                            Utilities.showError("JavaScript error: ("+type+" event in "+widget.getName()+"): "+e1.getMessage());
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onmouseup")) {
                widget.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseReleased(MouseEvent arg0) {
                        try {
                            engine.eval(value, "RapidS "+type+" event.");
                        } catch (Exception e1) {
                            Utilities.showError("JavaScript error: ("+type+" event in "+widget.getName()+"): "+e1.getMessage());
                            e1.printStackTrace();
                        }

                    }

                });

            } else if (type.equals("onselection")) {
                ((JSpinner) widget).addChangeListener(new ChangeListener(){
                    @Override
                    public void stateChanged(ChangeEvent arg0) {
                        try {
                            engine.eval(value, "RapidS "+type+" event.");
                        } catch (Exception e1) {
                            Utilities.showError("JavaScript error: ("+type+" event in "+widget.getName()+"): "+e1.getMessage());
                        }
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
