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


package us.derfers.tribex.rapids;
import static us.derfers.tribex.rapids.Utilities.debugMsg;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.GUI.Swing.GUI;
import us.derfers.tribex.rapids.jvStdLib.Sys;

/**
 * Starts the JavaScript engine and begins loading XML for widgets and layout information.
 *
 * @author TribeX, Nateowami
 *
 */
public class Loader {
    //Javascript engine initialization
    /** The initial JavaScript engine */
    public ScriptEngine engine = new ScriptEngine();

    public GUI GUI = new GUI();


    /**
     * Where widget variables are stored.  Format: WIDGETID {WIDGETID {WIDGET}, class {CLASSNAME}, And so on for the rest of the parameters}
     */
    public Map<String, Map<String, Object>> XMLObjects = new HashMap<String, Map<String, Object>>();

    /** Counts Taken ID's for ID-less widgets */
    public Integer XMLObjects__NO__ID = 0;

    /**
     * The startup method. Starts the JavaScript engine and runs loadAll.
     * @param filePath The file to load initially.
     */
    public void startLoader(String filePath) {
        String fileEscaped = Utilities.EscapeScriptTags(filePath);
        //JavaScript Engine Initialization
        //---------------------------------------------------------------------------//
        //Start Engine:
        debugMsg("JavaScript Engine Started", 4);

        //Import standard functions. Runs before the file loads
        try {
            //Import the standard JavaScript library (Java section)
            engine.eval("importPackage(Packages.us.derfers.tribex.rapids.jvStdLib);");

            debugMsg("Imported JavaScript Standard Library (Java-based)", 3);

        } catch (Exception e1) {
            e1.printStackTrace();
            Utilities.showError("Error initializing JavaScript engine, please make sure you have Java 6+\n\n"
                    + "If you do, please report this error:\n"+e1.getMessage());
            System.exit(1);
        }


        //XXX: Loader section :XXX\\
        //JAVASCRIPT INITIALIZATION:
        //Run JavaScript that must be run before preload. Mainly provides require() and similar routines.
        recursiveLoadJS(engine, "jsStdLib/init");
        debugMsg("Imported JavaScript Initialization Library (Init)", 4);

        //PRELOAD:
        //Loop through the JavaScript standard library for JavaScript and import all .js files in the preload folder.
        recursiveLoadJS(engine, "jsStdLib/preload");
        debugMsg("Imported JavaScript Standard Library (PreLoad)", 3);

        //LOAD:
        //Begin loading the XML file(s)
        debugMsg("Loading "+filePath+"", 2);
        loadAll(fileEscaped, engine);

        //POSTLOAD:
        //Loop through the JavaScript standard library for JavaScript and import all .js files in the postload folder.
        recursiveLoadJS(engine, "jsStdLib/postload");
        debugMsg("Imported JavaScript Standard Library (PostLoad)", 3);

        //XXX: End loader :XXX\\
    }

    /**
     * Starts loading the GUI. Sets Swing look and feel, then loads the GUI using the GUI_Swing object.
     * @param escapedFile The content .rsm file to load UI elements from.
     * @param parent The (optional) parent Object, Eg, a JFrame or JPanel.
     * @param engine The JavaScript engine to pass to GUI_Swing
     */
    public void loadAll(String escapedFile, ScriptEngine engine) {

        //Attempt to load .rsm file filePath
        try {

            //Parse filePath
            Document doc = Utilities.XMLStringToDocument(escapedFile);

            //Stabilize parsed document
            doc.normalize();

            //Get body element
            NodeList mainNodeList = doc.getElementsByTagName("rsm");

            //Make sure there is only ONE body element
            if (mainNodeList.getLength() == 1) {
                debugMsg("Parsing Main Element", 4);
                //Get body Element
                Element mainElement = (Element) mainNodeList.item(0);

                //If the bodyelement has the attribute "theme"
                if (mainElement.getAttributeNode("theme") != null) {

                    //Get the value of the attribute theme for the body element
                    Attr swing_Theme = mainElement.getAttributeNode("theme");

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
                            try {
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            } catch (Exception a) {
                                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            }
                            Utilities.showError("Error loading Look and Feel Specified, Look and Feel set to System");

                        }
                    } else {
                        //If swing_Theme == camo or is not set, use the system look'n'feel
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception a) {
                            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        }
                        debugMsg("Look and Feel (Swing) set to System", 3);

                    }


                    //Create a new GUI instance and initialize it.

                    for (int i = 0; i < mainElement.getElementsByTagName("window").getLength(); i++) {
                        GUI.loadWindow((Element) mainElement.getElementsByTagName("window").item(i), engine, false);
                    }

                    for (int i = 0; i < mainElement.getElementsByTagName("style").getLength(); i++) {
                        Element styleElement = (Element) mainElement.getElementsByTagName("style").item(i);
                        //Load all styles from the style tags
                        if (styleElement.getAttributeNode("href") != null) {
                            GUI.loadStyles(null, styleElement.getTextContent());
                        } else {
                            GUI.loadStyles(styleElement.getTextContent(), null);

                        }
                    }

                    Main.loader.loadJS(escapedFile, engine);

                }

            } else { //There was more than one body tag, or 0 body tags

                //Display Error and quit, as we cannot recover from an abnormally formatted file
                Utilities.showError("Error: More or less than one <rsm> tag in '"+escapedFile+"'.\n\n"
                        + "Please add ONE <rsm> tag to '"+escapedFile+"'.");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Discovers any script tags in the document and sends them to JSIterator to be parsed.
     * @param escapedFile The escaped file containing script(s) to run.
     * @param engine the JavaScript engine to load the script tags into.
     * @return Boolean telling whether or not it completed
     */
    public boolean loadJS(String escapedFile, ScriptEngine engine) {
        Utilities.debugMsg("Loading JavaScript from <script> tags.");
        //XXX: JAVASCRIPT HANDLING SECTION :XXX\\
        try {
            //Parse filePath
            Document doc = Utilities.XMLStringToDocument(escapedFile);

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
            Utilities.showError("Error loading Javascripts from '"+escapedFile+"'. Please check their validity.");
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
            Node scriptNode = scriptNodes.item(i);

            //Load code in <script> tags
            if (scriptNode.getNodeName().equals("script")) {
                Element scriptElement = (Element) scriptNode;
                if (scriptElement.getAttributeNode("src") != null) {
                    try {
                        engine.eval(new FileReader(new File(Globals.getCWD()+scriptElement.getAttributeNode("src").getNodeValue())));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    debugMsg("Loading Script tag: "+(i+1));
                    //Run all the code inside the <script> tags
                    try {
                        engine.eval(scriptNode.getTextContent());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * Loads all JavaScript files under 'folder' into 'engine'
     * @param engine The ScriptEngine to run all files in folder in.
     * @param folder The folder to load .js files from.
     */
    private static void recursiveLoadJS(ScriptEngine engine, String folder) {
        File dir = new File(Utilities.getJarDirectory()+"/"+folder);
        try {

            File [] fileList = dir.listFiles();

            Arrays.sort(fileList);

            for (File file : fileList) {
                if (file.isDirectory()) {
                    recursiveLoadJS(engine, folder+"/"+file.getName());
                } else if (file.toString().endsWith(".js")) {
                    engine.eval(new FileReader(new File(Utilities.getJarDirectory()+folder+"/"+file.getName())));
                    debugMsg("Imported JavaScript File: "+file.getName(), 4);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
