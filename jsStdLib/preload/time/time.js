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
Returns true if it is am, false if it is pm -FLAWED @Nateowami
*/
time.isAM = function () {
	//XXX bug here!
	if(Calendar.getInstance().get(Calendar.HOUR) < 12){
		return true;
	}
	else{
		return false;
	}
}

/*
Returns the hour in 12 hour format -FLAWED @Nateowami?
*/
time.hours12 = function () {
	var hours = Calendar.getInstance().get(Calendar.HOUR);
	//convert to 12-hour format
	hours %= 12;
	if(hours == 0){
		//in the 12-hour format 0 is shown as 12
		hours = 12;
	}
	return hours;
}

/*
Returns the hour in 24 hour format -FLAWED @Nateowami
*/
time.hours24 = function () {
	return Calendar.getInstance().get(Calendar.HOUR);
}



