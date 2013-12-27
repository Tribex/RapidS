package us.derfers.tribex.rapids;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.*;

/**
 * Provides a basic javax.scriptengine style interface for Mozilla's Rhino JavaScript Engine.
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
		jsContext.setOptimizationLevel(2);
		
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
		jsContext.setOptimizationLevel(2);

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
		jsContext.setOptimizationLevel(2);

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
		Context.enter().setOptimizationLevel(2);

		ScriptableObject.putProperty(scope, var, property);
		Context.exit();
	}
	
	/**
	 * Call a JavaScript function from java with arguments.
	 * @param func The function to call.
	 * @param args Varargs, the arguments to pass to the function.
	 */
	//TODO: Make this flexible for multiple layers of objects.
	public Object call(String func, Object... args) {
		//Enter the context
		Context jsContext = Context.enter();
		//Set optimization to max.
		jsContext.setOptimizationLevel(2);
		
		//Split the function into objects.
		String[] splitObjs = func.split("\\.");
		
		//If func is referencing an object,
		Scriptable object = null;
		
		//If there is a reference to an object
		if (splitObjs.length > 0) {
			object = (Scriptable) scope.get(splitObjs[0], scope);
		}
		//Create a new function with the values passed to call.
		Function fct;
		
		if (object != null) {
			//Get and run the function from the object
			fct = (Function)object.get(splitObjs[1], object);
		} else {
			//Get and run the function from the global scope
			fct = (Function)scope.get(func, scope);
		}
		//Call the function with the arguments passed to call.
	    Object result = fct.call(jsContext, scope, scope, args);
	    
		Context.exit();
		return result;
	}
	
	/** Retreives an object from JavaScript (top level scope) for use in Java
	 * @param var A string repressenting the name of the object in JavaScript
	 * @return The value of the variable
	 */
	public Object get(String var) {
		return get(var, scope);
	}
	
	/** Retreives an object from JavaScript for use in Java
	 * @param var A string repressenting the name of the object in JavaScript
	 * @param scope The scope to get the variable from
	 * @return The value of the variable
	 */
	public Object get(String var, Object inscope) {
		
		if (!(inscope instanceof Scriptable)) {
			inscope = (Scriptable) get(inscope.toString());
		}
		//Enter the context
		Context jsContext = Context.enter();
		//Set optimization to max.
		jsContext.setOptimizationLevel(2);

		Object toReturn = ((Scriptable) inscope).get(var, (Scriptable) scope);
		
		Context.exit();
		return toReturn;
		
	}
	
	/** Retreives an object as a HashMap from JavaScript for use in Java
	 * @param var A string repressenting the name of the object in JavaScript
	 * @param scope The scope to get the variable from
	 * @return The value of the variable
	 */
	public HashMap<String, Object> getMap(String var, Object scope) {
		return new HashMap<String, Object>((Map<String, Object>) get(var, scope));
	}

}
