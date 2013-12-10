package us.derfers.tribex.rapids.GUI;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.Loader;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;
import static us.derfers.tribex.rapids.Utilities.debugMsg;

public class GUI_SWT {
	static Display display = new Display();

	//XXX: Loading System for SWT :XXX\\
	public static void loadGUI(String filePath, ScriptEngine engine, Composite parentComposite, Boolean clearWidgets) {
		
		Shell shell = null;
		//XXX: Initialization :XXX\\
		//If this is the initial run, and therefore there is no shell or display, initialize them
		if (parentComposite == null) {
			shell = new Shell(display);
			shell.setLayout(new GridLayout(1, false));
			parentComposite = shell;
		} else {
			debugMsg(parentComposite.toString());
		}


		//If the user has specified the need to clear all widgets out of the parent Composite,
		if (clearWidgets == true) {
			//Iterate through the children of the composite
			for (Widget widget : parentComposite.getChildren()) {
				//dispose each child
				widget.dispose();
			}
			
			//Clear the object list.
			Main.loader.XMLWidgets.clear();
			Utilities.debugMsg("Cleared SWT composite.");
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
						shell.setText(headElement.getTextContent());
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
						Button widget = new Button(parentComposite, SWT.NONE);
						
						
						//Set style variables for temporary button
						widget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
						
						//Set button text with the content of the <button></button> tags
						widget.setText(widgetElement.getTextContent());
						
						//Identify the widget type via a data attribute
						widget.setData("type", "button");
						
						//Iterate through listener types and set listeners if they exist
						for (String listenerType : Main.loader.listenerTypesMap.keySet()) {
							//Add a listener for listenerType if specified
							if (widgetElement.getAttributeNode(listenerType) != null) {
								addMethodListener(listenerType, (Widget) widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
							}
						
						}
						Loader.addWidgetToMaps(widgetElement, widget, engine);

					} else if (widgetElement.getNodeName().equals("spinner")) {
						//SPINNER
						final Element spinnerElement = (Element) widgetNode;
						//Create a new composite for sub-elements
						
						Spinner tempspinner = new Spinner(parentComposite, SWT.BORDER);
						tempspinner.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

						if (spinnerElement.getAttributeNode("value") != null) {
							tempspinner.setSelection(Integer.valueOf(spinnerElement.getAttributeNode("value").getNodeValue()));
						}

						if (spinnerElement.getAttributeNode("max") != null) {
							tempspinner.setMaximum(Integer.valueOf(spinnerElement.getAttributeNode("max").getNodeValue()));
						}

						if (spinnerElement.getAttributeNode("min") != null) {
							tempspinner.setMinimum(Integer.valueOf(spinnerElement.getAttributeNode("min").getNodeValue()));
						}
						
						//Add widget to maps
						Loader.addWidgetToMaps(spinnerElement, tempspinner, engine);
					
					//LABEL CODE
					} else if (widgetElement.getNodeName().equals("label")) {
						final Element labelElement = (Element) widgetNode;

						Label templabel = new Label(parentComposite, SWT.NONE);
						templabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
						templabel.setText(labelElement.getTextContent());

						Loader.addWidgetToMaps(labelElement, templabel, engine);
						for (String listenerType : Main.loader.listenerTypesMap.keySet()) {
							//Add a listener for listenerType if specified
							if (widgetElement.getAttributeNode(listenerType) != null) {
								addMethodListener(listenerType, templabel, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
							}
						
						}

					}
				
				}

			}
			//position and draw the widgets
			parentComposite.layout();
			
		} catch (Exception e) {
			e.printStackTrace();
			Utilities.showError("Unable to properly initialize a SWT GUI. \n"+filePath+" may be corrupt or incorrectly formatted.");
		}

		shell.pack();
		//Add the shell to XMLWidgets, after everything has been added, so that we can access it from JS
		//Create a subMap for holding the Shell and any properties of it
		Map<String, Object> shellMap = new HashMap<String, Object>();

		shellMap.put("__SHELL__", shell);

		Main.loader.XMLWidgets.put("__SHELL__", shellMap);
		
		//Loading the JS must be done ABSOLUTELY LAST before the open call, or some properties will be missed.
		Main.loader.loadJS(filePath, engine);
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

			//XXX: END WIDGET CREATION :XXX\\	
	}
	
	
	private static void addMethodListener(String type, Widget widget, final String value, final ScriptEngine engine) {		
		//Add event listener
		widget.addListener(Main.loader.listenerTypesMap.get(type), new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				try {
					engine.eval(value);
				} catch (ScriptException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}
	

}
