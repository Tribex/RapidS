package us.derfers.tribex.rapids;

import java.util.HashMap;
import java.util.Map;

public class Globals {
	
	//XXX: SYSTEM :XXX\\
	static String[] jvStdLib = {"us.derfers.tribex.rapids.jsFunctions"};
	public static String selCWD = null;
	
	//XXX: STYLES :XXX\\
	//Holds all styles TODO: Make window-specific
	public static Map<String, Map<String, String>> stylesMap = new HashMap<String, Map<String, String>>();

	
	//XXX: WIDGET-SPECIFIC :XXX\\
	//Holds all posssible listener types
	public static String[] listenerTypesArray = {"onmouseup", "onmousedown", "onmouseover", "onmouseout", "onselection", "onclick"};

}
