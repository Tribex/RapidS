package us.derfers.tribex.rapids;
import java.io.IOException;
import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.*;

public class ScriptEngine {
	//Have a global scope for each ScriptEngine
	public Scriptable scope;
	
	//Constructor to populate the scope
	public ScriptEngine() {
		//Create the context
		Context jsContext = Context.enter();
		
		//Populate the scope with importertoplevel TODO: Replace this
		scope = new org.mozilla.javascript.ImporterTopLevel(jsContext);
		
		//Delete the context
		Context.exit();
	}
	
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
	
	public void put(String var, Object property) {
		Context.enter();

		ScriptableObject.putProperty(scope, var, property);
		Context.exit();
	}

}
