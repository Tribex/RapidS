/** RAPIDS JAVASCRIPT STANDARD LIBRARY (JAVASCRIPT SECTION)
 *
 * @file Defines global functions for importing Javascript files and Java packages/classes
 *
 * functions:
 *	-require(CLASS OR PACKAGE, NAME TO ASSIGN THE CLASS OR PACKAGE TO)
 *
 * @author Tribex
 */


/**
 * A top level wrapper for importing JavaScript files, Java classes, and Java Packages
 * 			Allows "importing" Java packages by creating a variable in the global
 * 			 namespace with the last section of the class/package qualifier.
 *
 *  @param classpkg A Class, Package or File path in the Rhino syntax: Packages.com.example.stuff.substuff
 *  @param qualifier {string} The optional name of the variable to be assigned to the global namespace.
 */
function require(classpkg, qualifier) {
    if (typeof(classpkg) == "string") {
        Packages.us.derfers.tribex.rapids.jvStdLib.Sys.importJS(classpkg);
    } else {
        require_Java(classpkg, qualifier);
    }
}

/**
 * Loads Java Classes or Packages and assigns them to top-level variables for ease-of-access.
 * @param classpkg The Class or Package to import
 * @param qualifier {string} The optional variable for the class or package to be assigned to.
 */
function require_Java(classpkg, qualifier) {
    //If the user didn't specify a qualifier, generate one
    if (String(qualifier) != "undefined" || String(qualifier) != "null" || String(qualifier) != "") {
        //Split the package name by .s, rendering '[JavaClass, Packages, com, example, stuff, substuff]'.
        var splitname = String(classpkg).split(".");
        //Assign the qualifier to substuff] from splitname after removing the ] character.
        qualifier = String(splitname[splitname.length-1].replace("]", ""));
    }

    //Add the qualifier to the global namespace with a value of classpkg.
    //This fakes an import by creating a shortcut to the fully-qualified name.
    this[qualifier] = classpkg;
}
