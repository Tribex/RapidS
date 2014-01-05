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

	private Pattern idPattern = Pattern.compile("(.*?)\\s*?\\{(.*?)}", Pattern.DOTALL);
	
	private Pattern propPattern = Pattern.compile("(.*?):(.+?);.*?", Pattern.DOTALL);

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
		
		//The map that will eventually be returned
		Map<String, Map<String, String>> idMap = new HashMap<String, Map<String, String>>();
		Map<String, String> valueMap = new HashMap<String, String>();

		//Pattern to get property id

		Matcher idMatcher = idPattern.matcher(toParse);

		//Initialize properties for further searching
		
		//Look for properties
		while (idMatcher.find()) {
			valueMap = this.getIdValues(idMatcher.group(1).trim(), "");
			idMap.put(idMatcher.group(1).trim(), valueMap);
		}
		
		return idMap;
		
	}
	
	/**
	 * Takes a identifier and prefix, and returns a Map[String, String] containing each property and its values.
	 * @param idTag The identifier (eg, class, element, or id). Gets appended to the prefix.
	 * @param prefix The prefix for the identifier: #, ., or null.
	 * @return A Map[String, String] of the seperated identifiers and values. eg, {"bgcolor" : "#FFF", "padding" : "200"}
	 */
	public Map<String, String> getIdValues(String idTag, String prefix) {
		
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
	public Map<String, String> getClassValues(String classTag) {
		
		return getIdValues(classTag, ".");
		
	}
	
	/**TODO: Implement*/
	public Map<String, Object> getByAttr(String attrTag) {
		return null;
		
	}
}
