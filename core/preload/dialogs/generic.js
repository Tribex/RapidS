/**
 * @file Provides methods for showing generic dialog windows.
 * @author Nateowami
 */
require(Packages.javax.swing.JOptionPane);
require(Packages.us.derfers.tribex.rapids.Globals);

//converts a string to a JOptionPane.SOME_ENUM_VALUE
//this is because JavaScript can't easily access JOptionPane, so they can send the message type as a string
//and this function will convert it to an enum
dialogs.getType = function(type){
    switch(type){
    case "error":
        return JOptionPane.ERROR_MESSAGE;
        break;
    case "plain":
        return JOptionPane.PLAIN_MESSAGE;
        break;
    case "warning":
        //on Ubuntu "warning" looks like an error message. Guessing it
        //depends on the platform
        return JOptionPane.WARNING_MESSAGE;
        break;
    case "info":
        return JOptionPane.INFORMATION_MESSAGE;
        break;
    default:
        return JOptionPane.PLAIN_MESSAGE;
    }
}

/**
 * Traditional alert dialog.
 * @param message {string} The message to display in the dialog.
 */
dialogs.alert = function(message){
    //This is the only way to display an information message.
    //Which is really odd since it doesn't let you set the title.
    JOptionPane.showMessageDialog(null, message);
}

/**
 * An alias of dialogs.alert.
 * @param message {string} The message to display in the dialog.
 */
alert = dialogs.alert;

/**
 * Show a dialog with a custom title, message, type, and/or icon.
 * @param message {string} The message to display in the dialog.
 * @param title {string} The dialog window title.
 * @param type {string} The type of dialog. Can be 'error', 'plain', 'warning', or 'info'. OPTIONAL
 * @param icon {string} The path to an image file to display as the icon next to the text. OPTIONAL
 */
dialogs.message = function(message, title, type, icon){

    //If the user didn't specify an icon
    if (icon == null || icon == undefined) {
        //Show a message without an icon
        JOptionPane.showMessageDialog(null, message, title, dialogs.getType(type));
    } else {
        //Attempt to load the icon, which *may* not work.
        try {
            var completeIcon = Globals.getCWD(icon);
            JOptionPane.showMessageDialog(null, message, title, dialogs.getType(type), images.getImageIcon(completeIcon));
        } catch (e) {
            Utilities.showError("Error loading icon '"+completeIcon+"' into Dialog: "+e.message);
        }
    }
}


/**
 * Get input from the user.
 * @param message {string} The message to display.
 * @return {string} The value entered by the user.
 */
dialogs.input = function(message){
    return JOptionPane.showInputDialog(message);
}


/**
 * Alias of dialogs.input
 * @param message {string} The message to display.
 * @return {string} The value entered by the user.
 */
prompt = dialogs.input;

/**
 * Yes/No confirmation dialog.
 * @param message {string} The message to display
 * @param title {string} The title of the dialog window.
 * @return {boolean} True if the user answered Yes, False if No.
 */
dialogs.confirm = function(message, title){
    if (title === null || title === undefined) {
        title = "Confirm";
    }

    var reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
    if (reply == JOptionPane.YES_OPTION) {
        return true;
    } else {
        return false;
    }
}

/**
 * Alias of dialogs.confirm
 * @param message {string} The message to display
 * @param title {string} The title of the dialog window.
 * @return {boolean} A boolean. True if the user answered Yes, False if No.
 */
confirm = dialogs.confirm;
