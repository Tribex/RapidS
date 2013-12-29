package us.derfers.tribex.rapids.GUI.Swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;
import us.derfers.tribex.rapids.Globals;

/**
 * Provides layout constructors and styles.
 * 
 * @author TribeX
 *
 */
public class Layouts {

	/**
	 * Loads the CSS layout styles for a given widget.
	 * @param widgetElement The widgetElement to process.
	 * @return A GridBagConstraints containing all the styles.
	 */
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
				loadConstraintStyles(widgetConstraint, styles);
			}

			if (widgetElement.getAttributeNode("class") != null) {
				Map<String, String> styles = Globals.stylesMap.get("."+widgetElement.getAttributeNode("class").getTextContent());
				loadConstraintStyles(widgetConstraint, styles);
			}

			if (widgetElement.getAttributeNode("id") != null) {
				Map<String, String> styles = Globals.stylesMap.get("#"+widgetElement.getAttributeNode("id").getTextContent());
				loadConstraintStyles(widgetConstraint, styles);
			} 


		} catch (Exception e) {
			e.printStackTrace();
			Utilities.showError("Bad CSS for "+widgetElement.getNodeName()+".");
		}
		return widgetConstraint;
	}

	/**
	 * Applies the styles for a widget constraint.
	 * @param widgetConstraint The constraint to operate on.
	 * @param styles The styles to apply.
	 */
	private static void loadConstraintStyles(GridBagConstraints widgetConstraint, Map<String, String> styles) {

		//Create a map of styles from the JavaScript object styles.layoutStyles
		HashMap<String, Object> layoutStyleTypes = Main.loader.engine.getMap("layoutStyles", "styles");

		//Create and populate the list of registered widgets
		ArrayList<String> registeredLayoutStyles = new ArrayList<String>();
		for (String styleType : layoutStyleTypes.keySet()) {
			registeredLayoutStyles.add(styleType);
		}

		//NEW CODE
		if (styles != null) {
			for (int i = 0; i < styles.size(); i++) {
				String style = (String) styles.keySet().toArray()[i];
				if (layoutStyleTypes.containsKey(style)) {
					Main.loader.engine.call("styles.layoutStyles."+style+".apply", widgetConstraint, styles.get(style));
				}
			}
		}
		return;
	}
}
