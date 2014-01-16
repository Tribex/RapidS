//adds functions to String
//the purpose of all the if statements -example below-
//if (typeof String.prototype.startsWith != 'function')
//is in case these functions are defined by the JavaScript
//interpriter in the future. Defining them when they are already defined can cause
//problems and seems to be keeping FireFox (for example) from implementing
//String.contains();

//I left this function in because your startsWith() uses it
function stringContains(search, substr) {
    if (search.indexOf(substr) > -1) {
        return true;
    } else {
        return false
    }
}


if (!String.prototype.startsWith) {
    Object.defineProperty(String.prototype, 'startsWith', {
        enumerable: false,
        configurable: false,
        writable: false,
        value: function (searchString, position) {
            position = position || 0;
            return this.indexOf(searchString, position) === position;
        }
    });
}

//tell if the string (this) ends with string
if (typeof String.prototype.endsWith != 'function') {
		String.prototype.endsWith = function(string){
		return (this.indexOf(string) == this.length - string.length);
	};
}

//tell if the the string (this) contains string
if (typeof String.prototype.contains != 'function') {
		String.prototype.contains = function(string){
		return (this.indexOf(string) != -1); 
	};
}

/*
--**NOTE**-- Commented out because Tribex already has a function for this. I don't really 
understand the other one, and I didn't realize it was there when I wrote this one. You
can remove it if you like.

//tell if the string (this) starts with string
if (typeof String.prototype.startsWith != 'function') {
		String.prototype.startsWith = function(string){
		return (this.indexOf(string) == 0);
	};
}
*/
