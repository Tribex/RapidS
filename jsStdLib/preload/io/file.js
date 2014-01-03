//pretends to try to think it almost half starts to have a buggy way to almost kinda read a file (pending)
//TODO: Convert to proper object format that does not allow method overwriting. @Tribex

//import packages
require(Packages.java.io.File);
require(Packages.java.util.Scanner);
require(Packages.us.derfers.tribex.rapids.Globals);

//creates a new file
function file(filename){

	//instintate the methods
	this.getNextLine = getNextLine;
	this.hasNextLine = hasNextLine;
	this.getNext = getNext;
	this.hasNext = hasNext;	

	//create a new scanner from the file name
	try{
		this.scanner = new Scanner(new File(Globals.getCWD()+filename));			
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
	if (this.scanner.hasNext()) {
		return this.scanner.next();
	} else {
		return "__END_OF_FILE__";
	}
}

//gets the next line in the file
//FIXME: Currently broken
function getNextLine(){
	if (this.scanner.hasNextLine()) {
		return this.scanner.nextLine();
	} else {
		return "__END_OF_FILE__";
	}
}

//gets the next line in the file
function hasNextLine(){
	return this.scanner.hasNextLine();
}
