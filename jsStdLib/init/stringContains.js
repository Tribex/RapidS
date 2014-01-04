/** Provides a paralell of the Java String.contains() method. */

function stringContains(search, substr) {
	if (search.indexOf(substr) > -1) {
		return true;
	} else {
		return false
	}
}