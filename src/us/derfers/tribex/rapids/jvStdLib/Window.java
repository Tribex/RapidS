package us.derfers.tribex.rapids.jvStdLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import us.derfers.tribex.rapids.ScriptEngine;

import us.derfers.tribex.rapids.Main;

/**
 * Provides window methods accessable by JavaScript that are not easily implemented in JavaScript itself.
 * @author TribeX
 *
 */
public class Window {	

	/**
	 * Gets the widget object fromm the XMLWidgets Map that matches 'id'.
	 * @param id The id of the widget to return
	 * @return The widget specified by 'id' from XMLWidgets
	 */
	public static Object getElementById(String id) {
		if (Main.loader.XMLWidgets.get(id) != null) {
			return Main.loader.XMLWidgets.get(id).get(id);
		} else {
			return null;
		}

	}

	/**
	 * Gets an array of widget objects from the XMLWidgets Map that match 'class_identifier'.
	 * @param class_identifier The class of the widgets to return
	 * @return An Object Array of widgets specified by the 'class_identifier' from XMLWidgets.
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

	/**An easy reference to the top-level JFrame*/
	public static javax.swing.JFrame window = (javax.swing.JFrame) Main.loader.XMLWidgets.get("__WINDOW__").get("__WINDOW__");


	//An easy static reference to the JavaScript engine.
	private static ScriptEngine engine = Main.loader.engine;


	//XXX: TIMER OPERATION :XXX\\
	private static Timer timer = new Timer();

	/**
	 * Run a JavaScript function after <em>x</em> milliseconds.
	 * @param function The JavaScript function to run.
	 * @param milliseconds The time in milliseconds before 'function' is run.
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
