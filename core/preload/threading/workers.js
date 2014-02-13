/**
 * @file Provides a basic worker thread.
 * @author Tribex
 */

/**
 * A basic worker thread. Run using worker.start().
 * @param func {string} The function to run (as a string).
 * @returns {Sys.Worker} A worker object.
 */
function Worker(func) {
    return new Sys.Worker(func);
}
