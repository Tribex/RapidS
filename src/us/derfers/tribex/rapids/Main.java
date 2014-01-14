package us.derfers.tribex.rapids;

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

        //Iterate through the arguments and perform the appropriate actions
        for (String arg : args) {
            //If the debug level is specified
            if (arg.contains("-d") || arg.contains("--debug-level")) {
                DEBUG_LEVEL = Integer.valueOf(arg.split(":")[1]);
                Utilities.debugMsg("Debug Level: "+DEBUG_LEVEL);

                //If the program is started with a .rsm file to parse other than init.rsm.
            } else if (!arg.startsWith("-")){
                runLoader(Utilities.getJarDirectory()+arg);
                hasLoaded = true;
            }
        }

        //If an argument for a file was not specified, attempt to load init.rsm
        if (hasLoaded == false) {
            //If the user is running RapidS in the directory for a reason,
            if (DEBUG_LEVEL == 5) {
                //Load from the java CWD instead of the Jar Location
                runLoader("init.rsm");

                //Load from package directory if in debug mode
            } else {
                runLoader(Utilities.getJarDirectory()+"init.rsm");
            }
        }

    }

    /**
     * Runs the loadAll method of loader and sets the selected Current working directory.
     * @param fileName The name of the file to be run, normally init.rsm
     */
    private static void runLoader(String fileName) {
        Globals.selCWD = Utilities.getJarDirectory();
        loader.startLoader(fileName);
    }

}
