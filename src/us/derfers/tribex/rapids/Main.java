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

import java.io.File;

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
     * The argument list. Passed to RapidS programs.
     */
    public static String[] programArguments;

    /**
     * The main method for RapidS. Program execution starts here
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        programArguments = args;
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
                //Create a file to test its location
                File file = null;
                if (!arg.startsWith(":\\", 1) && !arg.startsWith("/")) {
                    file = new File(Utilities.getJarDirectory()+arg);
                } else {
                    file = new File(arg);
                }

                //Make sure the file exists and is a directory
                if (file.exists() && !file.isDirectory()) {
                    runLoader(file.toString(), file.getParent());
                } else {
                    Utilities.showError("Error: File "+file.getAbsolutePath()+" does not exist or is a directory.");
                    System.exit(0);
                }
                hasLoaded = true;
            }
        }

        //If an argument for a file was not specified, attempt to load init.rsm
        if (hasLoaded == false) {
            //If the user is running RapidS in the directory for a reason,
            if (DEBUG_LEVEL == 5) {
                //Load from the java CWD instead of the Jar Location
                runLoader("init.rsm", "./");

                //Load from package directory if in debug mode
            } else {
                runLoader(Utilities.getJarDirectory()+"init.rsm", Utilities.getJarDirectory());
            }
        }

    }

    /**
     * Runs the loadAll method of loader and sets the selected Current working directory.
     * @param fileName The name of the file to be run, normally init.rsm
     */
    private static void runLoader(String fileName, String parentDirectory) {
        Globals.CWD = parentDirectory;
        loader.startLoader(fileName);
    }

}
