//pretends to try to think it almost half starts to have a buggy way to almost kinda read a file (pending)

//import packages
require(Packages.io.File);
require(Packages.util.Scanner);

//creates a new file
function file{
	
	//create a new scanner from the file name
	this.scanner = new Scanner(new File(filename));			
	
	//finds if there's anything more in the file
	function hasNext(){
		return scanner.hasNext();	
	}

	//finds the next word in the file
	function getNext(){
		return scanner.getNext();
	}

	//gets the next line in the file
	function getNextLine(){
		return scanner.getNextLine();
	}

	s//more functions to come after debug
}
