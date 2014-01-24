/*
Provides methods for finding the current date and time
For sorting purposes larger time periods are at the top,
while small time periods (seconds) are at the bottom.
*/

//import Calendar
require(Packages.java.util.Calendar);

//the top level variable
var time = {};


/*
Returns the current year
*/
time.year = function () {
	return Calendar.getInstance().get(Calendar.YEAR);
}

/*
Returns the current month of the current year
*/
time.month = function () {
	return Calendar.getInstance().get(Calendar.MONTH);
}

/*
Returns the current date of the current month (0-11)
*/
time.date = function () {
	return Calendar.getInstance().get(Calendar.DATE);
}

/*
Returns the name of the current month, such as "January"
*/
time.monthName = function () {
	switch(time.month()){
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

/*
Returns the current day of the current week (1-7)
*/
time.dayOfWeek = function () {
	return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
}

/*
Returns the name of the day of the week, such as "Monday"
*/
time.dayOfWeekName = function () {
	//return is as good as break
	switch(time.dayOfWeek()){
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

/*
Returns true if it is am, false if it is pm
*/
time.isAM = function () {
	//XXX bug here!
	if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12){
		return true;
	}
	else{
		return false;
	}
}

/*
Returns the hour in 12 hour format
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

/*
Returns the hour in 24 hour format
*/
time.hour24 = function () {
	return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
}

/*
Returns the number of minutes past the current hour
*/
time.minute = function (){
	return Calendar.getInstance().get(Calendar.MINUTE);
}

/*
Returns the number of seconds past the current minute
*/
time.second = function (){
	return Calendar.getInstance().get(Calendar.SECOND);
}

/*
Returns the number of milliseconds past the current second
*/
time.millisecond = function (){
	return Calendar.getInstance().get(Calendar.MILLISECOND);
}

//Leap-year stuff below

/*
Returns if the current year is a leap year
*/
time.isLeapYear = function (){
	//ask about the current year
	return time.isLeapYear(time.year());
}	

/*
Returns if the specified year is a leap year
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
