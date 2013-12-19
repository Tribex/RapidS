package us.derfers.tribex.rapids.jsFunctions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import us.derfers.tribex.rapids.Utilities;

/**
 * Provides a static method for adding JARs to a class path before execution
 */
public class sys {
	
	/**
	 * Adds the specified JAR to the class path
	 * @param fileString The name of the JAR to add to the class path
	 */
	public static void addJarToClasspath(String fileString) {
		try {
			File file = new File(Utilities.getJarDirectory()+fileString);
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
			method.setAccessible(true);
			method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
		} catch (NoSuchMethodException e) {
			Utilities.showError("Error adding Jar to Classpath.  Are you not using a standard JRE?");
		} catch (SecurityException e) {
			Utilities.showError("Error adding Jar to Classpath.  Security Error");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Utilities.showError("Error adding Jar to Classpath.  Security: Illegal Access.");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Utilities.showError("Error adding Jar to Classpath.  Illegal argument");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			Utilities.showError("Error adding Jar to Classpath.  Unable to load jar, improperly packaged?");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			Utilities.showError("Error adding Jar to Classpath.  Bad Path");
			e.printStackTrace();
		}
	}
}
