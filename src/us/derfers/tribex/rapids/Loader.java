package us.derfers.tribex.rapids;
import static us.derfers.tribex.rapids.Utilities.debugMsg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.GUI.GUI_Swing;
import us.derfers.tribex.rapids.jsFunctions.sys;

public class Loader {
	//Javascript engine initialization
		public ScriptEngine engine = new ScriptEngine();

	//What the widgets are stored in when loaded
		public Map<String, Map<String, Object>> XMLWidgets = new HashMap<String, Map<String, Object>>();
	
		//Counter for ID-less XML Elements
		public Integer XMLWidgets__NO__ID = 0;


	
	//XXX: Overload for maintaining a ScriptEngine :XXX\\
	public void loadAll(String filePath, Boolean clearWidgets) {
		//JavaScript Engine Initialization
		//---------------------------------------------------------------------------//
		//Start Engine:
		debugMsg("JavaScript Engine Started", 4);
		
		//Import standard functions:
		try {
			for (String toImport: Globals.SystemPackages) {
				engine.eval("importPackage(Packages."+toImport+");");
				debugMsg("Imported Java Class: "+toImport);
			}
			debugMsg("Imported JavaScript Standard Library (Java-based)", 4);

			// TODO Add JS-based functions
			//FIXME: Find a way to load files in the jar System.out.println(Main.class.getClassLoader().getResourceAsStream("import.js"));
			engine.eval("");

			debugMsg("Imported JavaScript Standard Library (JavaScript-based)", 4);

		} catch (Exception e1) {
			e1.printStackTrace();
			Utilities.showError("Error initializing JavaScript engine, please make sure you have Java 6+\n\n"
					+ "If you do, please report this error:\n"+e1.getMessage());
			System.exit(1);
		}
		
		//Begin loading the XML file(s)
		debugMsg("Loading "+filePath+"", 2);
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
				debugMsg("Parsing Body Element", 4);
				//Get body Element
				Element bodyElement = (Element) bodyNodeList.item(0);
					
				//If the bodyelement has the attribute "theme"
				if (bodyElement.getAttributeNode("theme") != null) {
					
					//Get the value of the attribute theme for the body element
					Attr swing_Theme = bodyElement.getAttributeNode("theme");
					
					//See if the lcm file specifies a theme other than camo
					if (swing_Theme != null && !swing_Theme.getNodeValue().equalsIgnoreCase("camo")) {
						try {
							//Split the theme into the jarfile and the classname (JARFILE.jar : com.stuff.stuff.theme)
							String[] splitTheme = swing_Theme.getNodeValue().split(":");
							
							//Attempt to dynamically load the specified jarfile
							sys.addJarToClasspath(splitTheme[0].trim());
							
							//Attempt to set the look'n'feel to the theme specified by the file
							UIManager.setLookAndFeel(splitTheme[1].trim());
							
							debugMsg("Look and Feel set to '"+swing_Theme.getNodeValue()+"'.", 3);

						} catch (Exception e) {
							//If unable to set to .lcm's theme, use the system look'n'feel
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							Utilities.showError("Error loading Look and Feel Specified, Look and Feel set to System");

						}
					 } else {
						//If swing_Theme == camo or is not set, use the system look'n'feel
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						debugMsg("Look and Feel (Swing) set to System", 3);

					 }
					
					
					//Create a new GUI instance and initialize it.
					GUI_Swing GUI = new GUI_Swing();
					GUI.loadGUI(filePath, engine, parent, false);
					
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

	public boolean loadJS(String filePath, ScriptEngine engine) {
		Utilities.debugMsg("Loading JavaScript from <script> tags.");
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

		return true;
		//XXX: END JAVASCRIPT HANDLING :XXX\\
		} catch (Exception e) {
			e.printStackTrace();
			Utilities.showError("Error loading Javascripts from '"+filePath+"'. Please check their validity.");
			return false;
		}
	}
	
	private void JSIterator(NodeList scriptNodes, ScriptEngine engine) {
		for (int i=0; i < scriptNodes.getLength(); i++) {
			//Get the specific <script> tag for this loop
			Node scriptElement = scriptNodes.item(i);
			
			//Load code in <script> tags
			if (scriptElement.getNodeName().equals("script")) {
				debugMsg("Running Script tag: "+(i+1));
				//Run all the code inside the <script> tags
				try {
					engine.eval(scriptElement.getTextContent());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	

}
