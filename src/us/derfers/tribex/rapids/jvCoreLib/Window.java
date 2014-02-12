/*
    RapidS - Web style development for the desktop.
    Copyright (C) 2014 TribeX

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
    //XXX: WINDOW OPERATIONS :XXX\\

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
