/** Provides basic url reading and writing features
 * --USES APACHE COMMONS IO--*/

//import packages
require(Packages.java.net.URL);
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.Utilities);
require(Packages.org.apache.commons.io.IOUtils);
require(Packages.org.jdesktop.http.Method);
require(Packages.org.jdesktop.http.async.XmlHttpRequest);

/**
 * A connection to a URL.
 * @param url {String} The url to connect to.
 * @param encoding {String} The encoding to use for the URL
 * @namespace
 */
function urls(url, encoding) {

    //I like to use these to hide variables while maintaining their presence... I feel safer with them. :3
    var url = url;
    var encoding = encoding;

    /**
     * Reads the plain-text content of the urls object's url.
     * @memberof urls
     */
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

    /**
     * BROKEN: Sends a post request to the URL.
     * @param data {String} The data to send in the post request.
     * @memberof urls
     */
    this.post = function(data) {
        var req = new XmlHttpRequest();
        req.open(Method.POST, url);
        req.send(data);
    }

    /**
     * Kinda works: Sends a get request to the URL.
     * @param data {String} The data to send in the GET request.
     * @memberof urls
     */
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

/**
 * Returns a urls object with the specified URL and encoding.
 * @param url {String} The web address of the file you are trying to manipulate.
 * @param encoding {String} The encoding of the file at the URL address.
 */
urls.open = function(url, encoding) {
    if (encoding == null) {
        encoding = "UTF-8";
    }

    return new urls(url, encoding);
}

/**
 * Static function to read the plain-text content of a URL.
 * @param url {String} The URL to read from.
 * @param encoding {String} The encoding of the file at the URL.
 */
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
