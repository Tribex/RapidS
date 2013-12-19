package us.derfers.tribex.rapids;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
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
}
