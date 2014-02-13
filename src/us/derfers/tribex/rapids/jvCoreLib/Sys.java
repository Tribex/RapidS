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

package us.derfers.tribex.rapids.jvCoreLib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
     * Adds the specified JAR to the class path. VERY HACKY AND COMPLICATED. Avoid whenever possible.
     * @param fileString The name of the JAR to add to the class path
     */
    public static void addJarToClasspath(String fileString) {
        try {
            URLClassLoader classLoader
            = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class<URLClassLoader> cls = URLClassLoader.class;

            // Use reflection
            Method method= cls.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(classLoader, new Object[] {new File(Globals.getCWD(fileString)).toURI().toURL()});
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
                Main.loader.engine.eval(new FileReader(fileString), fileString);

                //Load file from home directory. DOESNT WORK ON WINDOWS VISTA/7
            } else if (fileString.startsWith("~/")) {
                System.out.println(System.getProperty("user.home")+fileString.replace("~", ""));
                Main.loader.engine.eval(new FileReader(System.getProperty("user.home")+fileString.replace("~", "")), fileString);

                //Load file from jar directory.
            } else {
                System.out.println(Globals.CWD);
                Main.loader.engine.eval(new FileReader(Globals.CWD+fileString), fileString);
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

    /**
     * Creates a thread and returns it to the calling JavaScript.
     * @param javascript The script to run.
     */
    public static Thread Worker(final String javascript) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Main.loader.engine.eval(javascript, "RapidS worker thread.");
            }
        });
    }
}
