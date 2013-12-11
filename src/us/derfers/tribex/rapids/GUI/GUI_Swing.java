package us.derfers.tribex.rapids.GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Composite;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.miginfocom.swing.MigLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.Loader;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;
import us.derfers.tribex.rapids.parsers.CSSParser;
import static us.derfers.tribex.rapids.Utilities.debugMsg;

public class GUI_Swing {
	
	//Constants that need to be defined to keep the GUI in one frame system.
	private static JFrame window = new JFrame();
	private static JPanel windowPanel = new JPanel();
	
	public static void loadGUI(String filePath, ScriptEngine engine, Object parent, Boolean clearWidgets) {
		//XXX: Initialization :XXX\\
		/*
		 * Basically: Port the following code to Swing, nothing hard. :P   
		*/
		//If this is the initial run, and therefore there is no shell or display, initialize them
		
		//Create ParentComposite variable
		JPanel parentComposite = null;
		
		//Check to see if the parent exists
		if (parent == null || parent.getClass().equals(JFrame.class)) {
			//If it does not exist, add the the default panel to the window
			window.getContentPane().add(windowPanel);
			
			//Set the parentComposite to the windowPanel
			parentComposite = windowPanel;
		} else {
			//Set the parentComposite to the Parent
			parentComposite = (JPanel) parent;
		}
		
		//TODO: Flexible layout types
		//Create a gridbaglayout
		windowPanel.setLayout(new GridBagLayout());

		try {

			//XML File Loading
			File file = new File(filePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			//XXX: HEAD :XXX\\
			//Get Window information from the Head Element
			NodeList headElementList = doc.getElementsByTagName("head").item(0).getChildNodes();

			//Loop through the children of the Head element
			for (int counter=0; counter < headElementList.getLength(); counter++) {
				
				Node headNode = headElementList.item(counter);
				
				//Make sure we have real element nodes
				if (headNode.getNodeType() == Node.ELEMENT_NODE) {
					final Element headElement = (Element) headNode;
					//Set the shell title with the title or window_title node
					if (headElement.getNodeName().equals("title") || headElement.getNodeName().equals("window_title")) {
						window.setTitle(headElement.getTextContent());
					} else if (headElement.getNodeName().equals("style")) {
							CSSParser parser = new CSSParser(headElement.getTextContent());
							System.out.println(parser.parseAll().toString());
						
						//JButtons
					}
				}
			}
			
			//XXX: BODY : XXX\\
			//Loop through all children of the body element and add them
			loadInComposite(parentComposite, doc.getElementsByTagName("body").item(0), engine); 
			
		} catch (Exception e) {
			e.printStackTrace();
			Utilities.showError("Unable to properly initialize a SWT GUI. \n"+filePath+" may be corrupt or incorrectly formatted.");
		}

		//Fit the window to the elements in it.
		window.pack();

		//Create a subMap for holding the Window and any properties of it
		Map<String, Object> shellMap = new HashMap<String, Object>();

		shellMap.put("__WINDOW__", window);

		Main.loader.XMLWidgets.put("__WINDOW__", shellMap);
		
		//Loading the JS must be done ABSOLUTELY LAST before the open call, or some properties will be missed.
		Main.loader.loadJS(filePath, engine);
		
		//Open the window
		window.setVisible(true);

		//XXX: END WIDGET CREATION :XXX\\	
	}
	
	//TODO: Comment
	private static void loadInComposite(JPanel parentComposite, Node node, ScriptEngine engine) {
		GridBagConstraints widgetConstraint = new GridBagConstraints(){

			private static final long serialVersionUID = 1L;

			{
				fill = GridBagConstraints.BOTH;
				gridx = GridBagConstraints.RELATIVE;
				gridy = GridBagConstraints.RELATIVE;
			}
		};
			//XXX: BODY : XXX\\
		debugMsg(parentComposite.toString());

			//Get Widgets from the Body Element
			NodeList bodyElementList = node.getChildNodes();
			//Loop through all children of the root element.
			for (int counter=0; counter < bodyElementList.getLength(); counter++) {

				Node widgetNode = bodyElementList.item(counter);

				if (widgetNode.getNodeType() == Node.ELEMENT_NODE) {
					final Element widgetElement = (Element) widgetNode;

					//Load all link tags
					if (widgetElement.getNodeName().equals("link")) {
						if (widgetElement.getAttributeNode("href") != null) {
							Main.loader.loadAll((widgetElement.getAttributeNode("href").getNodeValue()), parentComposite, false, engine);

						} else {
							Utilities.showError("Warning: <link> tags must contain a href attribute.");
						}
					
					//JButtons
					} else if (widgetElement.getNodeName().equals("button")) {
						JButton widget = new JButton();
						
						parentComposite.add(widget, widgetConstraint);
						//Set button text with the content of the <button></button> tags
						widget.setText(widgetElement.getTextContent());

						
						//Iterate through listener types and set listeners if they exist
						for (String listenerType : Main.loader.listenerTypesMap.keySet()) {
							//Add a listener for listenerType if specified
							if (widgetElement.getAttributeNode(listenerType) != null) {
								addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
							}
						
						}
						Loader.addWidgetToMaps(widgetElement, widget, engine);

					} else if (widgetElement.getNodeName().equals("spinner")) {
						//SPINNER
						final Element spinnerElement = (Element) widgetNode;
						//Create a new composite for sub-elements
						SpinnerNumberModel model = new SpinnerNumberModel();

						if (spinnerElement.getAttributeNode("value") != null) {
							model.setValue(Integer.valueOf(spinnerElement.getAttributeNode("value").getNodeValue()));
						}

						if (spinnerElement.getAttributeNode("max") != null) {
							model.setMaximum(Integer.valueOf(spinnerElement.getAttributeNode("max").getNodeValue()));
						}

						if (spinnerElement.getAttributeNode("min") != null) {
							model.setMinimum(Integer.valueOf(spinnerElement.getAttributeNode("min").getNodeValue()));
						}
						
						//Add widget to maps
						JSpinner widget = new JSpinner(model);

						Loader.addWidgetToMaps(spinnerElement, widget, engine);
					
					//LABEL CODE
					} else if (widgetElement.getNodeName().equals("label")) {
						final Element labelElement = (Element) widgetNode;

						JLabel widget = new JLabel();

						widget.setText(labelElement.getTextContent());

						parentComposite.add(widget, widgetConstraint);

						Loader.addWidgetToMaps(labelElement, widget, engine);
						for (String listenerType : Main.loader.listenerTypesMap.keySet()) {
							//Add a listener for listenerType if specified
							if (widgetElement.getAttributeNode(listenerType) != null) {
								addMethodListener(listenerType, (Component) widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
							}
						
						}

					}
				
				}

			}
			//position and draw the widgets
			//parentComposite.doLayout();
			//window.pack();

	}
	
	
	private static void addMethodListener(String type, Component widget, final String value, final ScriptEngine engine) {		
		//Add event listener
		((JComponent) widget).addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					engine.eval(value);
				} catch (ScriptException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
