/**
 * @file Provides basic url reading and writing features
 * @author Tribex
 * --USES APACHE COMMONS IO--
 */

require(Packages.java.net.URL);
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.Utilities);
require(Packages.org.apache.commons.io.IOUtils);

/**
 * A connection to a URL.
 * @param url {string} The url to connect to.
 * @param encoding {string} The encoding to use for the URL
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
}

/**
 * Returns a urls object with the specified URL and encoding.
 * @param url {string} The web address of the file you are trying to manipulate.
 * @param encoding {string} The encoding of the file at the URL address.
 */
urls.open = function(url, encoding) {
    if (encoding == null) {
        encoding = "UTF-8";
    }

    return new urls(url, encoding);
}

/**
 * Static function to read the plain-text content of a URL.
 * @param url {string} The URL to read from.
 * @param encoding {string} The encoding of the file at the URL.
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

/**
 * Static function to save the bytes of a file on the internet to a location on the hard drive.
 * @param url {string} The URL from wich to download bytes from.
 * @param file {string} The location of the file to save the bytes to.
 * @param callback {function} A function to call once the file
 */
urls.download = function(url, file, callback, connectTimeout, readTimeout) {
    if (readTimeout == null && connectTimeout == null) {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(Globals.getCWD(file)), 15000, 200000000);
            return runCallback(true);
        } catch (e) {
            Utilities.showError("Error downloading from '"+url+"'. Unable to establish connection. (Timed out)");
            return runCallback(false);
        }
    } else if (readTimeout == null) {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(Globals.getCWD(file)), connectTimeout/2, connectTimeout/2);
            return runCallback(true);
        } catch (e) {
            Utilities.showError("Error downloading from '"+url+"'. Unable to establish connection. (Timed out)");
            return runCallback(false);
        }
    } else {
        try {
            FileUtils.copyURLToFile(new URL(url), new File(Globals.getCWD(file)), connectTimeout, readTimeout);
            return runCallback(true);
        } catch (e) {
            Utilities.showError("Error downloading from '"+url+"'. Unable to establish connection. (Timed out)");
            return runCallback(false);
        }
    }

    function runCallback(boolean) {
        if (callback != null) {
            callback(boolean);
            return boolean;
        } else {
            return boolean;
        }
    }

}
