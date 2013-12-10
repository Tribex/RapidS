package us.derfers.tribex.rapids;

import us.derfers.tribex.rapids.Loader;

public class Main {
	//DEBUG_LEVEL - Turn on only for debugging
	public static Integer DEBUG_LEVEL = 4;
	
	//All scripts need access to the loader, and its child variables
	public static Loader loader = new Loader();
	
	//Main function
	public static void main(String[] args) {
		
		//Iterate through the arguments and check for debug level
		for (String arg : args) {
			if (arg.contains("-d")) {
				DEBUG_LEVEL = Integer.valueOf(arg.split("-d")[1]);
			}
		}
		//Load .lcm files
		if (args.length > 0) {
			//From args
			loader.loadAll(args[0], false);
		} else {
			//Load from jar location if not in debug mode
			if (DEBUG_LEVEL > 1) {
				loader.loadAll("init.rsm", false);
			
			//Load from package directory if in debug mode
			} else {
				loader.loadAll(Utilities.getJarDirectory()+"init.rsm", false);
			}
		}
		
	}

}
