package us.derfers.tribex.rapids;

import java.io.File;

import us.derfers.tribex.rapids.Loader;

/**
 * Main class. Parses arguments, delegates loading of files, debug handling, and widget creation.
 * @author TribeX, Nateowami
 */
public class Main {
	/**
	 * Global Debug Level, used to control the amount of data printed during logging.
	 */
	public static Integer DEBUG_LEVEL = 1;
	
	/**
	 * Global Loader.  All classes need access to it.
	 */
	public static Loader loader = new Loader();
	
	/**
	 * The main method for RapidS. Program execution starts here
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		
		//Value to facilitate automatic loading of init.rsm
		Boolean hasLoaded  = false;
		
		//If false, load files from the directory where the Jarfile is located.
		//If true, load files from whatever the Java working directory is.
		Boolean useDynamic = false;
		
		//Iterate through the arguments and perform the appropriate actions
		for (String arg : args) {
			//If the debug level is specified
			if (arg.contains("-d") || arg.contains("--debug-level")) {
				DEBUG_LEVEL = Integer.valueOf(arg.split(":")[1]);
				Utilities.debugMsg("Debug Level: "+DEBUG_LEVEL);
			
			//If the dynamic flag is set, look in the Java CWD instead of the RapidS Jar Parent folder
			} else if (arg.contains("-l") || arg.contains("--load-from-cwd")) {
				useDynamic = true;
				
			}
		}
		
		//Run the file. Second loop to prevent the file from being loaded before all arguments are parsed.
		for (String arg : args) {
			//If the argument is the file
			if (!arg.startsWith("-")){
				
				//If useDynamic is true,
				if (useDynamic == true) {
					//Tell the loader to set the working directory to the Java CWD
					runLoader(arg, true);
					
				
				} else {
					//Tell the loader to set the working directory to the directory of the RapidS jarfile (DEFAULT)
					runLoader(Utilities.getJarDirectory()+arg, false);
				}
				
				//Do not attempt to load init.rsm later on
				hasLoaded = true;
			}
		}
		
		//If an argument for a file was not specified, attempt to load init.rsm
		if (hasLoaded == false) {
			//If the user is running RapidS in the directory for a reason,
			if (DEBUG_LEVEL == 5 || DEBUG_LEVEL == 0) {
				//Load from the java CWD instead of the Jar Location
				runLoader("init.rsm", false);
			
			//Load from package directory if in debug mode
			} else {
				runLoader(Utilities.getJarDirectory()+"init.rsm", false);
			}
		}
		
	}
	
	/**
	 * Runs the loadAll method of loader and sets the selected Current working directory.
	 * @param fileName The name of the file to be run, normally init.rsm
	 * @param dynamic Whether or not to use the Java Current Working Directory instead of the location of the Jarfile as the Current Working Directory.
	 */
	private static void runLoader(String fileName, Boolean dynamic) {
		if (dynamic == true) {
			Globals.selCWD = new File(".").getAbsolutePath().replaceAll(".", "");
		} else {
			Globals.selCWD = Utilities.getJarDirectory();
		}
		loader.startLoader(fileName);
	}

}