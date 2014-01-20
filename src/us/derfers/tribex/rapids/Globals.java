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


package us.derfers.tribex.rapids;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds variables that must be accessed by all classes at all times.
 * Don't modify this unless you know what you're doing.
 *
 * @author TribeX
 */
public class Globals {

    //XXX: SYSTEM :XXX\\
    /**The selected current working directory. DON'T modify this, as it is automatically set.*/
    public static String CWD = null;

    /**
     * Getter for the CWD. Useful for JavaScripts that need it.
     * @return selCWD
     */
    public static String getCWD() {
        return getCWD("");
    }

    /**
     * Getter for the CWD. Useful for JavaScripts that need it.
     * @param append The file to append to the CWD.
     * @return selCWD
     */
    public static String getCWD(String append) {
        if (append.startsWith(":\\", 1) || append.startsWith("/") || append.startsWith("~")) {
            return append;
        } else {
            return CWD+"/"+append;
        }

    }
    //XXX: STYLES :XXX\\
    /**Holds all styles TODO: Make window-specific*/
    public static Map<String, Map<String, String>> stylesMap = new HashMap<String, Map<String, String>>();

    //XXX: WIDGETS :XXX\\
    /**Holds all possible widget listener types*/
    public static String[] listenerTypesArray = {"onmouseup", "onmousedown", "onmouseover", "onmouseout", "onselection", "onclick"};
}


