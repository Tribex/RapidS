package us.derfers.tribex.rapids;
import static us.derfers.tribex.rapids.Utilities.debugMsg;

import java.io.File;
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

import us.derfers.tribex.rapids.GUI.Swing.GUI;
import us.derfers.tribex.rapids.jvStdLib.Sys;

public class Loader {
	//Javascript engine initialization
	/** The initial JavaScript engine */
	public ScriptEngine engine = new ScriptEngine();

	/**
	 * Where widget variables are stored.  Format: WIDGETID {WIDGETID {WIDGET}, class {CLASSNAME}, And so on for the rest of the parameters}
	 */
	public Map<String, Map<String, Object>> XMLWidgets = new HashMap<String, Map<String, Object>>();

	/** Counts Taken ID's for ID-less widgets */
	public Integer XMLWidgets__NO__ID = 0;

	/**
	 * The startup method. Starts the JavaScript engine and runs loadAll.
	 * @param filePath The file to load initially.
	 */
	public void startLoader(String filePath) {
		//JavaScript Engine Initialization
		//---------------------------------------------------------------------------//
		//Start Engine:
		debugMsg("JavaScript Engine Started", 4);

		//Import standard functions:
		try {
			//Import 
			engine.eval("importPackage(Packages.us.derfers.tribex.rapids.jvStdLib);");

			debugMsg("Imported JavaScript Standard Library (Java-based)", 3);

			//Loop through the JavaScript standard library for JavaScript and import all .js files.
			for (String toImport : Utilities.listFilesInJar("jsStdLib")) {
				engine.eval(new InputStreamReader(Main.class.getResourceAsStream("/jsStdLib/"+toImport)));
				debugMsg("Imported JavaScript File: "+toImport, 4);
			}
			debugMsg("Imported JavaScript Standard Library (JavaScript-based)", 3);

		} catch (Exception e1) {
			e1.printStackTrace();
			Utilities.showError("Error initializing JavaScript engine, please make sure you have Java 6+\n\n"
					+ "If you do, please report this error:\n"+e1.getMessage());
			System.exit(1);
		}

		//Begin loading the XML file(s)
		debugMsg("Loading "+filePath+"", 2);
		loadAll(filePath, null, engine);
	}

	/**
	 * Starts loading the GUI. Sets Swing look and feel, then loads the GUI using the GUI_Swing object.
	 * @param filePath The path of the .rsm file to load UI elements from.
	 * @param parent The (optional) parent Object, Eg, a JFrame or JPanel.
	 * @param engine The JavaScript engine to pass to GUI_Swing
	 */
	public void loadAll(String filePath, final Object parent, ScriptEngine engine) {

		//Attempt to load .rsm file filePath
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
							Sys.addJarToClasspath(splitTheme[0].trim());

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
					GUI GUI = new GUI();
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

	/**
	 * Discovers any script tags in the document and sends them to JSIterator to be parsed.
	 * @param filePath The file containing script(s) to run.
	 * @param engine the JavaScript engine to load the script tags into.
	 * @return Boolean telling whether or not it completed
	 */
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

	/**
	 * Iterates through a nodelist of script tags and parses them into the JavaScript engine.
	 * @param scriptNodes A NodeList of all script tags.
	 * @param engine The engine to run the scripts in.
	 */
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
