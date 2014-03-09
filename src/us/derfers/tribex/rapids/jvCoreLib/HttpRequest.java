/*
    RapidS - Web style development for the desktop.
    Copyright (C) 2014 TribeX Software Development

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Provides a HttpRequest object for JavaScript that functions in the same way as the browser counterpart.
 * Not fully tested!
 * @author Tribex
 */

package us.derfers.tribex.rapids.jvCoreLib;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jdesktop.http.Method;
import org.jdesktop.http.async.AsyncHttpRequest;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;

/**
 * HttpRequest object that functions in the same way as the browser counterpart.
 * @author Tribex
 */
public class HttpRequest extends AsyncHttpRequest {
    /**
     * Overload of the open method to accept a string as the method.
     * @param method either "GET" or "POST". Not case sensitive.
     * @param url The URL that you are trying to open a connection to.
     */
    public void open(String method, String url) {
        if (method.toUpperCase().equals("GET")) {
            open(Method.GET, url);
        } else if (method.toUpperCase().equals("POST")) {
            open(Method.POST, url);
        } else {
            Utilities.showError("Error: Invalid method in HttpRequest: "+method);
            return;
        }
    }

    /**
     * Allows calling a JS function from the listener.
     * @param func The JavaScript function to call.
     */
    public void addReadyStateChangeListener(final Function func) {
        addRSCL(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                func.call(Context.enter(), Main.loader.engine.scope, func, new Object[]{evt});
            }
        });
    }
}
