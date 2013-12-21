package us.derfers.tribex.rapids.GUI.Swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import us.derfers.tribex.rapids.Utilities;
import us.derfers.tribex.rapids.Globals;

public class Layouts {

	public static GridBagConstraints getWidgetConstraint(Element widgetElement) {
		//Default Styles for widgetConstraint
		GridBagConstraints widgetConstraint = new GridBagConstraints(){

			private static final long serialVersionUID = 1L;

			{
				anchor = GridBagConstraints.LINE_START;
				fill = BOTH;
				ipadx = 5;
				ipadx = 5;
				weightx = 0.1;
				weighty = 0.1;
				gridx = 0;
				gridy = GridBagConstraints.RELATIVE;
			}
		};


		//Load styles from ID
		try {
			if (widgetElement.getNodeName() != null) {
				Map<String, String> styles = Globals.stylesMap.get(widgetElement.getNodeName());
				loadConstraintStyles(widgetConstraint, widgetElement, styles);
			}

			if (widgetElement.getAttributeNode("class") != null) {
				Map<String, String> styles = Globals.stylesMap.get("."+widgetElement.getAttributeNode("class").getTextContent());
				loadConstraintStyles(widgetConstraint, widgetElement, styles);
			}
			
			if (widgetElement.getAttributeNode("id") != null) {
				Map<String, String> styles = Globals.stylesMap.get("#"+widgetElement.getAttributeNode("id").getTextContent());
				loadConstraintStyles(widgetConstraint, widgetElement, styles);
			} 


		} catch (Exception e) {
			Utilities.showError("Bad CSS");
		}
		return widgetConstraint;
	}
	
	private static void loadConstraintStyles(GridBagConstraints widgetConstraint, Element widgetElement, Map<String, String> styles) {

		//Margins, so that we can use multiple styles to accomplish the same thing
		Map<String, Integer> margins = new HashMap<String, Integer>(){
			private static final long serialVersionUID = 1L;
			{
				put("left", 0);
				put("right", 0);
				put("top", 0);
				put("bottom", 0);
			}
		};
		//Get the styles for the widget's ID

		//If there are some styles in it
		if (styles != null) {
			//Set fill direction(s)
			if (styles.get("fill") != null) {
				//If the widget is to fill horizontally
				if (styles.get("fill").equalsIgnoreCase("horizontal")) {
					widgetConstraint.fill = GridBagConstraints.HORIZONTAL;

					//If the widget is to fill vertically
				} else if (styles.get("fill").equalsIgnoreCase("vertical")){
					widgetConstraint.fill = GridBagConstraints.VERTICAL;

					//If the widget is to fill both horizontally and vertically
				} else if (styles.get("fill").equalsIgnoreCase("both")){
					widgetConstraint.fill = GridBagConstraints.BOTH;

					//If the widget is not to fill
				} else if (styles.get("fill").equalsIgnoreCase("none")){
					widgetConstraint.fill = GridBagConstraints.NONE;
				}
			}
			//Set the x grid position
			if (styles.get("position-x") != null) {
				if (styles.get("position-x").contains("rel")) {
					widgetConstraint.gridx = GridBagConstraints.RELATIVE;
				} else {
					widgetConstraint.gridx = Integer.valueOf(styles.get("position-x"));
				}
			}

			//Set the y grid position
			if (styles.get("position-y") != null) {
				if (styles.get("position-y").contains("rel")) {
					widgetConstraint.gridy = GridBagConstraints.RELATIVE;
				} else {
					widgetConstraint.gridy = Integer.valueOf(styles.get("position-y"));
				}
			}

			//Set the x weight
			if (styles.get("weight-x") != null) {
				widgetConstraint.weightx = Float.valueOf(styles.get("weight-x"));
			}

			//Set the y weight
			if (styles.get("weight-y") != null) {
				widgetConstraint.weighty = Float.valueOf(styles.get("weight-y"));
			}

			//Set the margin-left
			if (styles.get("margin-left") != null) {
				margins.put("left", Integer.valueOf(styles.get("margin-left")));
			}

			//Set the margin-right
			if (styles.get("margin-right") != null) {
				margins.put("right", Integer.valueOf(styles.get("margin-right")));
			}

			//Set the margin-top
			if (styles.get("margin-top") != null) {
				margins.put("top", Integer.valueOf(styles.get("margin-top")));
			}

			//Set the margin-bottom
			if (styles.get("margin-bottom") != null) {
				margins.put("bottom", Integer.valueOf(styles.get("margin-bottom")));
			}
			//Set the y external margin
			if (styles.get("margin") != null) {
				
				//Split string (int int) or (int int int int) into an array 
				String [] tempMargins = styles.get("margin").split(" ");
				
				//If the user has specified different values for every side, set them
				if (tempMargins.length == 4) {
					margins.put("top", Integer.valueOf(tempMargins[0]));
					margins.put("left", Integer.valueOf(tempMargins[1]));
					margins.put("bottom", Integer.valueOf(tempMargins[2]));
					margins.put("right", Integer.valueOf(tempMargins[3]));
					
				//If the user has specified horizontal and vertical values, set them
				} else if (tempMargins.length == 2) {
					margins.put("top", Integer.valueOf(tempMargins[0]));
					margins.put("left", Integer.valueOf(tempMargins[1]));
					margins.put("bottom", Integer.valueOf(tempMargins[0]));
					margins.put("right", Integer.valueOf(tempMargins[1]));
				}

			}

			//Set the x internal padding
			if (styles.get("padding-x") != null) {
				widgetConstraint.ipadx = Integer.valueOf(styles.get("padding-x"));
			}

			//Set the y internal padding
			if (styles.get("padding-y") != null) {
				widgetConstraint.ipady = Integer.valueOf(styles.get("padding-y"));
			}

			//Set the occupied cells x-dir
			if (styles.get("occupied-cells-x") != null) {
				widgetConstraint.gridwidth = Integer.valueOf(styles.get("occupied-cells-x"));
			}

			//Set the occupied cells y-dir
			if (styles.get("occupied-cells-y") != null) {
				widgetConstraint.gridheight = Integer.valueOf(styles.get("occupied-cells-y"));
			}

			//Set the anchor
			if (styles.get("anchor") != null) {
				Field f = null;
				try {
					f = widgetConstraint.getClass().getField(styles.get("anchor").toUpperCase().replace('-', '_'));
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					widgetConstraint.anchor = f.getInt(widgetConstraint);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			widgetConstraint.insets = new Insets(margins.get("top"), margins.get("left"), margins.get("bottom"), margins.get("right"));

		}
	}
}
