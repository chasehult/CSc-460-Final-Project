import java.sql.*;
import java.time.LocalTime;

public class Controller {
	private static Connection dbconn;

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

	public static void close() {
		try {
			dbconn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insertFlight(int flightId, String airlineName, String boardingGate, Date flightDate,
			LocalTime boardingTime, LocalTime departingTime, int intervalMinutes, int airportFrom, int airportTo)
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO Flight VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		pstmt.setInt(1, airportTo);
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

	public static void updateFlight(int flightId, String airlineName, String boardingGate, Date flightDate,
			LocalTime boardingTime, LocalTime departingTime, int intervalMinutes, int airportFrom, int airportTo)
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE Flight"
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

	public static void deleteFlight(int flightId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM Flight"
				+ " WHERE flight_id = ?"
		);
		pstmt.setInt(1, flightId);
		pstmt.executeQuery();
	}

	public static void insertPassenger(int passengerId, String name, boolean frequentFlier, boolean student, boolean minor) 
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO Staff VALUES (?, ?, ?, ?, ?)");
		pstmt.setInt(1, passengerId);
		pstmt.setString(2, name);
		pstmt.setInt(3, frequentFlier?1:0);
		pstmt.setInt(4, student?1:0);
		pstmt.setInt(5, minor?1:0);
		pstmt.executeQuery();
	}

	public static void updatePassenger(int passengerId, String name, boolean frequentFlier, boolean student, boolean minor) 
			throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE Airport"
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

	public static void deletePassenger(int passengerId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM Passenger"
				+ " WHERE passenger_id = ?"
		);
		pstmt.setInt(1, passengerId);
		pstmt.executeQuery();
	}

	public static void insertAirport(int airportId, String name, String airportCode, String location) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO Staff VALUES (?, ?, ?, ?)");
		pstmt.setInt(1, airportId);
		pstmt.setString(2, name);
		pstmt.setString(3, airportCode);
		pstmt.setString(4, location);
		pstmt.executeQuery();
		
	}

	public static void updateAirport(int airportId, String name, String airportCode, String location) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE Airport"
				+ " SET airport_name = ?, airport_code = ?, location = ?"
				+ " WHERE airport_id = ?"
		);
		pstmt.setString(1, name);
		pstmt.setString(2, airportCode);
		pstmt.setString(3, location);
		pstmt.setInt(3, airportId);
		pstmt.executeQuery();
	}

	public static void deleteAirport(int airportId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM Airport"
				+ " WHERE airport_id = ?"
		);
		pstmt.setInt(1, airportId);
		pstmt.executeQuery();
	}

	public static void insertBoardingGate(int gateId, int airport) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO BoardingGate VALUES (?, ?)");
		pstmt.setInt(1, gateId);
		pstmt.setInt(2, airport);
		pstmt.executeQuery();
	}

	public static void updateBoardingGate(int gateId, int airport) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE BoardingGate"
				+ " SET airport_id = ?"
				+ " WHERE boarding_gate = ?"
		);
		pstmt.setInt(1, airport);
		pstmt.setInt(2, gateId);
		pstmt.executeQuery();
	}

	public static void deleteBoardingGate(int gateId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM BoardingGate"
				+ " WHERE boarding_gate = ?"
		);
		pstmt.setInt(1, gateId);
		pstmt.executeQuery();
	}

	public static void insertStaff(int employeeId, String name, String job, String airport) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement("INSERT INTO Staff VALUES (?, ?, ?, ?)");
		pstmt.setInt(1, employeeId);
		pstmt.setString(2, name);
		pstmt.setString(3, job);
		pstmt.setString(4, airport);
		pstmt.executeQuery();

	}

	public static void updateStaff(int employeeId, String name, String job, String airport) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "UPDATE Staff"
				+ " SET name = ?, job = ?, employed_by = ?"
				+ " WHERE employee_id = ?"
		);
		pstmt.setString(1, name);
		pstmt.setString(2, job);
		pstmt.setString(3, airport);
		pstmt.setInt(4, employeeId);
		pstmt.executeQuery();
	}

	public static void deleteStaff(int employeeId) throws SQLException {
		PreparedStatement pstmt = dbconn.prepareStatement(""
				+ "DELETE FROM Staff"
				+ " WHERE employee_id = ?"
		);
		pstmt.setInt(1, employeeId);
		pstmt.executeQuery();
	}
}