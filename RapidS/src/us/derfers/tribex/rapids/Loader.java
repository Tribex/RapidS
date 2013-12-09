package us.derfers.tribex.rapids;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.GUI.*;

public class Loader {
	//Javascript engine initialization
		public ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

	//What the widgets are stored in when loaded
		public Map<String, Map<String, Object>> XMLWidgets = new HashMap<String, Map<String, Object>>();
	
		//Counter for ID-less XML Elements
		public Integer XMLWidgets__NO__ID = 0;
				
	//Listener types, eg. onclick, onmouseover, onmouoseout, etc.
	public  HashMap<String, Integer> listenerTypesMap = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;

		{
			put("onmouseup", SWT.MouseUp);
			put("onmousedown", SWT.MouseDown);
			put("onselection", SWT.Selection);
			put("onclick", SWT.Selection);
			put("onmouseover", SWT.MouseEnter);
			put("onmouseout", SWT.MouseExit);
		}
	};
	

	
	//XXX: Overload for maintaining a ScriptEngine :XXX\\
	public void loadAll(String filePath, Boolean clearWidgets) {
		//JavaScript Engine Initialization
		//---------------------------------------------------------------------------//
		//Start Engine:
		engine.put("engine", engine);
		
		//Import standard functions:
		try {
			engine.eval("importPackage(Packages.us.derfers.tribex.rapids.jsFunctions);");
			
			// TODO Add JS-based functions
			engine.eval("");
		} catch (ScriptException e1) {
			e1.printStackTrace();
			Utilities.showError("Error initializing JavaScript engine, please make sure you have Java 6+\n\n"
					+ "If you do, please report this error:\n"+e1.getMessage());
		}
		//---------------------------------------------------------------------------//
		
		//Begin loading the XML file(s)
		loadAll(filePath, null, clearWidgets, engine);
	}
	
	//XXX: Function for determining GUI type and loading appropriate GUI engine :XXX\\
	public void loadAll(String filePath, final Object parent, Boolean clearWidgets, ScriptEngine engine) {
		
		//Attempt to load .lcm file filePath
		try {
			File file = new File(filePath);
			
			//Start XML Document factory/builder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//Parse filePath
			Document doc = db.parse(file);
			
			//Stabilize parsed document
			doc.normalize();
			
			//Get body element
			NodeList bodyNodeList = doc.getElementsByTagName("body");
			
			//Make sure there is only ONE body element
			if (bodyNodeList.getLength() == 1) {
				//Get body Element
				Element bodyElement = (Element) bodyNodeList.item(0);

				if (bodyElement.getAttributeNode("gui_type") != null) {
					//Get the value of attribute gui_type for the body element
					String temp_GUI_type = bodyElement.getAttributeNode("gui_type").getNodeValue();
					
					//Get the value of the attribute theme for the body element
					Attr swing_Theme = bodyElement.getAttributeNode("theme");
					
					//See if the lcm file specifies a theme other than camo
					if (swing_Theme != null && !swing_Theme.getNodeValue().equalsIgnoreCase("camo")) {
						try {
							//Attempt to set the look'n'feel to the theme specified by the .lcm
							UIManager.setLookAndFeel(swing_Theme.getNodeValue());
							
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
							//If unable to set to .lcm's theme, use the system look'n'feel
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							
						}
					 } else {
						//If swing_Theme == camo or is not set, use the system look'n'feel
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					 }
					
					
					//Determine GUI type and load appropriate GUI toolkit
					if (temp_GUI_type.equals("Auto")) {
						//TODO: detect whether SWT works on platform or not and use it appropriately
						GUI_Swing.loadGUI(filePath, engine, null, false);

					} else if (temp_GUI_type.equals("Swing")) {
						//Load the Swing GUI system
						GUI_Swing.loadGUI(filePath, engine, null, false);

					} else if (temp_GUI_type.equals("SWT")) {
						//Load the SWT GUI system
						GUI_SWT.loadGUI(filePath, engine, null, false);
					}
					
					//Load the JavaScript Interpereter and run <script> tags
				}
				
			} else { //There was more than one body tag, or 0 body tags
				
				//Display Error and quit, as we cannot recover from an abnormally formatted file
				Utilities.showError("Error: More or less than one <body> tag in '"+filePath+"'.\n\n"
				+ "Please add ONE body tag to '"+filePath+"'.");
				System.exit(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

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
	
	public void loadJS(String filePath, ScriptEngine engine) {
		System.out.println("Loading JavaScript");
		//XXX: JAVASCRIPT HANDLING SECTION :XXX\\
		try {
		File file = new File(filePath);
		
		//Start XML Document factory/builder
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		//Parse filePath
		Document doc = db.parse(file);
		
		//Stabilize parsed document
		doc.normalize();
		//Execute anything in script tags. JavaScript
		//Load all <script> tags
		NodeList scriptNodes = doc.getElementsByTagName("script");
		
		JSIterator(scriptNodes, engine);
		//Iterate through all script tags and run the code in them
		
		//XXX: END JAVASCRIPT HANDLING :XXX\\
		} catch (Exception e) {
			e.printStackTrace();
			Utilities.showError("Error loading Javascripts from '"+filePath+"'. Please check their validity.");
		}
	}
	
	private void JSIterator(NodeList scriptNodes, ScriptEngine engine) {
		for (int i=0; i < scriptNodes.getLength(); i++) {
			//Get the specific <script> tag for this loop
			Node scriptElement = scriptNodes.item(i);
			
			//Double check, juuust to make sure that it is the right type of tag
			if (scriptElement.getNodeName().equals("script")) {
				//Run all the code inside the <script> tags
				try {
					engine.eval(scriptElement.getTextContent());
				} catch (DOMException | ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

}
