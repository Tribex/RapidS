package us.derfers.tribex.rapids.parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSSParser {
	private String toParse;
	public CSSParser(String ParseString) {
	    toParse = ParseString;
	}
	
	public Map<String, Map<String, String>> parseAll() {
		
		//The map that will eventually be returned
		Map<String, Map<String, String>> idMap = new HashMap<String, Map<String, String>>();
		Map<String, String> valueMap = new HashMap<String, String>();

		//Pattern to get property id

		Pattern idPattern = Pattern.compile("(.*?)\\s*?\\{(.*?)}", Pattern.DOTALL);
		Matcher idMatcher = idPattern.matcher(toParse);
		
		//Initialize properties for further searching
		
		//Look for properties
		while (idMatcher.find()) {
			valueMap = this.getIdValues(idMatcher.group(1).trim(), "");
			idMap.put(idMatcher.group(1).trim(), valueMap);
		}
		
		return idMap;
		
	}
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
		Pattern propPattern = Pattern.compile("(.*?):(.+?);.*?", Pattern.DOTALL);
		Matcher propMatcher = propPattern.matcher(properties);
		while (propMatcher.find()) {
			idMap.put(propMatcher.group(1).trim(), propMatcher.group(2).trim());
		}

		return idMap;
		
	}
	
	public Map<String, String> getClassValues(String classTag) {
		
		return getIdValues(classTag, ".");
		
	}
	
	public Map<String, Object> getByAttr(String attrTag) {
		return null;
		
	}
}
