package us.derfers.tribex.rapids;

import us.derfers.tribex.rapids.Loader;

public class Main {
	//MODE - Turn on only for debugging
	public static final Integer DEBUG_LEVEL = 2;
	
	//All scripts need access to the loader, and its child variables
	public static Loader loader = new Loader();
	
	//Main function
	public static void main(String[] args) {

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
