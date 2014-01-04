/** Provides basic file reading, writing, polling, moving, and copying features. 
 * --USES APACHE COMMONS IO--*/

//import packages
require(Packages.java.io.File);
require(Packages.java.util.Scanner);
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.Utilities);
require(Packages.org.apache.commons.io.FileUtils);

/** Files object TODO: Support multiple encodings. */
function files(filename, flag) {
	//The file being operated on
	var filename = filename;
	
	//If the file operation specified is "read", "read-write", or "read-write-append" (r, rw, rw+)
	if (stringContains(flag, "r")) { 
		try {
			var scanner = new Scanner(new File(Globals.getCWD()+filename));
		} catch (e) {
			Utilities.showError("Error reading file: "+filename+". Does it exist?\n\n"+e.message);
		}
		
		//Returns a string containing all the contents of filename
		this.read = function () {
			return FileUtils.readFileToString(new File(Globals.getCWD()+filename));
		}
		
		//Reads the next line from filename
		this.readNextLine = function() {
				if (scanner.hasNextLine()) {
					return scanner.nextLine();
				} else {
					//Return a blank string to prevent errors
					return "";
				}
		}
		
		//Reads the next work from filename
		this.readNext = function() {
			if (scanner.hasNext()) {
				return scanner.next();
			} else {
				return "";
			}
		}
		
		//Boolean. Determines whether or not there is another word in filename
		this.hasNext = function() {
			return scanner.hasNext();
		}
		
		//Boolean. Determines whether or not there is another line in filename
		this.hasNextLine = function() {
			return scanner.hasNextLine();
		}
	}
	
	//If the file operation is "write", "write-append", "read-write", or "read-write-append" (w, w+, rw, rw+)
	if (stringContains(flag, "w")) {

		var append = false;

		//If the flag contains +, set append to true, causing any write operations to preserve existing data 
		if (stringContains(flag, "+")) {
			append = true;
		}
		
		//Write 'data' to 'filename'.
		this.write = function(data) {
			try {
				FileUtils.write(new File(Globals.getCWD()+filename), data, append);
			} catch (e) {
				Utilities.showError("Error writing to file: "+filename+" - "+e.message);
			}
		}
		
		//Write 'data' to 'filename'. Always preserves existing data, and adds a newline to the end of 'data'
		this.writeLine = function(data) {
			try {
				FileUtils.write(new File(Globals.getCWD()+filename), data+"\n", true);
			} catch (e) {
				Utilities.showError("Error writing line to file: "+filename+" - "+e.message);
			}
		}
	}
}

/* Static constructor that removes the need for a 'new' keyword*/
files.open = function(filename, flag) {
	return new files(filename, flag);
}

/* Static function for copying one filename to another */
files.copy = function(sourceFileName, destFileName) {
	//Set up Files
	var sourcefile = new File(Globals.getCWD()+sourceFileName);
	var destfile = new File(Globals.getCWD()+destFileName);
	
	//If the source exists (of course the destination doesn't
	if (sourcefile.exists()) {
		
		//If the source is a directory, copy it as a directory
		if (sourcefile.isDirectory()) {
			FileUtils.copyDirectory(sourcefile, destfile);
			return true;
		
		//If the source is a file, or unknown type, attempt to copy it as a file
		} else {
			FileUtils.copyFile(sourcefile, destfile);
			return true;
		}
		
	} else {
		Utilities.showError("File: "+sourceFileName+" does not exist!");
		return false;
	}
}