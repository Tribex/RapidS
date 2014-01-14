//Provides methods for showing dialog windows.
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

//For old times's sake
dialogs.alert = function(message){
    //This is the only way to display an information message.
    //Which is really odd since it doesn't let you set the title.
    JOptionPane.showMessageDialog(null, message);
}

alert = dialogs.alert;

//Show a message with a custom title and message type.
//@Nateowami: I combined this with showMessageWithIcon, for brevity's sake.
dialogs.message = function(message, title, type, icon){

    //If the user didn't specify an icon
    if (icon == null || icon == undefined) {
        //Show a message without an icon
        JOptionPane.showMessageDialog(null, message, title, dialogs.getType(type));
    } else {
        //Attempt to load the icon, which *may* not work.
        try {
            var completeIcon = Globals.getCWD()+icon;
            JOptionPane.showMessageDialog(null, message, title, dialogs.getType(type), images.getImageIcon(completeIcon));
        } catch (e) {
            Utilities.showError("Error loading icon '"+completeIcon+"' into Dialog: "+e.message);
        }
    }
}


//Get input from the user.
dialogs.input = function(message){
    return JOptionPane.showInputDialog(message);
}

prompt = dialogs.getInput;

//Ask the user to confirm an action.
dialogs.confirm = function(message, title){
    if (title === null or title === undefined) {
        title = "Confirm";
    }

    var reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
    if (reply == JOptionPane.YES_OPTION) {
        return true;
    } else {
        return false;
    }
}

confirm = dialogs.getConfirmation;
