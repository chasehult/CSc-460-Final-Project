/*=============================================================================
 |   Assignment:  Program #4:  
 |		 	File: Controller.java
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
 |  Description:  This program is the controller of our system. The controller takes charge of communicating 
 |				  with our database and executing queries. The controller has multiple methods that query, update
 |			      delete and insert in the database. The controller must first run login to establish the connection to
 |				  the database before running any queries. The controller is not responsible of returning any values but
 |				  instead prints the result of the query that was ran.
 |				  
 |
 |     Language:  Java (JDK 1.6)
 |     Packages:  java.sql.*
 |			  	  java.time.LocalTime
 |                
 | Deficiencies:  We are not aware of unsatisfied requirements or logical errors.
 *===========================================================================*/

import java.sql.*;
import java.time.LocalTime;


/*+----------------------------------------------------------------------
||
||  Class Controller
||
||       Authors: Chase Hult
||				  Rebekah Julicher
||				  Felipe Lopez
||				  Davlat Uralov
||		  
||    Inspired By: Professor L. McCann
||
||        Purpose:  An object of this class can establish a connection to the database using the static login method.
||					we can then execute specific queries from the controller based on the methods provided. The controller
||					is responsible for communication with the oracle database.
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
||  Inst. Methods:  void login(String username, String password)
||                  void executeQueryOne(String prefix)
||                  void executeQueryTwo(String prefix, String date, String airline)
||                  void executeQueryThree(String prefix, String date)
||                  void executeQueryFour(String prefix)
||                  void executeQueryFive(String prefix, String airportID)
||					void insertFlight(int flightId, String airlineName, String boardingGate, Date flightDate,
||						LocalTime boardingTime, LocalTime departingTime, int intervalMinutes, int airportFrom, int airportTo)
||					static void updateFlight(int flightId, String airlineName, String boardingGate, Date flightDate,
||						LocalTime boardingTime, LocalTime departingTime, int intervalMinutes, int airportFrom, int airportTo)||
||					static void deleteFlight(int flightId)
||                  void insertPassenger(int passengerId, String name, boolean frequentFlier, boolean student, boolean minor)
||					void updatePassenger(int passengerId, String name, boolean frequentFlier, boolean student, boolean minor)
||					void deletePassenger(int passengerId)
||					void insertAirport(int airportId, String name, String airportCode, String location)
||					void updateAirport(int airportId, String name, String airportCode, String location)
||					void deleteAirport(int airportId)
||					void insertBoardingGate(int gateId, int airport)
||					void updateBoardingGate(int gateId, int airport)
||					void deleteBoardingGate(int gateId)
||					void insertStaff(int employeeId, String name, String job, String airport)
||					void updateStaff(int employeeId, String name, String job, String airport)
||					void deleteStaff(int employeeId)
||                  
++-----------------------------------------------------------------------*/
public class Controller {
	private static Connection dbconn;

    /*---------------------------------------------------------------------
    |  Method login(username, password)
    |
    |  Purpose:  Establishes the connection to the oracle database with the credentials
	|		     passed as parameters.
    |
    |  Pre-condition:  Their is no connection to the database.
    |
    |  Post-condition: A connection to the database is established.
    |
    |  Parameters:
    |      username - The username to be used in the oracle authentication.
	|	   password - The password to be used in the oracle authentication.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void login(String username, String password) {
		final String oracleURL = // Magic lectura -> aloe access spell
				"jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		// load the (Oracle) JDBC driver by initializing its base
		// class, 'oracle.jdbc.OracleDriver'.
		try {

			Class.forName("oracle.jdbc.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.err.println("*** ClassNotFoundException:  " + "Error loading Oracle JDBC driver.  \n"
					+ "\tPerhaps the driver is not on the Classpath?");
			System.exit(-1);

		}
		// make and return a database connection to the user's
		// Oracle database

		try {
			dbconn = DriverManager.getConnection(oracleURL, username, password);

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
	}

    /*---------------------------------------------------------------------
    |  Method executeQueryOne(prefix)
    |
    |  Purpose:  Queries the database to list all distinct passenger names, who took flights from
	|			 all four airlines in the year 2021.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      prefix - The prefix to the database tables being queried.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void executeQueryOne(String prefix) {
		try {
			Statement stmt = dbconn.createStatement(); // The statement to execute the query
			ResultSet answer = null; // The answer to the query

			// The query to execute to oracle
			String query = "SELECT DISTINCT name " + "FROM " + prefix + ".Passenger " + "JOIN " + prefix
					+ ".PassengerFlight USING (passenger_id) " + "JOIN " + prefix + ".Flight USING (flight_id) "
					+ "GROUP BY passenger_id, name " + "HAVING COUNT(DISTINCT airline) = 4";

			answer = stmt.executeQuery(query);

			if (answer != null) {

				System.out.println("\nThe results of the query are:\n");

				// Get the data about the query result to learn
				// the attribute names and use them as column headers

				ResultSetMetaData answermetadata = answer.getMetaData();

				for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
					System.out.print(answermetadata.getColumnName(i) + "\t");
				}
				System.out.println("\n-------------");

				// Use next() to advance cursor through the result
				// tuples and print their attribute values

				while (answer.next())
					System.out.println(answer.getString(1));
			}
			System.out.println("=============\n");

			stmt.close();

		} catch (SQLException e) {
			System.out.println("Could not execute query one due to some SQL exception.");
		}
	}
	
    /*---------------------------------------------------------------------
    |  Method executeQueryTwo(prefix, date, airline)
    |
    |  Purpose:  Queries the database to list the passengers with the number of checked-in bags sorted
	|			 in ascending order of the number of checked-in bags based on the airline passed as a parameter.
	|			 The output is grouped by flights for a particular date in march based on the date passed.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      prefix - The prefix to the database tables being queried.
	|	   date   - The date for the query to be grouped.
	|	   airline- The airline we are quering for.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void executeQueryTwo(String prefix, String date, String airline) {
		try {
			Statement stmt = dbconn.createStatement(); // The statement to execute the query
			ResultSet answer = null; // The answer to the query

			// The query to execute to oracle
			String query = "SELECT flight_id, name, bags_checked " + "FROM " + prefix + ".Passenger " + "NATURAL JOIN "
					+ prefix + ".PassengerFlight " + "NATURAL JOIN " + prefix + ".Flight "
					+ "WHERE flight_date = TO_DATE(" + date + " || '-MAR-21') AND airline = '" + airline
					+ "' ORDER BY flight_id, bags_checked";

			answer = stmt.executeQuery(query);

			if (answer != null) {

				System.out.println("\nThe results of the query are:\n");

				// Get the data about the query result to learn
				// the attribute names and use them as column headers

				ResultSetMetaData answermetadata = answer.getMetaData();

				for (int i = 2; i <= answermetadata.getColumnCount(); i++) {
					System.out.print(answermetadata.getColumnName(i) + "\t");
				}
				System.out.println("\n----------------------------------");

				// Use next() to advance cursor through the result
				// tuples and print their attribute values

				String currID = "";
				while (answer.next()) {
					String flight = answer.getString(1);
					if (!flight.equals(currID)) {
						currID = flight;
						System.out.println("\nFLIGHT #" + currID);
					}
					System.out.println(answer.getString(2) + "\t" + answer.getString(3));
				}
			}
			System.out.println("=================================\n");

			stmt.close();

		} catch (SQLException e) {
			System.out.println("Could not execute query two due to some SQL exception.");
		}
	}

    /*---------------------------------------------------------------------
    |  Method executeQueryThree(prefix, date)
    |
    |  Purpose:  Queries the database to list the scheduled flights for the date passed as a parameter in June. The
	|			 output is sorted in ascending order of the boarding time.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      prefix - The prefix to the database tables being queried.
	|	   date   - The day in june for the query.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void executeQueryThree(String prefix, String date) {
		try {
			Statement stmt = dbconn.createStatement(); // The statement to execute the query
			ResultSet answer = null; // The answer to the query

			// The query to execute to oracle
			String query = "SELECT DISTINCT flight_id, boarding_gate, airline, flight_date,"
					+ " boarding_time, departing_time, duration, apf.location AS \"from\"," + " apt.location AS \"to\""
					+ " FROM " + prefix + ".Flight" + " JOIN " + prefix + ".Airport apf ON (dest_from = apf.airport_id)"
					+ " JOIN " + prefix + ".Airport apt ON (dest_to = apt.airport_id)" + " WHERE flight_date = TO_DATE("
					+ date + " || '-JUN-21') ORDER BY boarding_time ASC";

			answer = stmt.executeQuery(query);

			if (answer != null) {

				System.out.println("\nThe results of the query are:\n");

				// Get the data about the query result to learn
				// the attribute names and use them as column headers

				ResultSetMetaData answermetadata = answer.getMetaData();

				for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
					System.out.print(answermetadata.getColumnName(i) + "\t");
					if (i == 3 || i == 8)
						System.out.print("\t");
					if (i == 8)
						System.out.print("\t");
				}
				System.out.println("\n---------------------------------------------------------");

				// Use next() to advance cursor through the result
				// tuples and print their attribute values

				while (answer.next()) {
					System.out.print(
							answer.getString(1) + "\t\t" + answer.getString(2) + "\t\t" + answer.getString(3) + "\t");
					if (answer.getString(3).length() < 9)
						System.out.print("\t");
					System.out.print(answer.getString(4).subSequence(0, 10) + "\t" + answer.getString(5) + "\t\t"
							+ answer.getString(6) + "\t\t" + answer.getString(7) + "\t\t" + answer.getString(8) + "\t\t"
							+ answer.getString(9) + "\n");
				}
			}
			System.out.println("=========================================================\n");

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Could not execute query three due to some SQL exception.");
		}
	}

    /*---------------------------------------------------------------------
    |  Method executeQueryFour(prefix)
    |
    |  Purpose:  Queries the database to list the the three categories of for each of the following queries
	|			 for any particular airline: Traveled only once in the month of March, Traveled with exactly
	|			 one checked in back anytime in the months of June and July, Ordered snacks/beverages on at least
	|			 one flight.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      prefix - The prefix to the database tables being queried.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void executeQueryFour(String prefix) {
		String[] categories = { "Student", "Frequent_Flier", "Minor" };
		for (int x = 0; x < 3; x++) {
			try {
				Statement stmt = dbconn.createStatement(); // The statement to execute the query
				ResultSet answer = null; // The answer to the query

				// The query to execute to oracle
				String queryP1 = "SELECT DISTINCT name" + " FROM " + prefix + ".Passenger" + " JOIN " + prefix
						+ ".PassengerFlight USING (passenger_id)" + " JOIN " + prefix + ".Flight USING (flight_id)"
						+ " WHERE airline = 'Delta'" + " AND " + categories[x] + " = 1"
						+ " AND flight_date BETWEEN DATE '2021-03-01' AND DATE '2021-03-31'"
						+ " GROUP BY name, passenger_id" + " HAVING COUNT(*) = 1";

				String queryP2 = "SELECT DISTINCT name" + " FROM " + prefix + ".Passenger" + " JOIN " + prefix
						+ ".PassengerFlight USING (passenger_id)" + " JOIN " + prefix + ".Flight USING (flight_id)"
						+ " WHERE airline = 'Delta'" + " AND " + categories[x] + " = 1"
						+ " AND flight_date BETWEEN DATE '2021-06-01' AND DATE '2021-07-31'" + " AND bags_checked = 1";

				String queryP3 = "SELECT DISTINCT name" + " FROM " + prefix + ".Passenger" + " JOIN " + prefix
						+ ".PassengerFlight USING (passenger_id)" + " JOIN " + prefix + ".Flight USING (flight_id)"
						+ " WHERE airline = 'Delta'" + " AND " + categories[x] + " = 1" + " AND ordered_snacks = 1";

				System.out.println("\nCATEGORY: " + categories[x] + "\n");

				answer = stmt.executeQuery(queryP1);
				if (answer != null) {
					System.out.println("TRAVELED ONCE:");
					ResultSetMetaData answermetadata = answer.getMetaData();

					for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
						System.out.print(answermetadata.getColumnName(i) + "\t");
					}
					System.out.println("\n--------------------------");

					while (answer.next())
						System.out.println(answer.getString(1));
				}
				System.out.println("=========================\n");

				answer = null;
				answer = stmt.executeQuery(queryP2);
				if (answer != null) {
					System.out.println("TRAVELED WITH ONE CHECKED IN BAG:");
					ResultSetMetaData answermetadata = answer.getMetaData();

					for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
						System.out.print(answermetadata.getColumnName(i) + "\t");
					}
					System.out.println("\n--------------------------");

					while (answer.next())
						System.out.println(answer.getString(1));
				}
				System.out.println("=========================\n");

				answer = null;
				answer = stmt.executeQuery(queryP3);
				if (answer != null) {
					System.out.println("ORDERED SNACKS/BEVERAGES:");
					ResultSetMetaData answermetadata = answer.getMetaData();

					for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
						System.out.print(answermetadata.getColumnName(i) + "\t");
					}
					System.out.println("\n--------------------------");

					while (answer.next())
						System.out.println(answer.getString(1));
				}
				System.out.println("=========================\n");

				stmt.close();

			} catch (SQLException e) {
				System.out.println("Could not execute query four due to some SQL exception.");
			}
		}

	}

    /*---------------------------------------------------------------------
    |  Method executeQueryFive(prefix)
    |
    |  Purpose:  Queries the database to list all the staff that are related to a particular airport
	|			 based on the airportID provided.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
    |      prefix - The prefix to the database tables being queried.
	|	   airportID - The ID of the airport to check the staff of.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void executeQueryFive(String prefix, String airportID) {
		/*
		 * Given an airportID display all the staff members that are involved with that
		 * airport based on the airlines that are involved in that airport. Display the
		 * employee_id and names of the staff members ordered by their names in
		 * alphabetical order
		 */
		try {
			Statement stmt = dbconn.createStatement(); // The statement to execute the query
			ResultSet answer = null; // The answer to the query

			// The query to execute to oracle
			// SELECT DISTINCT Staff.employee_id, Staff.name FROM Airport JOIN Flight ON
			// Flight.dest_from=Airport.airport_id JOIN Staff ON
			// Flight.airline=Staff.employed_by WHERE Airport.airport_id = 1 ORDER By
			// Staff.name;
			String query = "SELECT DISTINCT Staff.employee_id, Staff.name " + "FROM " + prefix + ".Airport" + " JOIN "
					+ prefix + ".Flight ON " + prefix + ".Flight.dest_from=" + prefix + ".Airport.airport_id" + " JOIN "
					+ prefix + ".Staff ON " + prefix + ".Flight.airline=" + prefix + ".Staff.employed_by "
					+ "WHERE Airport.airport_id = " + airportID + " ORDER By Staff.name";

			answer = stmt.executeQuery(query);

			if (answer != null) {

				System.out.println("\nThe results of the query are:\n");

				// Get the data about the query result to learn
				// the attribute names and use them as column headers

				ResultSetMetaData answermetadata = answer.getMetaData();

				for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
					System.out.print(answermetadata.getColumnName(i) + "\t");
				}
				System.out.println("\n-------------");

				// Use next() to advance cursor through the result
				// tuples and print their attribute values

				while (answer.next()) {
					System.out.println(answer.getString(1) + "\t" + answer.getString(2));
				}
			}
			System.out.println("=============\n");

			stmt.close();

		} catch (SQLException e) {
			System.out.println("Could not execute query one due to some SQL exception.");
		}
	}

    /*---------------------------------------------------------------------
    |  Method close()
    |
    |  Purpose:  Closes the connection to the oracle database.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: The connection to the database is closed.
    |
    |  Parameters: None
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void close() {
		try {
			dbconn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /*---------------------------------------------------------------------
    |  Method insertFlight(flightId, airlineName, boardingGate, flightDate,
	|					boardingTime, departingTime, intervalMinutes, airportFrom, airportTo)
    |
    |  Purpose:  Inserts a tuple to the Flight table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		flightId 		- ID of the new flight
	|		airlineName		- Name of the new flight
	|		boardingGate	- Boarding gate of new flight
	|		flightDate		- Day of the flight
	|		boardingTime	- Time of the flight boarding
	|		departingTime	- Time of departure
	|		intervalMinutes - Interval of flight
	|		airportFrom		- The ID of the airport its leaving
	|		airportTo		- The ID of the airport it is going to 
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void insertFlight(int flightId, String airlineName, String boardingGate, Date flightDate,
			LocalTime boardingTime, LocalTime departingTime, int intervalMinutes, int airportFrom, int airportTo)
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO chasehult.Flight VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		pstmt.setInt(1, flightId);
		pstmt.setString(2, airlineName);
		pstmt.setString(3, boardingGate);
		pstmt.setDate(4, flightDate);
		pstmt.setInt(5, boardingTime.getHour() * 100 + boardingTime.getMinute());
		pstmt.setInt(6, departingTime.getHour() * 100 + departingTime.getMinute());
		pstmt.setInt(7, intervalMinutes / 60 * 100 + intervalMinutes % 60);
		pstmt.setInt(8, airportFrom);
		pstmt.setInt(9, airportTo);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method updateFlight(flightId, airlineName, boardingGate, flightDate,
	|					boardingTime, departingTime, intervalMinutes, airportFrom, airportTo)
    |
    |  Purpose:  Updates a tuple to the Flight table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		flightId 		- ID of the flight to update
	|		airlineName		- Name of the new flight
	|		boardingGate	- Boarding gate of new flight
	|		flightDate		- Day of the flight
	|		boardingTime	- Time of the flight boarding
	|		departingTime	- Time of departure
	|		intervalMinutes - Interval of flight
	|		airportFrom		- The ID of the airport its leaving
	|		airportTo		- The ID of the airport it is going to 
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void updateFlight(int flightId, String airlineName, String boardingGate, Date flightDate,
			LocalTime boardingTime, LocalTime departingTime, int intervalMinutes, int airportFrom, int airportTo)
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE chasehult.Flight"
				+ " SET airline = ?, boarding_gate = ?, flight_date = ?, boarding_time = ?, departing_time = ?,"
				+ "     interval = ?, dest_from = ?, dest_to = ?"
				+ " WHERE flight_id = ?"
		);
		pstmt.setString(1, airlineName);
		pstmt.setString(2, boardingGate);
		pstmt.setDate(3, flightDate);
		pstmt.setInt(4, boardingTime.getHour() * 100 + boardingTime.getMinute());
		pstmt.setInt(5, departingTime.getHour() * 100 + departingTime.getMinute());
		pstmt.setInt(6, intervalMinutes / 60 * 100 + intervalMinutes % 60);
		pstmt.setInt(7, airportFrom);
		pstmt.setInt(8, airportTo);
		pstmt.setInt(9, flightId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method deleteFlight(flightId)
    |
    |  Purpose:  Deletes a tuple to the Flight table in the database currently connected based on 
	|			 the flight ID provided.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		flightId 		- ID of the flight to delete.
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	
	public static void deleteFlight(int flightId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM chasehult.Flight"
				+ " WHERE flight_id = ?"
		);
		pstmt.setInt(1, flightId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method insertPassenger(passengerId, name, frequentFlier, student, minor)
    |
    |  Purpose:  Inserts a tuple to the Passenger table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		passengerId   - The ID of the new passenger
	|		name		  - The name of the new passenger
	|		frequentFlier - Boolean representing if passenger is a frequent flier
	|		student		  - Boolean representing if passenger is a student
	|		minor		  - Boolean representing if passenger is a minor
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void insertPassenger(int passengerId, String name, boolean frequentFlier, boolean student, boolean minor) 
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO chasehult.Passenger VALUES (?, ?, ?, ?, ?)");
		pstmt.setInt(1, passengerId);
		pstmt.setString(2, name);
		pstmt.setInt(3, frequentFlier?1:0);
		pstmt.setInt(4, student?1:0);
		pstmt.setInt(5, minor?1:0);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method updatePassenger(passengerId, name, frequentFlier, student, minor)
    |
    |  Purpose:  Updates a tuple in the Passenger table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		passengerId   - The ID of the passenger to update
	|		name		  - The name of the new passenger
	|		frequentFlier - Boolean representing if passenger is a frequent flier
	|		student		  - Boolean representing if passenger is a student
	|		minor		  - Boolean representing if passenger is a minor
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void updatePassenger(int passengerId, String name, boolean frequentFlier, boolean student, boolean minor) 
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE chasehult.Passenger"
				+ " SET name = ?, frequent_flier = ?, student = ?, minor = ?"
				+ " WHERE airport_id = ?"
		);
		pstmt.setString(1, name);
		pstmt.setInt(2, frequentFlier?1:0);
		pstmt.setInt(3, student?1:0);
		pstmt.setInt(4, minor?1:0);
		pstmt.setInt(5, passengerId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method deletePassenger(passengerId)
    |
    |  Purpose:  Deletes a tuple in the Passenger table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		passengerId   - The ID of the passenger to delete
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void deletePassenger(int passengerId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM chasehult.Passenger"
				+ " WHERE passenger_id = ?"
		);
		pstmt.setInt(1, passengerId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method insertPassengerFlight(passengerId, flightId, bagsChecked, orderedSnacks)
    |
    |  Purpose:  Inserts a tuple to the PassengerFlight table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		passengerId   - The ID of the passenger
	|		flightId	  - The ID of the flight
	|		bagsChecked   - The number of bags that were checked
	|		orderedSnacks - Boolean representing if passenger ordered snacks
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void insertPassengerFlight(int passengerId, int flightId, int bagsChecked, boolean orderedSnacks) 
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO chasehult.PassengerFlight VALUES (?, ?, ?, ?)");
		pstmt.setInt(1, passengerId);
		pstmt.setInt(2, flightId);
		pstmt.setInt(3, bagsChecked);
		pstmt.setInt(4, orderedSnacks?1:0);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method updatePassengerFlight(passengerId, flightId, bagsChecked, orderedSnacks)
    |
    |  Purpose:  Updates a tuple in the PassengerFlight table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		passengerId   - The ID of the passenger
	|		flightId	  - The ID of the flight
	|		bagsChecked   - The number of bags that were checked
	|		orderedSnacks - Boolean representing if passenger ordered snacks
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void updatePassengerFlight(int passengerId, int flightId, int bagsChecked, boolean orderedSnacks)
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE chasehult.PassengerFlight"
				+ " SET bags_checked = ?, ordered_snacks = ?"
				+ " WHERE passenger_id = ? AND flight_id = ?"
		);
		pstmt.setInt(1, bagsChecked);
		pstmt.setInt(2, orderedSnacks?1:0);
		pstmt.setInt(3, passengerId);
		pstmt.setInt(4, flightId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method deletePassengerFlight(passengerId, flightId)
    |
    |  Purpose:  Deletes a tuple in the PassengerFlight table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		passengerId   - The ID of the passenger 
	|       flightId	  - The ID of the flight
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void deletePassengerFlight(int passengerId, int flightId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM chasehult.PassengerFlight"
				+ " WHERE passenger_id = ? AND flight_id = ?"
		);
		pstmt.setInt(1, passengerId);
		pstmt.setInt(2, flightId);
		pstmt.executeQuery();
	}
    /*---------------------------------------------------------------------
    |  Method insertAirport(airportId, name, airportCode, location)
    |
    |  Purpose:  Inserts a tuple to the Airport table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		airportId   	- The ID of the new airport
	|		name		  	- The name of the new airport
	|		airportCode 	- The code of the new airport
	|		location		- The airport location
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void insertAirport(int airportId, String name, String airportCode, String location) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO chasehult.Staff VALUES (?, ?, ?, ?)");
		pstmt.setInt(1, airportId);
		pstmt.setString(2, name);
		pstmt.setString(3, airportCode);
		pstmt.setString(4, location);
		pstmt.executeQuery();
		
	}

    /*---------------------------------------------------------------------
    |  Method insertAirport(airportId, name, airportCode, location)
    |
    |  Purpose:  Updates a tuple in the Airport table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		airportId   	- The ID of the airport to update
	|		name		  	- The new name of the airport
	|		airportCode 	- The new code of the airport
	|		location		- The new airport location
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void updateAirport(int airportId, String name, String airportCode, String location) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE chasehult.Airport"
				+ " SET airport_name = ?, airport_code = ?, location = ?"
				+ " WHERE airport_id = ?"
		);
		pstmt.setString(1, name);
		pstmt.setString(2, airportCode);
		pstmt.setString(3, location);
		pstmt.setInt(3, airportId);
		pstmt.executeQuery();
	}

	/*---------------------------------------------------------------------
    |  Method deleteAirport(airportId)
    |
    |  Purpose:  Deletes a tuple in the Airport table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		airportId   	- The ID of the airport to delete
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void deleteAirport(int airportId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM chasehult.Airport"
				+ " WHERE airport_id = ?"
		);
		pstmt.setInt(1, airportId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method insertBoardingGate(gateId, airport)
    |
    |  Purpose:  Inserts a tuple to the BoardingGate table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		gateId   	- The ID of the new boarding gate
	|		airport		- The airport in which this gate lives
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void insertBoardingGate(int gateId, int airport) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO chasehult.BoardingGate VALUES (?, ?)");
		pstmt.setInt(1, gateId);
		pstmt.setInt(2, airport);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method updateBoardingGate(gateId, airport)
    |
    |  Purpose:  Updates a tuple in the BoardingGate table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		gateId   	- The ID of the boarding gate to update
	|		airport		- The new airport in which the gate will live
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void updateBoardingGate(int gateId, int airport) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE chasehult.BoardingGate"
				+ " SET airport_id = ?"
				+ " WHERE boarding_gate = ?"
		);
		pstmt.setInt(1, airport);
		pstmt.setInt(2, gateId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method deleteBoardingGate(gateId)
    |
    |  Purpose:  Deletes a tuple in the BoardingGate table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		gateId   	- The ID of the boarding gate to delete
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void deleteBoardingGate(int gateId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM chasehult.BoardingGate"
				+ " WHERE boarding_gate = ?"
		);
		pstmt.setInt(1, gateId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method insertStaff(employeeId, name, job, airport)
    |
    |  Purpose:  Inserts a tuple in the Staff table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		employeeId   	- The ID of the new employee
	|		name		  	- The name of the new employee
	|		job 			- The job of the new employee
	|		airline			- The airline which employs this employee
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void insertStaff(int employeeId, String name, String job, String airline) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO chasehult.Staff VALUES (?, ?, ?, ?)");
		pstmt.setInt(1, employeeId);
		pstmt.setString(2, name);
		pstmt.setString(3, job);
		pstmt.setString(4, airline);
		pstmt.executeQuery();

	}

    /*---------------------------------------------------------------------
    |  Method insertStaff(employeeId, name, job, airport)
    |
    |  Purpose:  Updates a tuple in the Staff table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		employeeId   	- The ID of the employee top update
	|		name		  	- The new name of the employee
	|		job 			- The new job of the employee
	|		airline			- The new airline which employs this employee
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void updateStaff(int employeeId, String name, String job, String airline) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE chasehult.Staff"
				+ " SET name = ?, job = ?, employed_by = ?"
				+ " WHERE employee_id = ?"
		);
		pstmt.setString(1, name);
		pstmt.setString(2, job);
		pstmt.setString(3, airline);
		pstmt.setInt(4, employeeId);
		pstmt.executeQuery();
	}

    /*---------------------------------------------------------------------
    |  Method insertStaff(employeeId)
    |
    |  Purpose:  Deletes a tuple in the Staff table in the database currently connected.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Parameters:
	|		employeeId   	- The ID of the employee that will be deleted
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void deleteStaff(int employeeId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM chasehult.Staff"
				+ " WHERE employee_id = ?"
		);
		pstmt.setInt(1, employeeId);
		pstmt.executeQuery();
	}
	
    /*---------------------------------------------------------------------
    |  Method getPassengers()
    |
    |  Purpose:  Print all of the passengers.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void getPassengers() throws SQLException {
		ResultSet answer = dbconn.createStatement().executeQuery("SELECT * FROM chasehult.Passenger ORDER BY passenger_id");
		System.out.println("PID  Passenger Name                  Frequent Flier  Student  Minor");
		System.out.println("---  ------------------------------  --------------  -------  -----");
		while (answer.next()) {
			System.out.printf(
					"%3d  %30s  %14s  %7s  %5s\n",
					answer.getInt("passenger_id"),
					answer.getString("name"),
					answer.getBoolean("frequent_flier")?"Yes":"No",
					answer.getBoolean("student")?"Yes":"No",
					answer.getBoolean("minor")?"Yes":"No"
			);
		}
	}
	
    /*---------------------------------------------------------------------
    |  Method getFlights()
    |
    |  Purpose:  Print all of the flights.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	@SuppressWarnings("deprecation")
	public static void getFlights() throws SQLException {
		ResultSet answer = dbconn.createStatement().executeQuery(""
				+ "SELECT chasehult.Flight.*, apf.airport_code AS from_code, apt.airport_code AS to_code"
				+ " FROM chasehult.Flight"
				+ "  JOIN chasehult.Airport apf ON (dest_from = apf.airport_id)"
				+ "  JOIN chasehult.Airport apt ON (dest_to = apt.airport_id)"
				+ " ORDER BY flight_id");
		System.out.println("FID   Airline   GATE  DATE   BOARDING  DEPARTING  DURATION  FROM  TO ");
		System.out.println("---  ---------  ----  -----  --------  ---------  --------  ----  ---");
		while (answer.next()) {
			System.out.printf(
					"%3d  %9s  %4s  %02d/%02d     %02d:%02d      %02d:%02d  %5d:%02d  %4s  %3s\n",
					answer.getInt("flight_id"),
					answer.getString("airline"),
					answer.getString("boarding_gate"),
					answer.getDate("flight_date").getMonth() + 1,
					answer.getDate("flight_date").getDay() + 1,
					answer.getInt("boarding_time") / 100,
					answer.getInt("boarding_time") % 100,
					answer.getInt("departing_time") / 100,
					answer.getInt("departing_time") % 100,
					answer.getInt("duration") / 100,
					answer.getInt("duration") % 100,
					answer.getString("from_code"),
					answer.getString("to_code")
			);
		}
	}

    /*---------------------------------------------------------------------
    |  Method getHistory(passengerId)
    |
    |  Purpose:  Print the flight history of a passenger.
    |
    |  Pre-condition:  A connection to the database is established.
    |
    |  Post-condition: None
    |
    |  Returns:  None
    *-------------------------------------------------------------------*/
	public static void getHistory(int passengerId) throws SQLException {
		ResultSet answer = dbconn.createStatement().executeQuery("SELECT * FROM chasehult.PassengerFlight WHERE passenger_id = " + passengerId + " ORDER BY flight_id");
		System.out.println("FID  Bags Checked  Snacks?");
		System.out.println("---  ------------  -------");
		while (answer.next()) {
			System.out.printf(
					"%3d  %12d  %7s\n",
					answer.getInt("flight_id"),
					answer.getInt("bags_checked"),
					answer.getBoolean("ordered_snacks")?"Yes":"No"
			);
		}
	}
}