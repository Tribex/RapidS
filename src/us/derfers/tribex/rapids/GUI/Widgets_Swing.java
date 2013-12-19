package us.derfers.tribex.rapids.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.w3c.dom.Element;

import us.derfers.tribex.rapids.Globals;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.ScriptEngine;
import us.derfers.tribex.rapids.GUI.Layouts_Swing;
import us.derfers.tribex.rapids.GUI.WidgetOps_Swing;

public class Widgets_Swing {
	//TODO: Comment widgets!!!!!!!!!		
	
	//Get global JavaScript engine
	private static ScriptEngine engine = Main.loader.engine;
	
	//XXX: BUTTONS - Will be added on to, this is basic. :XXX\\
	public static void createButton(JPanel parentComposite, Element widgetElement) {
		JButton widget = new JButton();

		parentComposite.add(widget, Layouts_Swing.getWidgetConstraint(widgetElement));
		//Set button text with the content of the <button></button> tags
		widget.setText(widgetElement.getTextContent());


		//Iterate through listener types and set listeners if they exist
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps_Swing.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}
		WidgetOps_Swing.addWidgetToMaps(widgetElement, widget, engine);
	}
	
	
	//XXX: LABELS - Will be added on to, this is basic. :XXX\\
	public static void createLabel(JPanel parentComposite, Element widgetElement) {
		JLabel widget = new JLabel();
		widget = (JLabel) WidgetOps_Swing.getWidgetStyles(widget, widgetElement);
		widget.setText(widgetElement.getTextContent());

		parentComposite.add(widget, Layouts_Swing.getWidgetConstraint(widgetElement));

		WidgetOps_Swing.addWidgetToMaps(widgetElement, widget, engine);
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps_Swing.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}
	}
	
	
	//XXX: SPINNERS :XXX\\\
	public static void createSpinner(JPanel parentComposite, Element widgetElement) {
		//Create a new composite for sub-elements
		SpinnerNumberModel model = new SpinnerNumberModel();

		if (widgetElement.getAttributeNode("value") != null) {
			model.setValue(Integer.valueOf(widgetElement.getAttributeNode("value").getNodeValue()));
		}

		if (widgetElement.getAttributeNode("max") != null) {
			model.setMaximum(Integer.valueOf(widgetElement.getAttributeNode("max").getNodeValue()));
		}

		if (widgetElement.getAttributeNode("min") != null) {
			model.setMinimum(Integer.valueOf(widgetElement.getAttributeNode("min").getNodeValue()));
		}

		//Add widget to maps
		JSpinner widget = new JSpinner(model);
		parentComposite.add(widget, Layouts_Swing.getWidgetConstraint(widgetElement));
		
		
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps_Swing.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}
		WidgetOps_Swing.addWidgetToMaps(widgetElement, widget, engine);
	}
	
	
	//XXX: TEXTAREAS :XXX\\
	public static void createTextArea(JPanel parentComposite, Element widgetElement) {
		
		//Create a new textarea
		JTextArea widget  = (JTextArea) WidgetOps_Swing.getWidgetStyles(new JTextArea(), widgetElement);

		//Create a new scrollpane and add the textarea to it
		JScrollPane scrollPane = new JScrollPane(widget);  

		//Set the text of the textArea
		widget.setText(widgetElement.getTextContent());
		
		//Add the scrollpane to the parentComposite
		parentComposite.add(scrollPane, Layouts_Swing.getWidgetConstraint(widgetElement));
		
		//Add event listeners for the textarea
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps_Swing.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}
		
		//Add the widget to the maps
		WidgetOps_Swing.addWidgetToMaps(widgetElement, widget, engine);
	}
	
	
	//XXX: TEXTFIELDS :XXX\\
		public static void createTextField(JPanel parentComposite, Element widgetElement) {
			
			//Create a new textarea
			JTextField widget  = (JTextField) WidgetOps_Swing.getWidgetStyles(new JTextField(), widgetElement);

			//Set the text of the textArea
			widget.setText(widgetElement.getTextContent());
			
			//Add the scrollpane to the parentComposite
			parentComposite.add(widget, Layouts_Swing.getWidgetConstraint(widgetElement));
			
			//Add event listeners for the textarea
			for (String listenerType : Globals.listenerTypesArray) {
				//Add a listener for listenerType if specified
				if (widgetElement.getAttributeNode(listenerType) != null) {
					WidgetOps_Swing.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
				}

			}
			
			//Add the widget to the maps
			WidgetOps_Swing.addWidgetToMaps(widgetElement, widget, engine);
		}
	
}
