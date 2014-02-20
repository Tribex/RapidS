/**
 * @file Provides image operations. May be moved into a separate folder.
 * @author Nateowami
 */

/**
 * Images object for operating on images
 * @namespace
 */
var images = {};

/**
 * Get an icon from the source location.
 * @param src {string} The source location of the image
 * @returns {ImageIcon} An image object containing the image from the src.
 */
images.getImageIcon = function(src){
    try{
        var image = new ImageIcon(Globals.getCWD(src));
        return image;
    }
    catch (e) {
            Utilities.showError("Error reading image file: "+filename+". Does it exist?\n\n"+e.message);
            return null;
    }
}

