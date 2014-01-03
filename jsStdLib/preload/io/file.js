//pretends to try to think it almost half starts to have a buggy way to almost kinda read a file (pending)

//import packages
require(Packages.java.io.File);
require(Packages.java.util.Scanner);

//creates a new file
function file(filename){

	//instintate the methods
	this.getNextLine = getNextLine;
	this.getNext = getNext;
	this.hasNext = hasNext;	

	//create a new scanner from the file name
	try{
		this.scanner = new Scanner(new File(filename));			
	}
	catch (err){ 
		console.log(err.message);
	}
}

//finds if there's anything left in the file
function hasNext(){
		return this.scanner.hasNext();	
	}

//finds the next word in the file
function getNext(){
	return this.scanner.next();
}

//gets the next line in the file
function getNextLine(){
	return this.scanner.nextLine();
}
