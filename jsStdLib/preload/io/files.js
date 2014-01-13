/**
 * Provides basic file reading, writing, polling, moving, and copying features.
 * --USES APACHE COMMONS IO--
 */

//import packages
require(Packages.java.io.File);
require(Packages.java.util.Scanner);
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.Utilities);
require(Packages.org.apache.commons.io.FileUtils);
require(Packages.java.awt.FileDialog);

/** Files object TODO: Support multiple encodings. */
function files(filename, flag) {
    // The file being operated on
    var filename = filename;

    // If the file operation specified is "read", "read-write", or
    // "read-write-append" (r, rw, rw+)
    if (stringContains(flag, "r")) {
        try {
            var scanner = new Scanner(new File(Globals.getCWD() + filename));
        } catch (e) {
            Utilities.showError("Error reading file: " + filename
                    + ". Does it exist?\n\n" + e.message);
        }

        // Returns a string containing all the contents of filename
        this.read = function() {
            return FileUtils.readFileToString(new File(Globals.getCWD()
                    + filename));
        }

        // Reads the next line from filename
        this.readNextLine = function() {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                // Return a blank string to prevent errors
                return "";
            }
        }

        // Reads the next work from filename
        this.readNext = function() {
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return "";
            }
        }

        // Boolean. Determines whether or not there is another word in filename
        this.hasNext = function() {
            return scanner.hasNext();
        }

        // Boolean. Determines whether or not there is another line in filename
        this.hasNextLine = function() {
            return scanner.hasNextLine();
        }
    }

    // If the file operation is "write", "write-append", "read-write", or
    // "read-write-append" (w, w+, rw, rw+)
    if (stringContains(flag, "w")) {

        var append = false;

        // If the flag contains +, set append to true, causing any write
        // operations to preserve existing data
        if (stringContains(flag, "+")) {
            append = true;
        }

        // Write 'data' to 'filename'.
        this.write = function(data) {
            try {
                FileUtils.writeStringToFile(new File(Globals.getCWD()
                        + filename), data, append);
            } catch (e) {
                Utilities.showError("Error writing to file: " + filename
                        + " - " + e.message);
            }
        }

        // Write 'data' to 'filename'. Always preserves existing data, and adds
        // a newline to the end of 'data'
        this.writeLine = function(data) {
            try {
                FileUtils.write(new File(Globals.getCWD() + filename), data
                        + "\n", true);
            } catch (e) {
                Utilities.showError("Error writing line to file: " + filename
                        + " - " + e.message);
            }
        }
    }
}

/* Static constructor that removes the need for a 'new' keyword */
files.open = function(filename, flag) {
    return new files(filename, flag);
}

/* Static function for copying one filename to another */
files.copy = function(sourceFileName, destFileName) {
    // Set up Files
    var sourcefile = new File(Globals.getCWD() + sourceFileName);
    var destfile = new File(Globals.getCWD() + destFileName);

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

/* Static function for moving a file from one place to another. (cut) */
files.move = function(sourceFileName, destFileName) {
    // Set up Files
    var sourcefile = new File(Globals.getCWD() + sourceFileName);
    var destfile = new File(Globals.getCWD() + destFileName);

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
//files.cut, alias of files.move
files.cut = files.move;

/**
 * Make a directory.
 *
 * @param dirname
 *            The string name of the directory
 * @param recursive
 *            Whether or not to create multiple nested directories as indicated
 *            by dirname
 */
files.mkdir = function(dirname, recursive) {
    recursive = typeof recursive !== 'undefined' ? recursive : false;

    dir = new File(Globals.getCWD() + dirname);
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

/* Returns the size of a file in bytes */
files.size = function(filename) {
    file = new File(filename);
    try {
        return FileUtils.sizeOf(file);
    } catch (e) {
        Utilities.showError("File: " + filename + " does not exist.");
        return false;
    }
}

/* List files inside dirname */
files.find = function(dirname, recursive, extensions) {
    recursive = typeof recursive !== 'undefined' ? recursive : false;
    extensions = typeof extensions !== 'undefined' ? extensions : null;

    return FileUtils.listFiles(new File(Globals.getCWD() + dirname),
            extensions, recursive);
}

/* Determines whether or not a file exists */
files.exists = function(filename) {
    if (new File(Globals.getCWD() + filename).exists()) {
        return true;
    } else {
        return false;
    }
}

/* Opens the system file load or save dialog. */
files.fileDialog = function(opts) {

    //Check to make sure that a proper mode is always set.
    if (opts.mode === null || opts.mode === undefined) {
        //Default to load
        opts.mode = "l";
    } else {
        switch (opts.mode) {
        case "load":
            //Reduce the mode to a letter, easier to use.
            opts.mode = "l";
            break;
        case "save":
            //Reduce the mode to a letter, easier to use.
            opts.mode = "s";
            break;
        case "l":
            break;
        case "s":
            break;
        default:
            //Again, default to load
            opts.mode = "l";
        }
    }

    //Set the title of the dialog if it doesn't exist
    if (opts.title == null || opts.title == undefined) {
        opts.title = "Open File";
    }
    //Create the dialog
    var fd = new FileDialog(new Packages.javax.swing.JFrame(), opts.title);
    //If the mode is 'load'
    if (opts.mode == "l") {
        //Set the dialog to load mode.
        fd.setMode(FileDialog.LOAD);
    } else if (opts.mode == "s") {
        //Set the dialog to save mode.
        fd.setMode(FileDialog.SAVE);
    } else {
        //Display an error if somehow, parameters are missing.
        Utilities.showError("Invalid mode flag in opening file dialog: "+opts.mode);
        return {'file' : null, 'directory' : null};
    }
    //Set to the CWD.
    fd.setDirectory(Globals.getCWD());

    //Set the directory if it exists
    if (opts.directory !== null && opts.directory !== undefined) {

        //If the user didn't specify the CWD.
        if (!opts.directory == ".") {
            fd.setDirectory(opts.directory);
        }

    }

    //Set the filename if it exists.
    if (opts.filename !== null && opts.filename !== undefined) {
        fd.setFile(opts.filename);
    }

    if (opts.multi == true || opts.multiple == true) {
        fd.setMultipleMode(true);
    } else {
        fd.setMultipleMode(false);
    }



    fd.setVisible(true);
    var retFilename = fd.getFile();
    if (retFilename === null || retFilename === undefined || retFilename == "" || retFilename == " ") {
        return {'filename' : null, 'directory' : null};
    } else {
        return {'filename' : fd.getFile(), 'directory' : fd.getDirectory()};
    }
}

/* A string representing the location of the system temporary directory */
files.tempdir = FileUtils.getTempDirectoryPath();

/*
 * A string representing the location of the user's home directory. MAY
 * MISBEHAVE ON WINDOWS VISTA+
 */
files.userdir = FileUtils.getUserDirectoryPath();