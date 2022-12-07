import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.util.Scanner;

public class View{
	private static String prefix;
    private static Controller controller;
	private static void recordInsert(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of record would you like to insert?\n"
				+ "----------------------------------------------\n"
				+ "1: Passenger\n"
				+ "2: Flight\n"
				+ "3: Go Back\n");
		String input = scanner.nextLine();
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			System.out.println("Invalid input - please input a number from 1-3.");
			input = scanner.nextLine();
		}
		if (input.equals("1")) {
			// TODO: Insert passenger via controller
		}
		else if (input.equals("2")) {
			// TODO: Insert flight via controller
		}
		
	}
	
	private static void recordDelete(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of record would you like to delete?\n"
				+ "----------------------------------------------\n"
				+ "1: Passenger\n"
				+ "2: Flight\n"
				+ "3: Go Back\n");
		String input = scanner.nextLine();
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			System.out.println("Invalid input - please input a number from 1-3.");
			input = scanner.nextLine();
		}
		if (input.equals("1")) {
			// TODO: Delete passenger via controller
		}
		else if (input.equals("2")) {
			// TODO: Delete flight via controller
		}
	}
	
	private static void recordUpdate(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of record would you like to update?\n"
				+ "----------------------------------------------\n"
				+ "1: Passenger\n"
				+ "2: Flight\n"
				+ "3: Go Back\n");
		String input = scanner.nextLine();
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			System.out.println("Invalid input - please input a number from 1-3.");
			input = scanner.nextLine();
		}
		if (input.equals("1")) {
			// Update passenger
			while (true){
			System.out.println("\n----------------------------------------------\n"
					+ "Please provide Passenger ID to update or type 'exit' to leave\n");
			String idToUpdateString = scanner.nextLine().strip();
			if ("exit".equals(idToUpdateString))
				break;
			Integer idToUpdate = 0;
			try{
				idToUpdate = Integer.valueOf(idToUpdateString);
			}catch(Exception e){
				System.out.println("Invalid ID. Please enter a numeric value\n");
				continue;
			}

			System.out.println("\n----------------------------------------------\n"
					+ "Please provide following updated values\n");
			System.out.println("Name: ");
			String name = scanner.nextLine().strip();
			if (name.length() == 0){
				System.out.println("Name cannot be empty. Try again.\n");
				continue;
			}

			System.out.println();
			boolean frequent = false, student = false, third = false;
			System.out.println("Frequent Flier? (y/n): ");
			String temp = scanner.nextLine().strip();
			if ("y".equals(temp)){
				frequent = true;
			}
			System.out.println();

			System.out.println("Student?: ");
			temp = scanner.nextLine().strip();
			if ("y".equals(temp)){
				student = true;
			}
			System.out.println();

			System.out.println("Third Category?: ");
			temp = scanner.nextLine().strip();
			if ("y".equals(temp)){
				third = true;
			}
			System.out.println();

			try{
				Controller.updatePassenger(idToUpdate, name, frequent, 
				student, third);
			} catch(Exception e){
				System.out.println("There was an error while updating the table. "
				+ "Please see the error below and try again\n");
				System.out.println("Error:\n" + e.getMessage()+"\n");
				continue;
			}
			System.out.println("Success\n");
			break;
		}
		}
		else if (input.equals("2")) {
			// Update flight
			while(true){
			System.out.println("\n----------------------------------------------\n"
					+ "Please provide Flight ID to update or type 'exit' to leave\n");
			String idToUpdateString = scanner.nextLine().strip();
			if ("exit".equals(idToUpdateString))
				break;
			Integer idToUpdate = 0;
			try{
				idToUpdate = Integer.valueOf(idToUpdateString);
			}catch(Exception e){
				System.out.println("Invalid ID. Please enter a numeric value\n");
				continue;
			}
			System.out.println("\n----------------------------------------------\n"
					+ "Please provide following updated values\n");
			System.out.println("Airline Name: ");
			String name = scanner.nextLine().strip();
			if (name.length() == 0){
				System.out.println("Name cannot be empty. Try again.\n");
				continue;
			}
			System.out.println();

			System.out.println("Boarding Gate: ");
			String gate = scanner.nextLine().strip();
			if (gate.length() == 0){
				System.out.println("Gate cannot be empty. Try again.\n");
				continue;
			}

			System.out.println();
			System.out.println("Flight Date (yyyy-mm-dd): ");
			String[] dateString = scanner.nextLine().strip().split("-");
			Date date = null;
			Calendar calendarDate = null;
			if (dateString.length != 3){
				System.out.println("Invalid date. Try again.\n");
				continue;
			}
			else{
				try{
					Calendar c = Calendar.getInstance();
					c.set(Integer.valueOf(dateString[0]), Integer.valueOf(dateString[1]), Integer.valueOf(dateString[2]));
					date = new Date(c.getTimeInMillis());
					calendarDate = c;
				}catch(Exception e){
					System.out.println("Invalid date. Try again.\n");
					continue;
				}
			}
			System.out.println();

			Time boardingTime = null;
			System.out.println("Boarding Time (HHMM): ");
			String boardingTimeString = scanner.nextLine().strip();
			if (boardingTimeString.length() != 4){
				System.out.println("Invalid time. Try again.\n");
				continue;
			}
			else{
				try{
					calendarDate.set(Calendar.HOUR_OF_DAY, Integer.valueOf(boardingTimeString.substring(0,3)));
					calendarDate.set(Calendar.MINUTE, Integer.valueOf(boardingTimeString.substring(3,5)));
					boardingTime = new Time(calendarDate.getTimeInMillis());
				}catch (Exception e){
					System.out.println("Invalid time. Try again.\n");
					continue;
				}
			}
			System.out.println();

			Time departingTime = null;
			System.out.println("Departing Time (HHMM): ");
			String departingTimeString = scanner.nextLine().strip();
			if (departingTimeString.length() != 4){
				System.out.println("Invalid time. Try again.\n");
				continue;
			}
			else{
				try{
					calendarDate.set(Calendar.HOUR_OF_DAY, Integer.valueOf(departingTimeString.substring(0,3)));
					calendarDate.set(Calendar.MINUTE, Integer.valueOf(departingTimeString.substring(3,5)));
					departingTime = new Time(calendarDate.getTimeInMillis());
				}catch (Exception e){
					System.out.println("Invalid time. Try again.\n");
					continue;
				}
			}
			System.out.println();

			System.out.println("Time Interval (in minutes): ");
			String intervalString = scanner.nextLine().strip();
			Integer interval = 0;
			try{
				interval = Integer.valueOf(intervalString);
			}catch(Exception e){
				System.out.println("Invalid interval. Please enter a numeric value\n");
				continue;
			}
			System.out.println();

			System.out.println("Airport ID Of Departure : ");
			String airportFromString = scanner.nextLine().strip();
			Integer airportFrom = 0;
			try{
				airportFrom = Integer.valueOf(airportFromString);
			}catch(Exception e){
				System.out.println("Invalid ID. Please enter a numeric value\n");
				continue;
			}
			System.out.println();

			System.out.println("Airport ID Of Arrival : ");
			String airportToString = scanner.nextLine().strip();
			Integer airportTo = 0;
			try{
				airportTo = Integer.valueOf(airportToString);
			}catch(Exception e){
				System.out.println("Invalid ID. Please enter a numeric value\n");
				continue;
			}
			System.out.println();

			try{
				Controller.updateFlight(idToUpdate, name, gate, date, boardingTime.toLocalTime(),
				departingTime.toLocalTime(), interval, airportFrom, airportTo);
			} catch(Exception e){
				System.out.println("There was an error while updating the table. "
				+ "Please see the error below and try again\n");
				System.out.println("Error:\n" + e.getMessage()+"\n");
			}
			break;
		}
		}
	}
	
	private static void query(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of query would you like to do?\n"
				+ "----------------------------------------------\n"
				+ "1: Display list of distinct passenger names who took flights from all \n"
				+ "four airlines in 2021.\n\n"
				+ "2: For any airline entered by the user, print the list of passengers, \n"
				+ "with the number of checked–in bags. Sort the list in ascending order of \n"
				+ "the number of checked–in bags. Display the output grouped by flights for \n"
				+ "a particular date in March (input by the user).\n\n"
				+ "3: Print the schedule of flights for a given date in June (input by the \n"
				+ "user). The schedule should contain the flight number, gate number, name \n"
				+ "of the airline of that flight, boarding time, departing time, duration of \n"
				+ "flight, route of the flight (i.e. origin for arriving flights and destination \n"
				+ "for the departing flights). Sort the schedule in ascending order of the \n"
				+ "boarding time.\n\n"
				+ "4: Print the list for the three categories of passengers (Student, Frequent \n"
				+ "Flyer, Category of your choice) for each of the following queries for a \n"
				+ "particular airline (of your choice) who: Traveled only once in the month \n"
				+ "of March, Traveled with exactly one checked in bag anytime in the months \n"
				+ "of June and July, Ordered snacks/beverages on at least on one flight\n\n"
				+ "5: Custom Query\n\n"
				+ "6: Go Back");
		String input = scanner.nextLine();
		while (!(Integer.valueOf(input) > 0 && Integer.valueOf(input) < 7)) {
			System.out.println("Invalid input - please input a number from 1-6.");
			input = scanner.nextLine();
		}
		switch (input) {
			case "1":
				Controller.executeQueryOne(prefix);
				break;
			case "2":
				String date = "";
				String airline = "";
				
				System.out.println("Please enter a day in March to check: ");
				date = scanner.nextLine();
				while (!(Integer.valueOf(date) > 0 && Integer.valueOf(input) < 32)) {
					System.out.println("Invalid input - please input a number from 1-31.");
					date = scanner.nextLine();
				}
				System.out.println("Please enter an airline to check: ");
				airline = scanner.nextLine();
				Controller.executeQueryTwo(prefix, date, airline);
				break;
			case "3":
				date = "";
				
				System.out.println("Please enter a day in June to check: ");
				date = scanner.nextLine();
				while (!(Integer.valueOf(date) > 0 && Integer.valueOf(input) < 31)) {
					System.out.println("Invalid input - please input a number from 1-30.");
					date = scanner.nextLine();
				}
				Controller.executeQueryThree(prefix, date);
				break;
			case "4":
				Controller.executeQueryFour(prefix);
				break;
			case "5":
				
				break;
			case "6":
				break;
		}
	}
	
	private static void prompt() {
		System.out.println("\n------------------------\n"
				+ "Please choose an option.\n"
				+ "------------------------\n"
				+ "1: Insert a record\n"
				+ "2: Delete a record\n"
				+ "3: Update a record\n"
				+ "4: Query the DB\n"
				+ "5: Quit\n");
		
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (!input.equals("5")) {
			if (!(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4"))) {
				System.out.println("Invalid input - please input a number from 1-5.");
			}
			else {
				switch (input) {
					case "1":
						recordInsert(scanner);
						break;
					case "2":
						recordDelete(scanner);
						break;
					case "3":
						recordUpdate(scanner);
						break;
					case "4":
						query(scanner);
						break;
				}
				System.out.println("\n------------------------\n"
						+ "Please choose an option.\n"
						+ "------------------------\n"
						+ "1: Insert a record\n"
						+ "2: Delete a record\n"
						+ "3: Update a record\n"
						+ "4: Query the DB\n"
						+ "5: Quit");
			}
			input = scanner.nextLine();
		}

	}
	
    public static void main(String[] args){
    	if (args.length != 2) {    // get username/password from cmd line args
		    System.out.println("\nUsage:  java JDBC <username> <password>\n"
		                     + "    where <username> is your Oracle DBMS"
		                     + " username,\n    and <password> is your Oracle"
		                     + " password (not your system password).");
		    System.exit(-1);
		}
    	
    	System.out.println("Welcome to our CSC460 JDBC interface!");
    	prefix = "chasehult"; //TODO: Add correct database prefix
    	controller = new Controller();
		Controller.login(args[0], args[1]);
    	prompt();
        Controller.close();
    }
}