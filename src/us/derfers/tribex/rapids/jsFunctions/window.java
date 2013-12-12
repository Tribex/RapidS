package us.derfers.tribex.rapids.jsFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptException;

import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;

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
	
	
	//XXX: WINDOW OPERATIONS :XXX\\
	
	//Easy reference to shell
	public static javax.swing.JFrame window = (javax.swing.JFrame) Main.loader.XMLWidgets.get("__WINDOW__").get("__WINDOW__");
	
	//Easy title changing without direct access to shell
	public static void setTitle(String title) {
		window.setTitle(title);
	}
	
	//XXX: TIMER OPERATION :XXX\\
	static Timer timer = new Timer();
	public static void setTimeout(final String function, int seconds) {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
        	public void run() {
        		try {
					Main.loader.engine.eval(function);
				} catch (ScriptException e) {
					Utilities.showError("Bad JavaScript setTimeout call: "+function);
					Utilities.debugMsg("Bad JavaScript setTimeout call: "+function);
				}
        	}
        };
        timer.schedule(timerTask, seconds*1000);
	}
	
}
