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

import java.awt.GridBagConstraints;
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

                String widgetIdentifier = widgetElement.getNodeName();
                Map<String, String> styles = Globals.stylesMap.get(widgetIdentifier);
                loadConstraintStyles(widgetConstraint, styles, widgetIdentifier);
            }

            if (widgetElement.getAttributeNode("class") != null) {

                String widgetIdentifier = widgetElement.getAttributeNode("class").getTextContent();
                Map<String, String> styles = Globals.stylesMap.get("."+widgetIdentifier);
                loadConstraintStyles(widgetConstraint, styles, widgetIdentifier);
            }

            if (widgetElement.getAttributeNode("id") != null) {

                String widgetIdentifier = widgetElement.getAttributeNode("id").getTextContent();
                Map<String, String> styles = Globals.stylesMap.get("#"+widgetIdentifier);
                loadConstraintStyles(widgetConstraint, styles, widgetIdentifier);
            }


        } catch (Exception e) {
            //Show a general error
            e.printStackTrace();
            Utilities.showError("Invalid CSS for "+widgetElement.getNodeName()+".");
        }
        return widgetConstraint;
    }

    /**
     * Applies the styles for a widget constraint.
     * @param widgetConstraint The constraint to operate on.
     * @param styles The styles to apply.
     */
    private static void loadConstraintStyles(GridBagConstraints widgetConstraint, Map<String, String> styles, String widgetIdentifier) {

        //Create a map of styles from the JavaScript object styles.layoutStyles
        HashMap<String, Object> layoutStyleTypes = Main.loader.engine.getMap("layoutStyles", "styles");

        //If there are styles for this identifier
        if (styles != null && !styles.isEmpty()) {

            //Iterate through them
            for (int i = 0; i < styles.size(); i++) {
                //Get the style name
                String style = (String) styles.keySet().toArray()[i];

                //If there is a style by this name
                if (layoutStyleTypes.containsKey(style)) {

                    try {
                        //Attempt to apply it.
                        Main.loader.engine.call("styles.layoutStyles."+style+".apply", widgetConstraint, styles.get(style));
                    } catch (Exception e) {
                        //Show an error if it is invalid
                        Utilities.showError("Invalid CSS: '"+widgetIdentifier+" {"+style+" = "+styles.get(style)+";}'. "
                                + "\n\n Error: "+e.getMessage()+"\n\n Program may not behave as expected.");
                    }

                //If this style does not exist
                } else {
                    //FIXME: Doesn't handle widget style types correctly.
                    //Utilities.showError("CSS style '"+style+"' (from: "+widgetIdentifier+") does not exist! \nProgram may not behave as expected.");
                }
            }
        }
        return;
    }
}
