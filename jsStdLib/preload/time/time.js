/**
 * @file Provides methods for finding the current date and time
 * For sorting purposes larger time periods are at the top,
 * while small time periods (seconds) are at the bottom.
 * @author Nateowami
 */

//import Calendar
require(Packages.java.util.Calendar);

/**
 * Global time namespace.
 * @namespace
 */
var time = {};


/**
 * Gets the current year.
 * @returns {string} The current year.
 */
time.year = function () {
    return Calendar.getInstance().get(Calendar.YEAR);
}

/**
 * Gets the current month.
 * @returns {string} The current month of the current year.
 */
time.month = function () {
    return Calendar.getInstance().get(Calendar.MONTH);
}

/**
 * Gets the current date of the current month (0-11).
 * @returns {string} The current day of the current month, in number format.
 */
time.date = function () {
    return Calendar.getInstance().get(Calendar.DATE);
}

/**
 * Gets the name of the current month, such as "January".
 * @returns {string} The name of the current month.
 */
time.monthName = function () {
   return time.monthName(time.month());
}

/**
 * Gets the name of the specified month, such as "January".
 * @param month {integer} The month for which you want the name
 * @returns {string} The name of the current month.
 */
time.monthName = function (month) {
    switch(month){
    //return is as good as break
    case 0:
        return "January";
    case 1:
        return "February";
    case 2:
        return "March";
    case 3:
        return "April";
    case 4:
        return "May";
    case 5:
        return "June";
    case 6:
        return "July";
    case 7:
        return "August";
    case 8:
        return "September";
    case 9:
        return "October";
    case 10:
        return "November";
    case 11:
        "December";
    }
}

/**
 * Gets the current day of the current week (1-7)
 * @returns {string} The current day of the week.
 */
time.dayOfWeek = function () {
    return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
}

/**
 * Gets the name of the day of the week, such as "Monday".
 * @returns {string} The name of the current day of the week.
 */
time.dayOfWeekName = function () {
    return time.dayOfWeekName(time.dayOfWeek());
}

/**
 * Gets the name of the specified day of the week, such as "Monday".
 * @param day {integer} The day of the week for which you want the name
 * @returns {string} The name of the current day of the week.
 */
time.dayOfWeekName = function (day) {
    //return is as good as break
    switch(day){
    case 1:
        return "Sunday";
    case 2:
        return "Monday";
    case 3:
        return "Tuesday";
    case 4:
        return "Wednesday";
    case 5:
        return "Thursday";
    case 6:
        return "Friday";
    case 7:
        return "Saturday";
    }
}

/**
 * Whether or not the current time is AM.
 * @returns {boolean} True if it is currently AM, False if it is PM.
 */
time.isAM = function () {
    //XXX bug here!
    //XXX 24HourTime?
    if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12){
        return true;
    }
    else{
        return false;
    }
}

/**
 * Gets the hour in 12 hour format.
 * @returns {integer} Representing the current hour in 12 hour format.
 */
time.hour12 = function () {
    //gives us 0-11
    var hours = Calendar.getInstance().get(Calendar.HOUR);
    //call hour 0, 12
    if(hours == 0){
        //in the 12-hour format 0 is shown as 12
        hours = 12;
    }
    return hours;
}

/**
 * Gets the hour in 24 hour format.
 * @returns {integer} Representing the current hour in 24 hour format.
 */
time.hour24 = function () {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
}

/**
 * Gets the number of minutes past the current hour.
 * @returns {integer} The number of minutes past the current hour.
 */
time.minute = function (){
    return Calendar.getInstance().get(Calendar.MINUTE);
}

/**
 * Gets the number of seconds past the current minute.
 * @returns {integer} The number of seconds past the current minute.
 */
time.second = function (){
    return Calendar.getInstance().get(Calendar.SECOND);
}

/**
 * Gets the number of milliseconds past the current second.
 * @returns {integer} The number of milliseconds past the current second.
 */
time.millisecond = function (){
    return Calendar.getInstance().get(Calendar.MILLISECOND);
}

//Leap-year stuff below

/**
 * Whether or not the current year is a leap year
 * @returns True if the current year is a leap year, False if not.
 */
time.isLeapYear = function (){
    //ask about the current year
    return time.isLeapYear(time.year());
}

/**
 * Whether or not the specified year is a leap year
 * @param year {integer} The year to check.
 * @returns True if the specified year is a leap year, False if not.
 */
time.isLeapYear = function (year){
    var leap = false;
    //these are if's intentionally, not else if's
    //if it's divisible by four
    if(year % 4 == 0){
        leap = true;
    }
    //100
    if(year % 100 == 0){
        leap = false;
    }
    //400
    if(year % 400 == 0){
        leap = true;
    }
    return leap;
}

//misc time functions

/**
@return {long} The current time, expressed in milliseconds after January 1, 1970, 0:00:00 GMT.
*/
time.timeInMillis = function (){
	return Calendar.getInstance().getTimeInMillis()
}

/**
@return {integer} The current day of the year 
*/
time.dayOfYear = function (){
	return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
}

/**
@return {integer} The current week of the year. If the year does not start on Monday, the partial week is still counted.
*/
time.weekOfYear = function (){
	return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
}
