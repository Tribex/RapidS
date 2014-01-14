/** Provides a paralell of the Java String.contains() method. */

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
