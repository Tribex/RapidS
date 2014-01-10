//reads images from files

//read an image from a string

//Construct a new images object to avoid cluttering the global namespace.
var images = {};

images.getImageIcon = function(src){
	try{
		var image = new ImageIcon(src);
		return image;
	}
	catch (e) {
			Utilities.showError("Error reading image file: "+filename+". Does it exist?\n\n"+e.message);
			return null;
	}
}

