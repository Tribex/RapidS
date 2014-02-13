/**
 * @file Creates the global os object for other files to use.
 * @author Tribex
 */

require(Packages.java.lang.System);

/**
 * The Operating System reference object
 * @namespace
 */
var os = {};

/**
 * The Operating System name, eg: Linux, Windows 7, OSX (darwin?).
 */
os.name = System.getProperty("os.name");

/**
 * The Operating System architecture (32-bit or 64-bit).
 */
os.arch = System.getProperty("os.arch");

/**
 * The Operating System version.
 */
os.version = System.getProperty("os.version");
