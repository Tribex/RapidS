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


package us.derfers.tribex.rapids.parsers;

import java.util.HashMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A custom-made parser for CSS type files. Uses regexes.
 *
 * @author TribeX
 *
 */
public class CSSParser {
    private String toParse;

    /**
     * Creates a new CSSParser with the CSS to parse.
     * @param ParseString The CSS to parse
     */
    public CSSParser(String ParseString) {
        toParse = ParseString;
    }

    /**
     * Parses everything in the private field toParse, which is initialized by the CSSParser constructor.
     * @return A Map[String, Map[String, String]] containing the parsed CSS
     */
    public Map<String, Map<String, String>> parseAll() {

        String[] rules = toParse.split("}");
        for (String rule : rules){
            String[] splitRule = rule.split("\\{");
            if (splitRule.length == 2) {
                String head = rmTabNewLns(splitRule[0].trim());
                String body = rmTabNewLns(splitRule[1].trim());
                System.out.println("HEAD: "+head+"\n");
                System.out.println("BODY: "+body+"\n");
            } else {
                System.out.println("END OF FILE");
            }
        }
        //System.exit(0);
        //Initialize properties for further searching
        return new HashMap<String, Map<String, String>>();


    }

    /**
     * Removes newlines and tabs from a string.
     * @param string A string with tabs and newlines.
     * @return A string with all of its tabs and newlines removed.
     */
    public static String rmTabNewLns(String string) {
        string = string.replaceAll("\t", "");
        string = string.replaceAll("\n", "");
        return string;
    }

    /**
     * Takes a identifier and prefix, and returns a Map[String, String] containing each property and its values.
     * @param idTag The identifier (eg, class, element, or id). Gets appended to the prefix.
     * @param prefix The prefix for the identifier: #, ., or null.
     * @return A Map[String, String] of the seperated identifiers and values. eg, {"bgcolor" : "#FFF", "padding" : "200"}
     */
    /*public Map<String, String> getIdValues(String idTag, String prefix) {

        //The map that will eventually be returned
        Map<String, String> idMap = new HashMap<String, String>();

        //Add prefix to tag, to match classes, xml objects, or ids
        idTag = prefix+idTag;

        //Regex for matching the id
        Pattern idPattern = Pattern.compile(""+idTag+".*?\\{(.*?)}", Pattern.DOTALL);
        Matcher idMatcher = idPattern.matcher(toParse);

        //Initialize properties for further searching
        String properties = "";

        //Look for properties of idTag
        while (idMatcher.find()) {
            //Set properties to the contents of idTag
            properties = idMatcher.group(1).trim();
        }

        //Regex to loop through properties and add them to the map
        Matcher propMatcher = propPattern.matcher(properties);
        while (propMatcher.find()) {
            idMap.put(propMatcher.group(1).trim(), propMatcher.group(2).trim());
        }

        return idMap;

    }

    /**
     * Runs getIdValues with the prefix '.', for extracting classes.
     * @param classTag The string identifying the class.
     * @return A Map[String, String] of the seperated identifiers and values. eg, {"bgcolor" : "#FFF", "padding" : "200"}
     */
   /* public Map<String, String> getClassValues(String classTag) {
        return getIdValues(classTag, ".");

    }

    /*TODO: Implement*/
   /* public Map<String, Object> getByAttr(String attrTag) {
        return null;

    }*/
}
