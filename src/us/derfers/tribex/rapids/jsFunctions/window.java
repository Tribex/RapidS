package us.derfers.tribex.rapids.jsFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import us.derfers.tribex.rapids.ScriptEngine;

import us.derfers.tribex.rapids.Main;

/**
 * Provides static functions for dealing with windows, getting widgets by ID or class,
 * or set a JavaScript function to run after a given number of milliseconds.
 */
public class window {	

	//XXX: ELEMENT GETTERS :XXX\\
	
	/**
	 * A clone of the JavaScript function getElementByID()
	 * @param id The ID of the object to return
	 * @return The object associated with the given ID
	 */
	public static Object getElementById(String id) {
		if (Main.loader.XMLWidgets.get(id) != null) {
			return Main.loader.XMLWidgets.get(id).get(id);
		} else {
			return null;
		}
		
	}
	
	/**
	 * Collects all elements of a given class into an Object array
	 * @param class_identifier The name of the class to work with
	 * @return An Object array of the elements that are of the given class
	 */
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
	
	/**
	 * Sets the title of the window
	 * @param title The new title for the window
	 */
	public static void setTitle(String title) {
		window.setTitle(title);
	}
	
	//Easy static reference to the engine
	static ScriptEngine engine = Main.loader.engine;
	
	
	//XXX: TIMER OPERATION :XXX\\
	static Timer timer = new Timer();
	
	/**
	 * Schedules a JavaScript function to run after a given number of milliseconds
	 * @param function The name of the JavaScript function to run
	 * @param milliseconds The number of milliseconds to delay before calling the
	 * JavaScript function
	 */
	public static void setTimeout(final String function, int milliseconds) {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
        	@Override
        	public void run() {
    			engine.eval(function);

        	}
        };
        timer.schedule(timerTask, milliseconds);
	}
	
}
