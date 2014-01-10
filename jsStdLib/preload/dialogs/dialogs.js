//provides methods for showing dialog windows
require(Packages.javax.swing.JOptionPane);

//for old times's sake
//remove if you don't like it
function alert(message){
	//this is the only way to display an information message 
	//which is really odd since it doesn't let you set the title
	JOptionPane.showMessageDialog(null, message);
}

//show a message with a custom title and message type
function showMessage(message, title, type){
	JOptionPane.showMessageDialog(null, message, title, getType(type));
}

//show a message with a custom icon
function showMessageWithIcon(message, title, type, icon){
	JOptionPane.showMessageDialog(null, message, title, getType(type), icon);
}

//converts a string to a JOptionPane.SOME_ENUM_VALUE
//this is because JavaScript can't easily access JOptionPane, so they can send the message type as a string
//and this function will convert it to an enum
function getType(type){
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

//get input from the user
function getInput(message){
	return JOptionPane.showInputDialog(message);
}

