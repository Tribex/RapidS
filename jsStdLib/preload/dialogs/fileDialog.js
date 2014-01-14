
/* Opens the system file load or save dialog. */
dialogs.fileDialog = function(opts) {

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
