package us.derfers.tribex.rapids;

import java.io.File;

import us.derfers.tribex.rapids.Loader;
public class Main {
	//DEBUG_LEVEL - Turn on only for debugging
	public static Integer DEBUG_LEVEL = 1;
	
	//All scripts need access to the loader, and its child variables
	public static Loader loader = new Loader();
	
	//Main function
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
				
			} else if (arg.contains("-l") || arg.contains("--load-from-cwd")) {
				useDynamic = true;
				
			}
		}
		
		//Run the file. Second loop to prevent the file from being loaded before all arguments are parsed.
		for (String arg : args) {
			if (!arg.startsWith("-")){
				if (useDynamic == true) {
					runLoader(arg, true);
				} else {
					runLoader(Utilities.getJarDirectory()+arg, false);
				}
				hasLoaded = true;
			}
		}
		
		//If an argument for a file was not specified, attempt to load init.rsm
		if (hasLoaded == false) {
			//If the user is running RapidS in the directory for a reason,
			if (DEBUG_LEVEL == 5 || DEBUG_LEVEL == 0) {
				//Load from the java CWD instead of the Jar Location
				runLoader("init.rsm", true);
			
			//Load from package directory if in debug mode
			} else {
				runLoader(Utilities.getJarDirectory()+"init.rsm", false);
			}
		}
		
	}
	
	private static void runLoader(String fileName, Boolean dynamic) {
		if (dynamic == true) {
			Globals.selCWD = new File(fileName).getParent();
		} else {
			Globals.selCWD = Utilities.getJarDirectory();
		}
		System.out.println(Globals.selCWD);
		loader.loadAll(fileName, false);
	}

}
