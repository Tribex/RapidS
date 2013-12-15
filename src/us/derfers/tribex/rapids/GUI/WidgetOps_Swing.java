package us.derfers.tribex.rapids.GUI;

import java.awt.Color;
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

public class WidgetOps_Swing {
	//Widget styles
	public static JComponent getWidgetStyles(JComponent widget, Element widgetElement) {

		if (widgetElement.getNodeName() != null) {
			Map<String, String> styles = Globals.stylesMap.get(widgetElement.getNodeName());
			widget = loadWidgetStyles(widget, widgetElement, styles);
		}

		if (widgetElement.getAttributeNode("class") != null) {
			Map<String, String> styles = Globals.stylesMap.get("."+widgetElement.getAttributeNode("class").getTextContent());
			widget = loadWidgetStyles(widget, widgetElement, styles);
		}
		
		if (widgetElement.getAttributeNode("id") != null) {
			Map<String, String> styles = Globals.stylesMap.get("#"+widgetElement.getAttributeNode("id").getTextContent());
			widget = loadWidgetStyles(widget, widgetElement, styles);
		} 
		
		return widget;
	}
	
	
	
	public static JComponent loadWidgetStyles(JComponent widget, Element widgetElement, Map<String, String> styles) {
		
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
			
			if (styles.get("border") != null) {
				//Set the background of the widget
				String[] borderinfo = styles.get("border").split(" ");
				Border border = BorderFactory.createLineBorder(Color.decode(borderinfo[1]), Integer.valueOf(borderinfo[0]));
				widget.setBorder(border);
			}
		}
		
		return widget;
	}
	
	//Event listeners
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
		
		widgetMap.put("id", widgetID);
		
		//Add the temporary widgetMap to the XMLWidgets array.
		Main.loader.XMLWidgets.put(widgetID, widgetMap);
		engine.put("$"+widgetID, widget);
		
	}
}
