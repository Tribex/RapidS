/*
    RapidS - Web style development for the desktop.
    Copyright (C) 2014 TribeX Software Development

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


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
     * @param funcPath The function to call.
     * @param args Varargs, the arguments to pass to the function.
     */
    //TODO: Make this flexible for multiple layers of objects.
    public Object call(String funcPath, Object... args) {
        //Enter the context
        Context jsContext = Context.enter();
        //Set optimization to max.
        jsContext.setOptimizationLevel(2);

        //Split the function into objects.
        String[] splitObjs = funcPath.split("\\.");

        Scriptable parentScope = scope;

        //If the function is not global
        for (int i = 0; i< splitObjs.length-1; i++) {
            //Set the parent object
            parentScope = (Scriptable) parentScope.get(splitObjs[i], scope);
        }
        //Create a new function with the values passed to call.
        Function fct;

        //Get and run the function from the global scope
        fct = (Function)parentScope.get(splitObjs[splitObjs.length-1], parentScope);

        //Call the function with the arguments passed to call.
        Object result = fct.call(jsContext, parentScope, parentScope, args);

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
     * @param inscope The scope to get the variable from
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
    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getMap(String var, Object scope) {
        return new HashMap<String, Object>((Map<String, Object>) get(var, scope));
    }

}
