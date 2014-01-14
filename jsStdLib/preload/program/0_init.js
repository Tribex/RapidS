/* Provides program functions, mainly exiting */

var program = {};

program.exit = function (code) {
    System.exit(code);
}

program.showError = Packages.us.derfers.tribex.rapids.Utilities.showError;

