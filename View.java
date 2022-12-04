import java.util.Scanner;

public class View{
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
		// TODO: Add query descriptions and call queries via controller
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
    	System.out.println("Welcome to our CSC460 JDBC interface!");
    	prompt();
    }
}