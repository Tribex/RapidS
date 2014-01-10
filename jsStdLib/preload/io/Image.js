//reads images from files

//read an image from a string
function getImageIcon(src){
	try{
		var image = new ImageIcon(src);
		return image;
	}
	catch (e) {
			Utilities.showError("Error reading image file: "+filename+". Does it exist?\n\n"+e.message);
			return null;
	}
}

