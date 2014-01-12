/* Allows executing OS shell commands from JavaScript */
require(Packages.us.derfers.tribex.rapids.jvStdLib.Sys);

//Executes a command
os.exec = function(command, wait) {
	if (wait != null && wait != undefined) {
		return Sys.execCommand(command, new Packages.java.lang.Boolean(wait));
	} else {
		return Sys.execCommand(command, false);
	}
}