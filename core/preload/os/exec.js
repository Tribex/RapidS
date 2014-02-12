/**
 * @file Allows executing Operating System commands from JavaScript.
 * @author Tribex
 */
require(Packages.us.derfers.tribex.rapids.jvCoreLib.Sys);

/**
 * Execute a command and optionally wait for it to finish.
 * @param command {string} The command to run.
 * @param waitfor {boolean} Wait for the command to finish or not.
 */
os.exec = function(command, waitfor) {
    if (waitfor != null && waitfor != undefined) {
        return Sys.execCommand(command, new Packages.java.lang.Boolean(waitfor));
    } else {
        return Sys.execCommand(command, false);
    }
}
