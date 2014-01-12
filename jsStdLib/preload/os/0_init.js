/* Creates the global os object for other files to use. */

require(Packages.java.lang.System);

var os = {};

//Get the Operating System name.
os.name = System.getProperty("os.name");

//Get the Operating System architecture (32-bit or 64-bit).
os.arch = System.getProperty("os.arch");

//Get the Operating System version.
os.version = System.getProperty("os.version");