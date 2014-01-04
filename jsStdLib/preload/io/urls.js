/** Provides basic url reading and writing features
 * --USES APACHE COMMONS IO--*/

//import packages
require(Packages.java.net.URL);
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.Utilities);
require(Packages.org.apache.commons.io.IOUtils);
require(Packages.org.jdesktop.http.Method);
require(Packages.org.jdesktop.http.async.XmlHttpRequest);
function urls(url, encoding) {
	
	//I like to use these to hide variables while maintaining their presence... I feel safer with them. :3
	var url = url;
	var encoding = encoding;
	
	/* Reads the contents of a webpage/url. Can also send get requests. */
	this.read = function() {
		try {
			var hasRead = IOUtils.toString(new URL(url));
			if (hasRead != "" && hasRead != " " && hasRead != null && hasRead != undefined) {
				return hasRead;
			} else {
				return "Error: URL not found: '"+url+"'.";
			}
		} catch (e) {
			return e.message;
		}
	}
	
	this.post = function(data) {
		var req = new XmlHttpRequest();
		req.open(Method.POST, url);
		req.send(data);
	}
	
	this.get = function(data) {
		try {
			var hasRead = IOUtils.toString(new URL(url+data));
			if (hasRead != "" && hasRead != " " && hasRead != null && hasRead != undefined) {
				return hasRead;
			} else {
				return "Error: URL not found: '"+url+"'.";
			}
		} catch (e) {
			return e.message;
		}
	}
}

/* Static constructor for a URL object */
urls.open = function(url, encoding) {
	if (encoding == null) {
		encoding = "UTF-8";
	}
	
	return new urls(url, encoding);
}

/* STATIC - Reads the contents of a webpage/url. Can also send get requests. */
urls.read = function(url, encoding) {
	if (encoding == null) {
		encoding = "UTF-8";
	}
	
	try {
		var hasRead = IOUtils.toString(new URL(url), encoding);
		if (hasRead != "" && hasRead != " " && hasRead != null && hasRead != undefined) {
			return hasRead;
		} else {
			return "Error: URL not found: '"+url+"'.";
		}
	} catch (e) {
		return e.message;
	}
}