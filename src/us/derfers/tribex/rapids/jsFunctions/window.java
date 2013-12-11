package us.derfers.tribex.rapids.jsFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import us.derfers.tribex.rapids.Main;

public class window {	
 
	//XXX: ELEMENT GETTERS :XXX\\
	//getElementById('string')
	public static Object getElementById(String id) {
		if (Main.loader.XMLWidgets.get(id) != null) {
			return Main.loader.XMLWidgets.get(id).get(id);
		} else {
			return null;
		}
		
	}
	
	//Returns A List of Elements
	public static Object[] getElementsByClass(String class_identifier) {
		
		List<Object> elementList = new ArrayList<Object>();
		
		//Loop through all the widgets
		for (Map<String, Object> element: Main.loader.XMLWidgets.values()) {
			if (element.get("class") != null && element.get("class").equals(class_identifier)) {
				elementList.add(element.get(element.get("id")));
			}
		}		

		return elementList.toArray();
		
	}
	
	
	//XXX: WINDOW OPERATION :XXX\\
	
	//Easy reference to shell
	public static javax.swing.JFrame window = (javax.swing.JFrame) Main.loader.XMLWidgets.get("__WINDOW__").get("__WINDOW__");
	
	//Easy title changing without direct access to shell
	public static void setTitle(String title) {
		window.setTitle(title);
	}
	
}

