package us.derfers.tribex.rapids;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Utilities {
	
	
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
	
	
	//Always last method, for organization
	public static void debugMsg(Object msg) { //Method for printing debug messages in the format: [RapidS] MESSAGE - DATE
		debugMsg(msg, 2);
	}
	
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
	
	/* listFilesInJar(String folder)
	 * 	Returns a String[] with the names of all files in @folder.
	 * 
	 * @folder - The folder to list files in.
	 */
	public static ArrayList<String> listFilesInJar(String folder) {
		Enumeration<URL> en;
		try {
			en = Main.class.getClassLoader().getResources(folder);
		
		ArrayList<String> filenames = new ArrayList<String>();
		if (en.hasMoreElements()) {
		    URL metaInf=en.nextElement();
		    File fileMetaInf=new File(metaInf.toURI());

		    File[] files=fileMetaInf.listFiles();
		    //or 
		    for (String item : fileMetaInf.list()) {
		    	filenames.add(item);
		    }
		} 
		//rootFolder here represents a File Object pointing the root forlder of your search 

		return filenames;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
