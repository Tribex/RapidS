package us.derfers.tribex.rapids;
import java.io.IOException;
import java.io.Reader;

import org.mozilla.javascript.*;

/**
 * Provides a basic javax.scriptengine type interface for Mozilla's Rhino JavaScript Engine.
 * 
 * @author TribeX
 *
 */
public class ScriptEngine {
	/** The global scope for the engine, contains information about the execution enviornment.*/
	public Scriptable scope;
	
	/** Populates the scope with the ImporterTopLevel variables. Possibly will be deprecated.*/
	public ScriptEngine() {
		//Create the context
		Context jsContext = Context.enter();
		
		//Populate the scope with importertoplevel TODO: Replace this
		scope = new org.mozilla.javascript.ImporterTopLevel(jsContext);
		//Delete the context
		Context.exit();
	}
	
	/**
	 * Run a JavaScript string through the engine.
	 * @param script The script to run.
	 */
	public void eval(String script) {
		//Create the thread's context
		Context jsContext = Context.enter();

		//Compile the script
		Script scriptjs = jsContext.compileString(script, "RapidS Script", 1, null);
		
		//Run the script
		scriptjs.exec(jsContext, scope);
		
		//Exit the context
		Context.exit();
	}

	/**
	 * Run a JavaScript reader (normally a file) through the engine.
	 * @param script The script (reader) to run.
	 */
	public void eval(Reader script) {
		Context jsContext = Context.enter();

		/*A Javascript JSON Object*/
		Script scriptjs = null;
		try {
			scriptjs = jsContext.compileReader(script, "RapidS Script", 1, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*Result is a Javascript Object*/
		scriptjs.exec(jsContext, scope);
		Context.exit();
	}
	
	/**
	 * Insert a variable into the JavaScript global scope
	 * @param var The name of the variable to insert.
	 * @param property The value of the variable to insert.
	 */
	public void put(String var, Object property) {
		Context.enter();

		ScriptableObject.putProperty(scope, var, property);
		Context.exit();
	}

}
