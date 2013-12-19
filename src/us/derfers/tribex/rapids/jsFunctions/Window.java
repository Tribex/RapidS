package us.derfers.tribex.rapids.jsFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import us.derfers.tribex.rapids.ScriptEngine;

import us.derfers.tribex.rapids.Main;

public class Window {	

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
	
	//Easy static reference to the engine
	static ScriptEngine engine = Main.loader.engine;
	
	
	//XXX: TIMER OPERATION :XXX\\
	static Timer timer = new Timer();
	
	//
	public static void setTimeout(final String function, int seconds) {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
        	@Override
        	public void run() {
    			engine.eval(function);

        	}
        };
        timer.schedule(timerTask, seconds);
	}
	
}
