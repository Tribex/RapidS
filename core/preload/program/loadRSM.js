/**
 * @file Adds the ability to load a rsm or CSS file via JavaScript.
 * @author Tribex
 **/

require(Packages.us.derfers.tribex.rapids.Main);
require(Packages.us.derfers.tribex.rapids.Utilities);
require(Packages.us.derfers.tribex.rapids.Globals);

require(Packages.java.net.URL);
require(Packages.org.apache.commons.io.FileUtils);
require(Packages.org.apache.commons.io.IOUtils);

/**
 * Load an rsm file from a URL or file, with the option to cache or fallback if the file or URL is not found.
 * @param fileLocation {string} the location or URL of the file to be loaded.
 * @param cache {string} where to cache the file (optional)
 * @param fallback {string} load this file instead of the URL if it is not found (optional)
 */
program.loadRSM = function(fileLocation, cache, fallback) {
    //If the file is a URL on the internet:
    if (fileLocation.contains("://")) {
        try {
            //Load the file from the internet.
            Main.loader.loadAll(Utilities.EscapeScriptTags(IOUtils.toString(new URL(fileLocation))));

            if (cache != null) {
                try {
                    FileUtils.writeStringToFile(new File(Globals.getCWD(cache)), IOUtils.toString(new URL(fileLocation)));
                } catch (e2) {
                    program.showError("Error: Unable to cache to file "+cache);
                }
            }
        } catch (e) {
            if (fallback != null) {
                try {
                    Main.loader.loadAll(Utilities.EscapeScriptTags(FileUtils.readFileToString(new File(Globals.getCWD(fallback)))));
                } catch (e2) {
                    program.showError("Error: Invalid file in fallback tag pointing to "+fallback);
                }
            } else {
                program.showError("Error: Unable to load file: "+fileLocation+" and no fallback file found.");
            }
        }

        //Load the file from the Hard Drive
    } else {
        try {
            Main.loader.loadAll(Utilities.EscapeScriptTags(FileUtils.readFileToString(new File(Globals.getCWD(fileLocation)))));
        } catch (e) {
            program.showError("Error: Unable to find file "+fallback);
        }
    }
}


