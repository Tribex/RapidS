package us.derfers.tribex.rapids.GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Composite;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;

public class GUI_Swing {
	public static void loadGUI(String filePath, ScriptEngine engine, JPanel parentComposite, Boolean clearWidgets) {

		//Set up initial GUI code
		JFrame mainFrame = new JFrame();
		parentComposite = new JPanel();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.getContentPane().add(parentComposite, BorderLayout.NORTH);
	    parentComposite.setLayout(new GridLayout(3, 3, 0, 3));
	    
		//If the user has specified the need to clear all widgets out of the parent Composite,
		if (clearWidgets == true) {
			//Clear the object list
			Main.loader.XMLWidgets.clear();
			System.out.println("cleared");
		}
		
		
		
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
						mainFrame.setTitle(headElement.getTextContent());
					}
				}
			}
			
			//XXX: BODY : XXX\\
			//Get Widgets from the Body Element
			NodeList bodyElementList = doc.getElementsByTagName("body").item(0).getChildNodes();
			//Loop through all children of the root element.
			for (int counter=0; counter < bodyElementList.getLength(); counter++) {

				Node widgetNode = bodyElementList.item(counter);

				if (widgetNode.getNodeType() == Node.ELEMENT_NODE) {
					final Element widgetElement = (Element) widgetNode;

					
					//Loading <link tags and running any script in them>
					if (widgetElement.getNodeName().equals("link")) {
						if (widgetElement.getAttributeNode("href") != null) {
							Main.loader.loadAll((widgetElement.getAttributeNode("href").getNodeValue()), parentComposite, false, engine);

						} else {
							Utilities.showError("Warning: <link> tags must contain a href attribute.");
						}
					
						
					//Load all Button Tags
					} else if (widgetElement.getNodeName().equals("button")) {
						//Get the button tags
						//Create a temporary button as a parent class Widget
						JButton tempbutton = new JButton();
						
						//Set style variables for temporary button
						parentComposite.add(tempbutton);
						
						//Set button text with the content of the <button></button> tags
						tempbutton.setText(widgetElement.getTextContent());
						
						//Identify the widget type via a data attribute
						
						//Iterate through listener types and set listeners if they exist
						for (String listenerType : Main.loader.listenerTypesMap.keySet()) {
							//Add a listener for listenerType if specified
							if (widgetElement.getAttributeNode(listenerType) != null) {
								//addMethodListener(listenerType, tempbutton, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
							}
						
						}
						//If the ID is set, add it to the Object list so that we can get it later.
						if (widgetElement.getAttributeNode("id") != null) {
							Main.loader.XMLWidgets.put(widgetElement.getAttributeNode("id").getNodeValue(), tempbutton);
							//tempbutton.setData("id", (widgetElement.getAttributeNode("id").getNodeValue()));
						
						//If not, assign an incremental id: __ID__#
						} else {
							Main.loader.XMLWidgets.put("__ID__"+Integer.toString(Main.loader.XMLWidgets__NO__ID+1), tempbutton);
							Main.loader.XMLWidgets__NO__ID += 1;
						}
						
						if (widgetElement.getAttributeNode("class") != null) {
							//tempbutton.setData("class", (widgetElement.getAttributeNode("class").getNodeValue()));
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
		mainFrame.pack();
		Main.loader.loadJS(filePath, engine);

		mainFrame.setVisible(true);
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
