//adds functions to String
//the purpose of all the if statements -example below-
//if (typeof String.prototype.startsWith != 'function')
//is in case these functions are defined by the JavaScript
//interpriter in the future. Defining them when they are already defined can cause
//problems and seems to be keeping FireFox (for example) from implementing
//String.contains();
//tell if the string (this) ends with string

if (typeof String.prototype.endsWith != 'function') {
        String.prototype.endsWith = function(string){
        return (this.indexOf(string) == this.length - string.length);
    };
}

//tell if the the string (this) contains string
if (typeof String.prototype.contains != 'function') {
        String.prototype.contains = function(string){
        return (this.indexOf(string) > -1);
    };
}

//tell if the string (this) starts with string
if (typeof String.prototype.startsWith != 'function') {
        String.prototype.startsWith = function(string){
        return (this.indexOf(string) == 0);
    };
}

