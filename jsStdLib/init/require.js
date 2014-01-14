/* RAPIDS JAVASCRIPT STANDARD LIBRARY (JAVASCRIPT SECTION)
 *
 * import.js - Defines global functions for importing Javascript files and Java packages/classes
 *
 * functions:
 *	-require(CLASS OR PACKAGE, NAME TO ASSIGN THE CLASS OR PACKAGE TO)
 */


/*
 * require - A top level wrapper for importing JavaScript files, Java classes, and Java Packages
 * 			Allows "importing" Java packages by creating a variable in the global
 * 			 namespace with the last section of the class/package qualifier.
 *
 *  @classpkg - A Class, Package or File path in the Rhino syntax: Packages.com.example.stuff.substuff
 *  @qualifier - The optional name of the variable to be assigned to the global namespace.
 */
function require(classpkg, qualifier) {
    if (typeof(classpkg) == "string") {
        Packages.us.derfers.tribex.rapids.jvStdLib.Sys.importJS(classpkg);
    } else {
        require_Java(classpkg, qualifier);
    }
}

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
