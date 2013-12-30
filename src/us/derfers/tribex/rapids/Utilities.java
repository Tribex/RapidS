package us.derfers.tribex.rapids;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Provides static functions for displaying an error dialog or printing a debug message
 * @author Nateowami, TribeX
 */
public class Utilities {
	
	/**
	 * Shows an error message dialog to the user. Uses Swing
	 * @param errMsg The error message to display
	 */
	public static void showError(String errMsg) { //Shows a swing dialog box with an error message
		//Log to command line
		debugMsg(errMsg);
		
		//Show a cross-platform error message.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			debugMsg("Unable to mimic System Look and feel, falling back to Ocean");
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, errMsg);
	}
	
	/**
	 * Finds the directory the JAR is running in
	 * @return The URL of the current directory
	 */
	public static String getJarDirectory() {
	    String absolutePath = null;
		try {
			absolutePath = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return absolutePath;
	}
	
	/**
	 * Returns a String[] with the names of all files in the folder.
	 * @param folder - The folder to list files in.
	 * @return a String[] with the names of all files in the specified folder.
	 */
	public static ArrayList<String> listFilesInJar(String folder) {
		Enumeration<URL> en;
		try {
			en = Main.class.getClassLoader().getResources(folder);
		
		ArrayList<String> filenames = new ArrayList<String>();
		if (en.hasMoreElements()) {
		    URL metaInf=en.nextElement();
		    File fileMetaInf=new File(metaInf.toURI());
		    
		    //List the files
		    String[] sortedFiles = fileMetaInf.list();
		    
		    //Sort the files
		    Arrays.sort(sortedFiles);
		    
		    //Iterate through the files.
		    for (String item : sortedFiles) {
		    	filenames.add(item);
		    }
		} 

		return filenames;
		
		} catch (Exception e) {
			showError("Error getting file list (inside jar) from "+folder+"\n\n"
					+ "Does "+folder+" exist inside the jarfile?");
			
			e.printStackTrace();
			return null;
		}
	}
	
	//Always last method, for organisation
	/**
	 * Prints a debug message at debug if the current debug level is 2 or greater.
	 * The format is [RapidS] DEBUG MESSAGE - DATE/TIME  DEBUG LEVEL.
	 * @param msg The debug message to print
	 */
	public static void debugMsg(Object msg) { //Method for printing debug messages in the format: [RapidS] MESSAGE - DATE
		debugMsg(msg, 2);
	}
	
	/**
	 * Prints a debug message in the form of [RapidS] DEBUG MESSAGE - DATE/TIME  DEBUG LEVEL 
	 * only if lvl is less than or equal to the current debug level the program is running at. 
	 * @param msg The debug message to print
	 * @param lvl The debug level for this message
	 */
	public static void debugMsg(Object msg, int lvl) { //Method for printing debug messages in the format: [RapidS] MESSAGE - LEVEL - DATE
		
		//Format the current date to hour:minute:second-millisecond
		SimpleDateFormat debugDateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
		Date currDate = new Date();
		String debugDate = debugDateFormat.format(currDate);
		//Make sure the debug level is greater than one (verbose)
		if (Main.DEBUG_LEVEL >= lvl && Main.DEBUG_LEVEL != 0) {
			
			//Print the message
			System.out.println("[RapidS] "+(String) msg+" - "+debugDate+" ["+lvl+"]");
			
		}
	}
	
	/**
	 * Parse a string into an XML document.
	 * @param string The string to parse
	 * @return An XML DOM document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document XMLStringToDocument(String string) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		//Parse filePath
		Document doc = db.parse(new InputSource(new StringReader(string)));

		//Stabilize parsed document
		doc.normalize();
		return doc;
	}
	
	/**
	 * Escapes all script tags in the file 'filePath'.
	 * @param filePath The file to escape from.
	 * @return A string containing the escaped text.
	 */
	public static String EscapeScriptTags(String filePath) {
		String escaped = null;
		
		//Put all the escapable items in the map
		HashMap<String, String> escapable = new HashMap<String, String>();
		escapable.put("&&", "&amp;&amp;");
		escapable.put("& ", "&amp; ");
		escapable.put("<", "&lt;");
		escapable.put(">", "&gt;");
		
		//Attempt to escape the file
		try {
			escaped = FileUtils.readFileToString(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			Utilities.debugMsg("ERROR: File not found for script escaping: "+filePath);
		}

		Pattern pat = Pattern.compile("<script .+?>(.+?)</script>", Pattern.DOTALL);
        Matcher m = pat.matcher(escaped);
        while (m.find()) {
        	String temp = m.group(1);
        	for (int i = 0; i < escapable.size(); i++) {
        		temp = temp.replaceAll((String) escapable.keySet().toArray()[i], (String) escapable.values().toArray()[i]);
        	}
        	escaped = escaped.replace(m.group(0), m.group(0).replace(m.group(1), temp));

        }
		return escaped;
	}
}
