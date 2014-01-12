package us.derfers.tribex.rapids.jvStdLib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import us.derfers.tribex.rapids.Globals;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;


/**
 * Provides static methods for adding JARs to a class path before execution and running JavaScript from files.
 * 
 * @author TribeX, Nateowami
 */
public class Sys {

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


	/**
	 * Runs the specified JavaScript file.
	 * @param fileString The name of the JavaScript file to run
	 */
	public static void importJS(String fileString) {
		try {
			//Load file from root directory.
			if (fileString.startsWith(System.getProperty("path.separator")) || fileString.startsWith("C:\\")) {
				Main.loader.engine.eval(new FileReader(fileString));

				//Load file from home directory. DOESNT WORK ON WINDOWS VISTA/7
			} else if (fileString.startsWith("~/")) {
				System.out.println(System.getProperty("user.home")+fileString.replace("~", ""));
				Main.loader.engine.eval(new FileReader(System.getProperty("user.home")+fileString.replace("~", "")));

				//Load file from jar directory.
			} else {
				System.out.println(Globals.selCWD);
				Main.loader.engine.eval(new FileReader(Globals.selCWD+fileString));
			}
		} catch (FileNotFoundException e) {
			Utilities.showError("File does not exist: '" +fileString+"'\n\n"
					+ "Unexpected program behavior may result.");
		}
	}
	
	/**
	 * Executes an Operating System command, with the option of waiting for it to finish and passing the logs.
	 * @param command The command to run. Arguments should be separated by spaces.
	 * @param waits Whether or not the program should wait for the command to finish.
	 * @return 1 if error, 0 if successful.
	 */
	public static int execCommand(String command, Boolean wait) {
		java.lang.ProcessBuilder processBuilder = new java.lang.ProcessBuilder(command.split(" "));
		java.lang.Process p = null;
		try {
			// Create an environment (shell variables)
			processBuilder.environment().putAll(System.getenv());
			//change the working directory
			processBuilder.directory(new java.io.File(Globals.getCWD()));

			// Start new process
			p = processBuilder.start();
			if (wait) {
				p.waitFor();
				BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line=reader.readLine();
				while(line!=null) { 
					System.out.println(line);
					line=reader.readLine(); 
				}
			}

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
}
