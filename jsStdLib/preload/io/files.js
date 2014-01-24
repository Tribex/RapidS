/**
 * @file Provides basic file reading, writing, polling, moving, and copying features.
 * @author Nateowami, Tribex
 */

//Import packages
require(Packages.java.io.File);
require(Packages.java.util.Scanner);
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.Utilities);
require(Packages.org.apache.commons.io.FileUtils);
require(Packages.java.awt.FileDialog);

/** Files object TODO: Support multiple encodings.
 * @namespace
 */
function files(filename, flag) {
    // The file being operated on
    var filename = filename;

    if (flag === null || flag === undefined) {
        flag = "rw";
    }

    // If the file operation specified is "read", "read-write", or
    // "read-write-append" (r, rw, rw+)
    if (flag.contains("r")) {
        try {
            var scanner = new Scanner(new File(filename));
        } catch (e) {
            Utilities.showError("Error reading file: " + filename
                    + ". Does it exist?\n\n" + e.message);
        }

        /**
         * Read the content of the object's file.
         * @reutrns {string} The contents of the file.
         */
        this.read = function() {
            return FileUtils.readFileToString(new File(filename));
        }

        /**
         * Read the content of the next line of this object's file.
         * @reutrns {string} The contents of the line.
         */
        this.readNextLine = function() {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                // Return a blank string to prevent errors
                return "";
            }
        }

        /**
         * Read the content of the next word of this object's file.
         * @reutrns {string} The next word.
         */
        this.readNext = function() {
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return "";
            }
        }

        /**
         * Determines whether or not there is another word in filename
         * @reuturs {boolean} True if there is another word, false if not.
         */
        this.hasNext = function() {
            return scanner.hasNext();
        }

        /**
         * Determines whether or not there is another line in filename
         * @returns {boolean} True if there is another line, false if not.
         */
        this.hasNextLine = function() {
            return scanner.hasNextLine();
        }
    }

    // If the file operation is "write", "write-append", "read-write", or
    // "read-write-append" (w, w+, rw, rw+)
    if (flag.contains("w")) {

        var append = false;

        // If the flag contains +, set append to true, causing any write
        // operations to preserve existing data
        if (flag.contains("+")) {
            append = true;
        }

        /**
         * Write data to filename.
         * @param data {string} The data to write. Must be a string.
         */
        this.write = function(data) {
            try {
                FileUtils.writeStringToFile(new File(filename), data, append);
            } catch (e) {
                Utilities.showError("Error writing to file: " + filename
                        + " - " + e.message);
            }
        }

        /**
         * Append a line to filename.
         * @param data {string} The line to write. Must be a string.
         */
        this.writeLine = function(data) {
            try {
                FileUtils.write(new File(filename), data
                        + "\n", true);
            } catch (e) {
                Utilities.showError("Error writing line to file: " + filename
                        + " - " + e.message);
            }
        }
    }
}

/**
 * Opens the file at location filename with a mode set by flag.
 * @param filename {string} The location of the file to open.
 * @param flag {string} The mode of the file: Must be 'r', 'rw', 'w', 'w+', or 'rw+'
 */
files.open = function(filename, flag) {
    return new files(Globals.getCWD(filename), flag);
}

/**
 * Copy a file from one place to another
 * @param sourceFileName {string} The location of the source file.
 * @param destFileName {string} The location of the destination file.
 * @returns {boolean} True on success, false on failure.
 */
files.copy = function(sourceFileName, destFileName) {
    // Set up Files
    var sourcefile = new File(Globals.getCWD(sourceFileName));
    var destfile = new File(Globals.getCWD(destFileName));

    // If the source exists (of course the destination doesn't)
    if (sourcefile.exists()) {

        // If the source is a directory, copy it as a directory
        if (sourcefile.isDirectory()) {
            FileUtils.copyDirectory(sourcefile, destfile);
            return true;

            // If the source is a file, or unknown type, attempt to copy it as a
            // file
        } else {
            FileUtils.copyFile(sourcefile, destfile);
            return true;
        }

    } else {
        Utilities.showError("File: " + sourceFileName + " does not exist!");
        return false;
    }
}


/**
 * Move a file from one place to another
 * @param sourceFileName {string} The location of the source file.
 * @param destFileName {string} The location of the destination file.
 * @returns {boolean} True on success, false on failure.
 */
files.move = function(sourceFileName, destFileName) {
    // Set up Files
    var sourcefile = new File(Globals.getCWD(sourceFileName));
    var destfile = new File(Globals.getCWD(destFileName));

    // If the source exists (of course the destination doesn't)
    if (sourcefile.exists()) {

        // If the source is a directory, move it as a directory
        if (sourcefile.isDirectory()) {
            FileUtils.moveDirectory(sourcefile, destfile);
            return true;

            // If the source is a file, or unknown type, attempt to move it as a
            // file
        } else {
            FileUtils.moveFile(sourcefile, destfile);
            return true;
        }

    } else {
        Utilities.showError("File: " + sourceFileName + " does not exist!");
        return false;
    }
}
/**
 * Alias of files.move.
 */
files.cut = files.move;

/**
 * Make a directory.
 * @param dirname {string} The string name of the directory
 * @param recursive {string}
 *            Whether or not to create multiple nested directories as indicated
 *            by dirname
 * @returns {boolean} True on success, false on failure.
 */
files.mkdir = function(dirname, recursive) {
    recursive = typeof recursive !== 'undefined' ? recursive : false;

    dir = new File(Globals.getCWD(dirname));
    if (recursive) {
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    } else {
        if (dir.mkdir()) {
            return true;
        } else {
            return false;
        }
    }
}

/**
 * Returns the size of a file in bytes
 * @param filename {string} The location of the file to check the size on.
 * @returns Integer of the size on success, false on failure
 */
files.size = function(filename) {
    file = new File(filename);
    try {
        return FileUtils.sizeOf(file);
    } catch (e) {
        Utilities.showError("File: " + filename + " does not exist.");
        return false;
    }
}

/**
 * Get an array of all the files in dirname, optionally search dirname recursively. DOES NOT SHOW DIRECTORIES
 * @param dirname {string} The directory to search.
 * @param recursive {boolean} Set to true to search all subdirectories of dirname.
 * @param extensions Either a string or an array of extensions. If this is set, the search will only look for files with these extensions.
 * @returns {array} An array of file paths in dirname.
 */
files.find = function(dirname, recursive, extensions) {
    recursive = typeof recursive !== 'undefined' ? recursive : false;
    extensions = typeof extensions !== 'undefined' ? extensions : null;

    return FileUtils.listFiles(new File(Globals.getCWD(dirname)),
            extensions, recursive);
}

/**
 * Determine whether or not a file exists.
 * @param filename {string} the location of the file to check.
 * @returns {boolean} True if the file exists, false if it doesn't.
 */
files.exists = function(filename) {
    if (new File(Globals.getCWD(filename)).exists()) {
        return true;
    } else {
        return false;
    }
}

/** The full path to the system temporary directory */
files.tempdir = FileUtils.getTempDirectoryPath();

/**
 * A string representing the location of the user's home directory. MAY
 * MISBEHAVE ON WINDOWS VISTA+
 */
files.userdir = FileUtils.getUserDirectoryPath();
