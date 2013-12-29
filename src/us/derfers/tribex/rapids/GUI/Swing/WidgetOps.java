package us.derfers.tribex.rapids.GUI.Swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

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
	//Widget styles
	/**
	 * Returns a widget styled with all the styles specified in its element, class.
	 * @param widget The original widget
	 * @param id The ID of the original widget
	 * @return The styled widget
	 */
	public static JComponent getWidgetStyles(JComponent widget, String id) {
		//Get the widget data for the id of the widget.
		Map<String, Object> widgetData = Main.loader.XMLWidgets.get(id);

		//If the element is specified (should be, but just to make sure)
		if (Globals.stylesMap.get(widgetData.get("element"))!= null) {
			//Get the styles for the element
			Map<String, String> styles = Globals.stylesMap.get(Main.loader.XMLWidgets.get(id).get("element"));

			//Load the widget styles.
			widget = loadWidgetStyles(widget, styles);
		}

		//If the class is specified
		if (Globals.stylesMap.get("."+widgetData.get("class"))!= null) {
			//Get the styles for the class
			Map<String, String> styles = Globals.stylesMap.get("."+Main.loader.XMLWidgets.get(id).get("class"));

			//Load the widget styles.
			widget = loadWidgetStyles(widget, styles);
		}

		//If the id is specified (It ought to be, but just to be sure)
		if (Globals.stylesMap.get("#"+id)!= null) {
			//Get the styles for the ids
			Map<String, String> styles = Globals.stylesMap.get("#"+id);
            widget.setName(id);

			//Load the widget styles.
			widget = loadWidgetStyles(widget, styles);
		}

		return widget;
	}


	/**
	 * Loads and applies styles for widgets.
	 * @param widget The widget to apply styles to.
	 * @param styles A Map of styles for that widget
	 * @return The widget with all the styles applied.
	 */
	public static JComponent loadWidgetStyles(JComponent widget, Map<String, String> styles) {

		//Make sure that there are styles to apply
		if (styles != null && !styles.isEmpty()) {
			//Set background color
			if (styles.get("background-color") != null) {
				//Set the background color of the widget
				widget.setBackground(Color.decode(styles.get("background-color")));

				//Necessarry for some widgets to display the background, no adverse effects afaik.
				widget.setOpaque(true);
			}

			//Set foregound color
			if (styles.get("foreground-color") != null) {
				//Set the foreground color of the widget
				widget.setForeground(Color.decode(styles.get("foreground-color")));
			}

			//Create a border TODO: make more flexible and advanced
			if (styles.get("border") != null) {
				//Split the border string between color and width
				String[] borderinfo = styles.get("border").split(" ");

				//Create a new border
				Border border = BorderFactory.createLineBorder(Color.decode(borderinfo[1]), Integer.valueOf(borderinfo[0]));
				widget.setBorder(border);
			}

			if (styles.get("width") != null) {
				//Set the width of the widget
				widget.setPreferredSize(new Dimension(Integer.valueOf(styles.get("width")), widget.getPreferredSize().height));
			}

			if (styles.get("height") != null) {
				//Set the height of the widget
				widget.setPreferredSize(new Dimension(widget.getPreferredSize().width, Integer.valueOf(styles.get("height"))));
			}

			if (styles.get("z-index") != null) {
				//Get the parent of the widget
				Container parent = widget.getParent();
				
				//Get the value set by CSS for the z-index
				int ZIndex = Integer.valueOf(styles.get("z-index"));
				
				//If ZIndex is larger than the amount of widgets in the parent, decrease it to avoid a NPE.
				if (ZIndex > parent.getComponentCount()-1) {
					ZIndex = parent.getComponentCount()-1;
					
				//If ZIndex is less than 0, set it to 0 to avoid a NPE or IVE
				} else if (ZIndex < 0) {
					ZIndex = 0;
				}
				parent.setComponentZOrder(widget, ZIndex);
			}
			
			if (styles.get("visibility") != null) {
				String visibility = styles.get("visibility");
				
				if (visibility.equalsIgnoreCase("hidden")) {
					widget.setVisible(false);
					
				} else if (visibility.equalsIgnoreCase("visible")) {
					widget.setVisible(true);
				} else {
					widget.setVisible(false);
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


	//XXX: Map setup :XXX\\

	public static void addWidgetToMaps(Element widgetElement, Object widget, ScriptEngine engine) {
		//Create a HashMap to hold Button ID and class, as well as other parameters.
		Map<String, Object> widgetMap = new HashMap<String, Object>();

		//Define the ID of the button
		String widgetID = null;
		if (widgetElement.getAttributeNode("id") != null) {
			widgetID = widgetElement.getAttributeNode("id").getNodeValue();

			//If not, assign an incremental id: __ID__#
		} else {
			widgetID = "__ID__"+Integer.toString(Main.loader.XMLWidgets__NO__ID+1);
			Main.loader.XMLWidgets__NO__ID += 1;
		}
		Utilities.debugMsg("Adding widget "+widgetID+" to XMLWidgets.", 3);

		//Add the widget to the widgetMap
		widgetMap.put(widgetID, widget);

		//If the ID is set, add it to the Object list so that we can get it later.
		NamedNodeMap widgetAttributes = widgetElement.getAttributes();

		//Iterate through all the attributes of the widget and add them to the widgetMap
		for (int i=0; i < widgetAttributes.getLength(); i++) {
			widgetMap.put(widgetAttributes.item(i).getNodeName(), widgetAttributes.item(i).getTextContent());
		}

		widgetMap.put("element", widgetElement.getNodeName());
		widgetMap.put("id", widgetID);

		//Add the temporary widgetMap to the XMLWidgets array.
		Main.loader.XMLWidgets.put(widgetID, widgetMap);
		engine.put("$"+widgetID, widget);

	}
}
