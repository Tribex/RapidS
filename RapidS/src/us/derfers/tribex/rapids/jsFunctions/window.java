package us.derfers.tribex.rapids.jsFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

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
		int i = 0;
		System.out.println(Main.loader.XMLWidgets.toString());
		for (Map<String, Object> element: Main.loader.XMLWidgets.values()) {
			if (element.get("class") != null && element.get("class").equals(class_identifier)) {
				elementList.add(element.get(element.get("id")));
				i++;
			}
		}		

		return elementList.toArray();
		
	}
	
	
	//XXX: WINDOW OPERATION :XXX\\
	
	//Easy reference to shell
	public static org.eclipse.swt.widgets.Shell shell = (Shell) Main.loader.XMLWidgets.get("__SHELL__").get("__SHELL__");
	
	//Easy title changing without direct access to shell
	public static void setTitle(String title) {
		shell.setText(title);
	}
	
}

