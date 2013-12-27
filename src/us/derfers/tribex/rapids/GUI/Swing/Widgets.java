package us.derfers.tribex.rapids.GUI.Swing;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import us.derfers.tribex.rapids.Globals;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.ScriptEngine;
import us.derfers.tribex.rapids.GUI.Swing.Layouts;
import us.derfers.tribex.rapids.GUI.Swing.WidgetOps;

/**
 * Provides methods for creating widgets inside composites. 
 * Might be better written as seperate classes that extend their respective widgets
 * @author TribeX
 * @deprecated In favor of a widgets.js based solution. see createButton src for an example.
 */
public class Widgets {
	//TODO: Comment widgets!!!!!!!!!		

	//Get global JavaScript engine
	private static ScriptEngine engine = Main.loader.engine;

	/**
	 * Creates a Composite (JPanel) and populates any widgets inside of it.
	 * @param parentComposite The parent JPanel to place the new JPanel in.
	 * @param widgetElement The element to get the widget from.
	 */
	public static void createComposite(JPanel parentComposite, Element widgetElement) {
		//Create a new Panel
		JPanel widget = new JPanel();

		//Set the layout of the panel to GridBagLayout TODO: Add more layout types
		widget.setLayout(new GridBagLayout());

		//Add the panel to the window with all of its constraints.
		parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

		//Load all elements inside of the composite/widget.  INFINITE NESTING!
		GUI.loadInComposite(widget, (Node) widgetElement, engine);

		//Iterate through listener types and set listeners if they exist
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}

		//Add the panel to the maps. child widgets will still be toplevel.  There is no concept of a widget 'scope' TODO?
		WidgetOps.addWidgetToMaps(widgetElement, widget, engine);
	}


	/**
	 * Creates a Basic JButton
	 * @param parentComposite The parent JPanel to place the new JButton in.
	 * @param widgetElement The element to get the widget from.
	 */
	public static void createButton(JPanel parentComposite, Element widgetElement) {
		engine.call("widgets.button", parentComposite, widgetElement, engine);
	}


	/**
	 * Creates a Basic JLabel
	 * @param parentComposite The parent JPanel to place the new JLabel in.
	 * @param widgetElement The element to get the widget from.
	 */
	public static void createLabel(JPanel parentComposite, Element widgetElement) {
		JLabel widget = new JLabel();
		parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

		widget.setText(widgetElement.getTextContent());

		WidgetOps.addWidgetToMaps(widgetElement, widget, engine);
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}
	}


	/**
	 * Creates a Basic JSpinner
	 * @param parentComposite The parent JPanel to place the new JSpinner in.
	 * @param widgetElement The element to get the widget from.
	 */
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
		parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));


		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}
		WidgetOps.addWidgetToMaps(widgetElement, widget, engine);
	}


	/**
	 * Creates a Basic JTextArea
	 * @param parentComposite The parent JPanel to place the new JTextArea in.
	 * @param widgetElement The element to get the widget from.
	 */
	public static void createTextArea(JPanel parentComposite, Element widgetElement) {

		//Create a new textarea
		JTextArea widget  = new JTextArea();

		//Create a new scrollpane and add the textarea to it
		JScrollPane scrollPane = new JScrollPane(widget);  

		//Set the text of the textArea
		widget.setText(widgetElement.getTextContent());

		//Add the scrollpane to the parentComposite
		parentComposite.add(scrollPane, Layouts.getWidgetConstraint(widgetElement));

		//Add event listeners for the textarea
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}

		//Add the widget to the maps
		WidgetOps.addWidgetToMaps(widgetElement, widget, engine);
	}


	/**
	 * Creates a Basic JTextField
	 * @param parentComposite The parent JPanel to place the new JTextField in.
	 * @param widgetElement The element to get the widget from.
	 */
	public static void createTextField(JPanel parentComposite, Element widgetElement) {

		//Create a new textarea
		JTextField widget  = new JTextField();

		//Set the text of the textArea
		widget.setText(widgetElement.getTextContent());

		//Add the scrollpane to the parentComposite
		parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

		//Add event listeners for the textarea
		for (String listenerType : Globals.listenerTypesArray) {
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}

		//Add the widget to the maps
		WidgetOps.addWidgetToMaps(widgetElement, widget, engine);
	}

}
