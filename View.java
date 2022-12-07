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
			// TODO: Update passenger via controller
		}
		else if (input.equals("2")) {
			// TODO: Update flight via controller
		}
	}
	
	private static void query(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of record would you like to update?\n"
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
				controller.executeQueryOne(prefix);
				break;
			case "2":
				String date = "";
				String airline = "";
				
				System.out.println("Please enter a day in June to check: ");
				date = scanner.nextLine();
				while (!(Integer.valueOf(date) > 0 && Integer.valueOf(input) < 32)) {
					System.out.println("Invalid input - please input a number from 1-31.");
					date = scanner.nextLine();
				}
				System.out.println("Please enter an airline to check: ");
				airline = scanner.nextLine();
				controller.executeQueryTwo(prefix, date, airline);
				break;
			case "3":
				date = "";
				
				System.out.println("Please enter a day in March to check: ");
				date = scanner.nextLine();
				while (!(Integer.valueOf(date) > 0 && Integer.valueOf(input) < 31)) {
					System.out.println("Invalid input - please input a number from 1-30.");
					date = scanner.nextLine();
				}
				controller.executeQueryThree(prefix, date);
				break;
			case "4":
				controller.executeQueryFour(prefix);
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
    	controller = new Controller(args[0], args[1]);
    	prompt();
        controller.close();
    }
}