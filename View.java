/*=============================================================================
 |   Assignment:  Program #4:
 |		 	File: View.java
 |       Authors: Chase Hult
 |				  Rebekah Julicher
 |				  Felipe Lopez
 |				  Davlat Uralov
 |       Grader:  L. McCann
 |
 |       Course:  CSC 460
 |   Instructor:  L. McCann
 |     Due Date:  12/05/2022 3:30 PM
 |
 |  Description:  This program is the view of our system. The view prompts the user with console prompts. The view then 
 |				  collects the user input to determine what methods in the controller to call. The calls to the controller
 |				  then prints the appropriate output based on the input of the user.
 |				  
 |
 |     Language:  Java (JDK 1.6)
 |     Packages:  java.sql.*
 |				  java.util*
 |			  	  java.time.LocalTime
 |				  java.text.SimpleDateFormat
 |                
 | Deficiencies:  We are not aware of unsatisfied requirements or logical errors.
 *===========================================================================*/

import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Scanner;


/*+----------------------------------------------------------------------
||
||  Class View
||
||       Authors: Chase Hult
||				  Rebekah Julicher
||				  Felipe Lopez
||				  Davlat Uralov
||		  
||    Inspired By: Professor L. McCann
||
||        Purpose:  This class has a main function which creates a controller to establish a connection to the database and
||					runs a loop that will read in user input to interact with the database through the controller based on the
||					user input.
||
||  Inherits From:  None
||
||     Interfaces:  None
||
|+-----------------------------------------------------------------------
||
||      Constants: None
||
|+-----------------------------------------------------------------------
||
||   Constructors:  The default constructor, no arguments.
||
||  Class Methods:  None.
||
||  Inst. Methods:  boolean isNumeric(String strNum)
||                  void insertFlight(Scanner scanner)
||                  void insertPassenger(Scanner scanner)
||                  void deletePassenger(Scanner scanner)
||                  void deleteFlight(Scanner scanner)
||                  void recordInsert(Scanner scanner)
||					void recordDelete(Scanner scanner)
||					void recordUpdate(Scanner scanner)
||					void query(Scanner scanner)
||                  void prompt()
||					void main(String[] args)
||                  
++-----------------------------------------------------------------------*/
public class View{
	private static String prefix;
    private static Controller controller;

    /*---------------------------------------------------------------------
    |  Method isNumeric(strNum)
    |
    |  Purpose:  Checks if the string passed is a number for data integrity purposes.
    |
    |  Pre-condition:  None
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      strNum - The string to check
    |
    |  Returns:  Boolean that indicates if string is a number.
    *-------------------------------------------------------------------*/
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Double.parseDouble(strNum);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

    /*---------------------------------------------------------------------
    |  Method insertFlight(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the insertion of flight, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void insertFlight(Scanner scanner) {
		System.out.println("Enter a ID for the flight.");
		String flightIDString = scanner.nextLine();
		while (!(isNumeric(flightIDString))) {
			System.out.println("Invalid input - please input a number from 1-3.");
			flightIDString = scanner.nextLine();
		}
		int flightID = Integer.valueOf(flightIDString);

		System.out.println("Enter a name for the airline.");
		String airlineName = scanner.nextLine();
		while (airlineName.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			airlineName = scanner.nextLine();
		}

		System.out.println("Enter a boarding gate. EX: A1");
		String boardingGate = scanner.nextLine();
		while (boardingGate.length() == 0|| !boardingGate.matches("[a-zA-Z][0-9]")) {
			System.out.println("Invalid input - please enter a valid Gate.");
			boardingGate = scanner.nextLine();
		}

		System.out.println("Enter a flight date. Format: MM-dd-yyyy");
		String flightDate = scanner.nextLine();
		while (flightDate.length() == 0 || !flightDate.matches("[0-1][0-9]-[0-3][0-9]-[0-9][0-9][0-9][0-9]")) {
			System.out.println("Invalid input - please enter a valid Date.");
			flightDate = scanner.nextLine();
		}

		java.sql.Date sqlFlightDate = null;
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
			java.util.Date date1 = sdf1.parse(flightDate);
			sqlFlightDate = new java.sql.Date(date1.getTime()); 
		} catch (Exception e) {
			System.out.println("Invalid Date");
			System.out.println(e);
			return;
		}

		System.out.println("Enter a boardingTime in format HH:MM");
		String boardingTime = scanner.nextLine();
		while (boardingTime.length() == 0 || !boardingTime.matches("[0-1][0-9]:[0-6][0-9]")) {
			System.out.println("Invalid input - please enter a valid boarding time.");
			boardingTime = scanner.nextLine();
		}
		LocalTime sqlBoardingTime = LocalTime.parse(boardingTime);

		System.out.println("Enter a departingTime in format HH:MM");
		String departingTime = scanner.nextLine();
		while (departingTime.length() == 0 || !departingTime.matches("[0-1][0-9]:[0-6][0-9]")) {
			System.out.println("Invalid input - please enter a valid departingTime.");
			departingTime = scanner.nextLine();
		}
		LocalTime sqlDepartingTime = LocalTime.parse(departingTime);

		System.out.println("Enter an interval for the flight.");
		String intervalString = scanner.nextLine();
		while (intervalString.length() == 0 || !isNumeric(intervalString)) {
			System.out.println("Invalid input - please enter a valid airport from ID.");
			intervalString = scanner.nextLine();
		}
		int interval = Integer.valueOf(intervalString);

		System.out.println("Enter an ID for the airport from for the flight.");
		String airportFromString = scanner.nextLine();
		while (airportFromString.length() == 0 || !isNumeric(airportFromString)) {
			System.out.println("Invalid input - please enter a valid airport from ID.");
			airportFromString = scanner.nextLine();
		}
		int airportFrom = Integer.valueOf(airportFromString);

		System.out.println("Enter an ID for the airport to for the flight.");
		String airportToString = scanner.nextLine();
		while (airportToString.length() == 0 || !isNumeric(airportToString)) {
			System.out.println("Invalid input - please enter a valid airport to ID.");
			airportToString = scanner.nextLine();
		}
		int airportTo = Integer.valueOf(airportToString);
		try {
			Controller.insertFlight(flightID, airlineName, boardingGate, sqlFlightDate, sqlBoardingTime, sqlDepartingTime, interval, airportFrom, airportTo);
		} catch (SQLException e) {
			System.out.println("Insertion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

    /*---------------------------------------------------------------------
    |  Method insertPassenger(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the insertion of passenger, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void insertPassenger(Scanner scanner) {
		System.out.println("Enter a ID for the passenger.");
		String passengerIDString = scanner.nextLine();
		while (!(isNumeric(passengerIDString))) {
			System.out.println("Invalid input - please input a number.");
			passengerIDString = scanner.nextLine();
		}
		int passengerID = Integer.valueOf(passengerIDString);

		System.out.println("Enter a name for the passenger.");
		String passengerName = scanner.nextLine();
		while (passengerName.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			passengerName = scanner.nextLine();
		}

		System.out.println("Is the passenger a frequent flier. Type 1 if yes, 0 if no.");
		String frequentFlierString = scanner.nextLine();
		while (frequentFlierString.length() != 1 || !isNumeric(frequentFlierString)) {
			System.out.println("Invalid input - please enter 1 or 0.");
			frequentFlierString = scanner.nextLine();
		}
		boolean frequentFlier = false;
		if (!frequentFlierString.equals("0")) {
			frequentFlier = true;
		}

		System.out.println("Is the passenger a student. Type 1 if yes, 0 if no.");
		String studentString = scanner.nextLine();
		while (studentString.length() != 1 || !isNumeric(studentString)) {
			System.out.println("Invalid input - please enter 1 or 0.");
			studentString = scanner.nextLine();
		}
		boolean student = false;
		if (!studentString.equals("0")) {
			student = true;
		}

		System.out.println("Is the passenger a minor. Type 1 if yes, 0 if no.");
		String minorString = scanner.nextLine();
		while (minorString.length() != 1 || !isNumeric(minorString)) {
			System.out.println("Invalid input - please enter 1 or 0.");
			minorString = scanner.nextLine();
		}
		boolean minor = false;
		if (minorString != "0") {
			minor = true;
		}

		try {
			Controller.insertPassenger(passengerID, passengerName, frequentFlier, student, minor);
		} catch (SQLException e) {
			System.out.println("Insertion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method insertBoardingGate(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the insertion of boarding gate, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void insertBoardingGate(Scanner scanner) {
		System.out.println("Enter a ID for the gate.");
		String boardingGateIDString = scanner.nextLine();
		while (!(isNumeric(boardingGateIDString))) {
			System.out.println("Invalid input - please input a number.");
			boardingGateIDString = scanner.nextLine();
		}
		int boardingGateID = Integer.valueOf(boardingGateIDString);

		System.out.println("Enter a ID of the airport for the gate.");
		String airportIDString = scanner.nextLine();
		while (!(isNumeric(airportIDString))) {
			System.out.println("Invalid input - please input a number.");
			airportIDString = scanner.nextLine();
		}
		int airportID = Integer.valueOf(airportIDString);

		try {
			Controller.insertBoardingGate(boardingGateID, airportID);
		} catch (SQLException e) {
			System.out.println("Insertion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method updateBoardingGate(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the insertion of boarding gate, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void updateBoardingGate(Scanner scanner) {
		System.out.println("Enter the ID for the gate to be update.");
		String boardingGateIDString = scanner.nextLine();
		while (!(isNumeric(boardingGateIDString))) {
			System.out.println("Invalid input - please input a number.");
			boardingGateIDString = scanner.nextLine();
		}
		int boardingGateID = Integer.valueOf(boardingGateIDString);

		System.out.println("Enter a the updated ID of the airport for the gate.");
		String airportIDString = scanner.nextLine();
		while (!(isNumeric(airportIDString))) {
			System.out.println("Invalid input - please input a number.");
			airportIDString = scanner.nextLine();
		}
		int airportID = Integer.valueOf(airportIDString);

		try {
			Controller.updateBoardingGate(boardingGateID, airportID);
		} catch (SQLException e) {
			System.out.println("Update failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method deleteBoardingGate(scanner)
    |
    |  Purpose:  Prompts the user with the boarding gate ID for the gate to be deleted, the
	|			 controller is then called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void deleteBoardingGate(Scanner scanner) {
		System.out.println("Enter a ID for the gate to be deleted.");
		String boardingGateIDString = scanner.nextLine();
		while (!(isNumeric(boardingGateIDString))) {
			System.out.println("Invalid input - please input a number.");
			boardingGateIDString = scanner.nextLine();
		}
		int boardingGateID = Integer.valueOf(boardingGateIDString);
		
		try {
			Controller.deleteBoardingGate(boardingGateID);
		} catch (SQLException e) {
			System.out.println("Deletion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method insertStaff(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the insertion of staff member, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void insertStaff(Scanner scanner) {
		System.out.println("Enter a ID for the employee.");
		String employeeIDString = scanner.nextLine();
		while (!(isNumeric(employeeIDString))) {
			System.out.println("Invalid input - please input a number.");
			employeeIDString = scanner.nextLine();
		}
		int employeeID = Integer.valueOf(employeeIDString);

		System.out.println("Enter a name for the passenger.");
		String employeeName = scanner.nextLine();
		while (employeeName.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			employeeName = scanner.nextLine();
		}

		System.out.println("Enter the job of the employee.");
		String employeeJob = scanner.nextLine();
		while (employeeJob.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			employeeJob = scanner.nextLine();
		}

		System.out.println("Enter the airline the employee is employed by.");
		String employeeAirline = scanner.nextLine();
		while (employeeAirline.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			employeeAirline = scanner.nextLine();
		}

		try {
			Controller.insertStaff(employeeID, employeeName, employeeJob, employeeAirline);
		} catch (SQLException e) {
			System.out.println("Insertion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method insertPassengerFlight(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the insertion of passengerFlight member, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void insertPassengerFlight(Scanner scanner) {
		System.out.println("Enter a ID of the passenger.");
		String passengerIDString = scanner.nextLine();
		while (!(isNumeric(passengerIDString))) {
			System.out.println("Invalid input - please input a number.");
			passengerIDString = scanner.nextLine();
		}
		int passengerID = Integer.valueOf(passengerIDString);

		System.out.println("Enter a ID of the flight.");
		String flightIDString = scanner.nextLine();
		while (!(isNumeric(flightIDString))) {
			System.out.println("Invalid input - please input a number.");
			flightIDString = scanner.nextLine();
		}
		int flightID = Integer.valueOf(flightIDString);

		System.out.println("Enter a the number of bags checked.");
		String bagsCheckedString = scanner.nextLine();
		while (!(isNumeric(bagsCheckedString))) {
			System.out.println("Invalid input - please input a number.");
			bagsCheckedString = scanner.nextLine();
		}
		int bagsChecked = Integer.valueOf(bagsCheckedString);

		System.out.println("Enter 1 if ordered snacks, 0 otherwise.");
		String orderedSnacksString = scanner.nextLine();
		while (orderedSnacksString.length() != 1 || !isNumeric(orderedSnacksString)) {
			System.out.println("Invalid input - please enter 1 or 0.");
			orderedSnacksString = scanner.nextLine();
		}
		boolean orderedSnacks = false;
		if (!orderedSnacksString.equals("0")) {
			orderedSnacks = true;
		}

		try {
			Controller.insertPassengerFlight(passengerID, flightID, bagsChecked, orderedSnacks);
		} catch (SQLException e) {
			System.out.println("Insertion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method updatePassengerFlight(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the update of passengerFlight member, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void updatePassengerFlight(Scanner scanner) {
		System.out.println("Enter a ID of the passenger to be updated.");
		String passengerIDString = scanner.nextLine();
		while (!(isNumeric(passengerIDString))) {
			System.out.println("Invalid input - please input a number.");
			passengerIDString = scanner.nextLine();
		}
		int passengerID = Integer.valueOf(passengerIDString);

		System.out.println("Enter a ID of the flight of passenger to be updated.");
		String flightIDString = scanner.nextLine();
		while (!(isNumeric(flightIDString))) {
			System.out.println("Invalid input - please input a number.");
			flightIDString = scanner.nextLine();
		}
		int flightID = Integer.valueOf(flightIDString);

		System.out.println("Enter a the updated number of bags checked.");
		String bagsCheckedString = scanner.nextLine();
		while (!(isNumeric(bagsCheckedString))) {
			System.out.println("Invalid input - please input a number.");
			bagsCheckedString = scanner.nextLine();
		}
		int bagsChecked = Integer.valueOf(bagsCheckedString);

		System.out.println("Enter 1 if ordered snacks, 0 otherwise.");
		String orderedSnacksString = scanner.nextLine();
		while (orderedSnacksString.length() != 1 || !isNumeric(orderedSnacksString)) {
			System.out.println("Invalid input - please enter 1 or 0.");
			orderedSnacksString = scanner.nextLine();
		}
		boolean orderedSnacks = false;
		if (!orderedSnacksString.equals("0")) {
			orderedSnacks = true;
		}

		try {
			Controller.updatePassengerFlight(passengerID, flightID, bagsChecked, orderedSnacks);
		} catch (SQLException e) {
			System.out.println("Insertion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method deletePassengerFlight(scanner)
    |
    |  Purpose:  Prompts the user with the employeeID for the employee to be deleted, the
	|			 controller is then called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void deletePassengerFlight(Scanner scanner) {
		System.out.println("Enter a ID for the passenger that the flight will be deleted from.");
		String employeeIDString = scanner.nextLine();
		while (!(isNumeric(employeeIDString))) {
			System.out.println("Invalid input - please input a number.");
			employeeIDString = scanner.nextLine();
		}
		int employeeID = Integer.valueOf(employeeIDString);

		System.out.println("Enter a ID for the flight that will be deleted from the passenger.");
		String flightIDString = scanner.nextLine();
		while (!(isNumeric(flightIDString))) {
			System.out.println("Invalid input - please input a number.");
			flightIDString = scanner.nextLine();
		}
		int flightID = Integer.valueOf(flightIDString);
		
		try {
			Controller.deletePassengerFlight(employeeID, flightID);
		} catch (SQLException e) {
			System.out.println("Deletion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	

		/*---------------------------------------------------------------------
    |  Method updateStaff(scanner)
    |
    |  Purpose:  Prompts the user with multiple questions regarding the update of staff member, user input is 
	|			 collected with a scanner and the controller is called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void updateStaff(Scanner scanner) {
		System.out.println("Enter the ID for the employee to be updated.");
		String employeeIDString = scanner.nextLine();
		while (!(isNumeric(employeeIDString))) {
			System.out.println("Invalid input - please input a number.");
			employeeIDString = scanner.nextLine();
		}
		int employeeID = Integer.valueOf(employeeIDString);

		System.out.println("Enter an updated name for the passenger.");
		String employeeName = scanner.nextLine();
		while (employeeName.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			employeeName = scanner.nextLine();
		}

		System.out.println("Enter an updated job of the employee.");
		String employeeJob = scanner.nextLine();
		while (employeeJob.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			employeeJob = scanner.nextLine();
		}

		System.out.println("Enter the updated airline the employee is employed by.");
		String employeeAirline = scanner.nextLine();
		while (employeeAirline.length() == 0) {
			System.out.println("Invalid input - please enter a valid name.");
			employeeAirline = scanner.nextLine();
		}

		try {
			Controller.updateStaff(employeeID, employeeName, employeeJob, employeeAirline);
		} catch (SQLException e) {
			System.out.println("Update failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

	/*---------------------------------------------------------------------
    |  Method deleteStaff(scanner)
    |
    |  Purpose:  Prompts the user with the employeeID for the employee to be deleted, the
	|			 controller is then called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void deleteStaff(Scanner scanner) {
		System.out.println("Enter a ID for the employee to be deleted.");
		String employeeIDString = scanner.nextLine();
		while (!(isNumeric(employeeIDString))) {
			System.out.println("Invalid input - please input a number.");
			employeeIDString = scanner.nextLine();
		}
		int employeeID = Integer.valueOf(employeeIDString);
		
		try {
			Controller.deleteStaff(employeeID);
		} catch (SQLException e) {
			System.out.println("Deletion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

    /*---------------------------------------------------------------------
    |  Method deletePassenger(scanner)
    |
    |  Purpose:  Prompts the user with the passengerID for the passenger to be deleted, the
	|			 controller is then called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void deletePassenger(Scanner scanner) {
		System.out.println("Enter a ID for the passenger to be deleted.");
		String passengerIDString = scanner.nextLine();
		while (!(isNumeric(passengerIDString))) {
			System.out.println("Invalid input - please input a number.");
			passengerIDString = scanner.nextLine();
		}
		int passengerID = Integer.valueOf(passengerIDString);
		
		try {
			Controller.deletePassenger(passengerID);
		} catch (SQLException e) {
			System.out.println("Deletion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

    /*---------------------------------------------------------------------
    |  Method deleteFlight(scanner)
    |
    |  Purpose:  Prompts the user with the flight to be deleted, the
	|			 controller is then called accordingly.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void deleteFlight(Scanner scanner) {
		System.out.println("Enter a ID for the passenger to be deleted.");
		String flightIDString = scanner.nextLine();
		while (!(isNumeric(flightIDString))) {
			System.out.println("Invalid input - please input a number.");
			flightIDString = scanner.nextLine();
		}
		int flightID = Integer.valueOf(flightIDString);
		
		try {
			Controller.deletePassenger(flightID);
		} catch (SQLException e) {
			System.out.println("Deletion failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}

    /*---------------------------------------------------------------------
    |  Method deleteFlight(scanner)
    |
    |  Purpose:  Prompts the user with options on what table the user wants to insert data to.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void recordInsert(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of record would you like to insert?\n"
				+ "----------------------------------------------\n"
				+ "1: Passenger\n"
				+ "2: Flight\n"
				+ "3: Staff\n"
				+ "4: Boarding Gate\n"
				+ "5: Passenger Flight\n"
				+ "6: Go Back\n");
		String input = scanner.nextLine();
		while (!(input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5"))) {
			System.out.println("Invalid input - please input a number from 1-5.");
			input = scanner.nextLine();
		}
		if (input.equals("1")) {
			insertPassenger(scanner);
		}
		else if (input.equals("2")) {
			insertFlight(scanner);
		}
		else if (input.equals("3")) {
			insertStaff(scanner);
		}
		else if (input.equals("4")) {
			insertBoardingGate(scanner);
		}
		else if (input.equals("5")) {
			insertPassengerFlight(scanner);
		}
		
	}
	
	/*---------------------------------------------------------------------
    |  Method recordDelete(scanner)
    |
    |  Purpose:  Prompts the user with options on what table the user wants to delete data from.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void recordDelete(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of record would you like to delete?\n"
				+ "----------------------------------------------\n"
				+ "1: Passenger\n"
				+ "2: Flight\n"
				+ "3: Staff\n"
				+ "4: Boarding Gate\n"
				+ "5: Passenger Flight\n"
				+ "6: Go Back\n");
		String input = scanner.nextLine();
		while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
			System.out.println("Invalid input - please input a number from 1-3.");
			input = scanner.nextLine();
		}
		if (input.equals("1")) {
			deletePassenger(scanner);
		}
		else if (input.equals("2")) {
			deleteFlight(scanner);
		}
		else if (input.equals("3")) {
			deleteStaff(scanner);
		}
		else if (input.equals("4")) {
			deleteBoardingGate(scanner);
		}
		else if (input.equals("5")) {
			deletePassengerFlight(scanner);
		}
	}
	
	/*---------------------------------------------------------------------
    |  Method recordUpdate(scanner)
    |
    |  Purpose:  Prompts the user with options on what table the user wants to update data from.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void recordUpdate(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which type of record would you like to update?\n"
				+ "----------------------------------------------\n"
				+ "1: Passenger\n"
				+ "2: Flight\n"
				+ "3: Staff\n"
				+ "4: Boarding Gate\n"
				+ "5: Passenger Flight\n"
				+ "6: Go Back\n");
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

			System.out.println("Minor?: ");
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
			System.out.println("Boarding Time (HH:MM): ");
			String boardingTimeString = scanner.nextLine().strip();
			if (boardingTimeString.length() != 5){
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
			System.out.println("Departing Time (HH:MM): ");
			String departingTimeString = scanner.nextLine().strip();
			if (departingTimeString.length() != 5){
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
		else if (input.equals("3")) {
			updateStaff(scanner);
		}
		else if (input.equals("4")) {
			updateBoardingGate(scanner);
		}
		else if (input.equals("5")) {
			updatePassengerFlight(scanner);
		}
	}
	
	/*---------------------------------------------------------------------
    |  Method query(scanner)
    |
    |  Purpose:  Prompts the user with an option of different queries to run on the database, the
	|			 user input then determines how we will call the controller.
    |
    |  Pre-condition:  Scanner is open and controller database connection is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
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
				+ "5: Given an airportID list all the staff member names and IDs that are involved with that\n"
				+ "airport based on the airlines that work in that airport. Display the\n"
				+ "employee_id and names of the staff members ordered by their names in\n"
				+ "alphabetical order\n\n"
				+ "6: Go Back");
		String input = scanner.nextLine();
		while (!isNumeric(input) || !(Integer.valueOf(input) > 0 && Integer.valueOf(input) < 7)) {
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
				while (!isNumeric(input) || !(Integer.valueOf(date) > 0 && Integer.valueOf(input) < 32)) {
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
				while (!isNumeric(input) || !(Integer.valueOf(date) > 0 && Integer.valueOf(input) < 31)) {
					System.out.println("Invalid input - please input a number from 1-30.");
					date = scanner.nextLine();
				}
				Controller.executeQueryThree(prefix, date);
				break;
			case "4":
				Controller.executeQueryFour(prefix);
				break;
			case "5":
				String airport = "";
				System.out.println("Please enter an Airport ID: ");
				airport = scanner.nextLine();
				while (!(isNumeric(airport))) {
					System.out.println("Invalid input - please enter a number ID.");
					airport = scanner.nextLine();
				}

				Controller.executeQueryFive(prefix, airport);
				break;
			case "6":
				break;
		}
	}

	/*---------------------------------------------------------------------
    |  Method showAll(scanner)
    |
    |  Purpose:  Prompts the user with an option of different data to list from
	|			 the database, the proper controller method is then called based on
	|			 user input.
    |
    |  Pre-condition:  Scanner object is open.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      scanner - The scanner to collect user input.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void showAll(Scanner scanner) {
		System.out.println("\n----------------------------------------------\n"
				+ "Which data would you like to see?\n"
				+ "----------------------------------------------\n"
				+ "1: List all passengers.\n\n"
				+ "2: List all flights.\n\n"
				+ "3: List a passengers History.\n\n"
				+ "4: Go Back");
		String input = scanner.nextLine();
		while (!isNumeric(input) || !(Integer.valueOf(input) > 0 && Integer.valueOf(input) < 7)) {
			System.out.println("Invalid input - please input a number from 1-6.");
			input = scanner.nextLine();
		}
		try {
			switch (input) {
				case "1":
					Controller.getPassengers();
					break;
				case "2":
					Controller.getFlights();
					break;
				case "3":
					String passengerID = "";
					
					System.out.println("Please enter a day in June to check: ");
					passengerID = scanner.nextLine();
					while (!isNumeric(input) || passengerID.length() == 0) {
						System.out.println("Invalid input - please enter a valid ID.");
						passengerID = scanner.nextLine();
					}
	
					Controller.getHistory(Integer.valueOf(passengerID));
					break;
				case "4":
					break;
			}
		} catch (SQLException e) {
			System.out.println("Update failed");
			System.out.println("Error:\n" + e.getMessage()+"\n");
		}
	}
	
	/*---------------------------------------------------------------------
    |  Method query(scanner)
    |
    |  Purpose:  Prompts the user with an option of different menus to explore, the
	|			 method representing the designated menu is then called.
    |
    |  Pre-condition:  None
    |
    |  Post-condition: Scanner object is open.
    |
    |  Parameters: None
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	private static void prompt() {
		System.out.println("\n------------------------\n"
				+ "Please choose an option.\n"
				+ "------------------------\n"
				+ "1: Insert a record\n"
				+ "2: Delete a record\n"
				+ "3: Update a record\n"
				+ "4: Query the DB\n"
				+ "5: List Data\n"
				+ "6: Quit\n");
		
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (!input.equals("5")) {
			if (!isNumeric(input) || !(0 < Integer.valueOf(input) && Integer.valueOf(input) < 6)) {
				System.out.println("Invalid input - please input a number from 1-6.");
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
					case "5":
						showAll(scanner);
						break;
				}
				System.out.println("\n------------------------\n"
						+ "Please choose an option.\n"
						+ "------------------------\n"
						+ "1: Insert a record\n"
						+ "2: Delete a record\n"
						+ "3: Update a record\n"
						+ "4: Query the DB\n"
						+ "5: List Data\n"
						+ "6: Quit");
			}
			input = scanner.nextLine();
		}

	}
	
	/*---------------------------------------------------------------------
    |  Method main(scanner)
    |
    |  Purpose:  The main function creates the controller, establishes the connection to the database
	|			 and calls prompt which allows the user to interact with the database. The database connection
	|			 is then closed before returning.
    |
    |  Pre-condition:  Connection to the database is not established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      args[0] - The username for authentication to the oracle database.
	|	   args[1] - The password for authentication to the oracle database.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
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