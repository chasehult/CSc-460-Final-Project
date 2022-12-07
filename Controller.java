import java.sql.*;
import java.util.Date;

import javax.xml.datatype.Duration;

public class Controller{
    private static Connection dbconn;
    public Controller(String username, String password){
        final String oracleURL =   // Magic lectura -> aloe access spell
                "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

        // load the (Oracle) JDBC driver by initializing its base
        // class, 'oracle.jdbc.OracleDriver'.
         try {

            Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

            System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
            System.exit(-1);

        }
        // make and return a database connection to the user's
        // Oracle database

        dbconn = null;

        try {
            dbconn = DriverManager.getConnection
                    (oracleURL,username,password);

        } catch (SQLException e) {

            System.err.println("*** SQLException:  "
                    + "Could not open JDBC connection.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);

        }
    }
	
	public static void executeQueryOne(String prefix) {
		try {
			Statement stmt = dbconn.createStatement();	// The statement to execute the query
			ResultSet answer = null; // The answer to the query
			
			// The query to execute to oracle
			String query = "SELECT DISTINCT name "
					+ "FROM " + prefix + ".Passenger "
							+ "JOIN " + prefix + ".PassengerFlight USING (passenger_id) "
							+ "JOIN " + prefix + ".Flight USING (flight_id) "
					+ "GROUP BY passenger_id, name "
					+ "HAVING COUNT(DISTINCT airline) = 4";
			
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

                while (answer.next()) System.out.println(answer.getString(1));
            }
            	System.out.println("=============\n");
            
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Could not execute query one due to some SQL exception.");
		}
	}
	
	public static void executeQueryTwo(String prefix, String date, String airline) {
		try {
			Statement stmt = dbconn.createStatement();	// The statement to execute the query
			ResultSet answer = null; // The answer to the query
			
			// The query to execute to oracle
			String query = "SELECT flight_id, name, bags_checked "
					+ "FROM " + prefix + ".Passenger "
						+ "NATURAL JOIN " + prefix + ".PassengerFlight "
						+ "NATURAL JOIN " + prefix + ".Flight "
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
			Statement stmt = dbconn.createStatement();	// The statement to execute the query
			ResultSet answer = null; // The answer to the query
			
			// The query to execute to oracle
			String query = "SELECT DISTINCT flight_id, boarding_gate, airline, flight_date,"
					+ " boarding_time, departing_time, duration, apf.location AS \"from\","
					+ " apt.location AS \"to\""
					+ " FROM " + prefix + ".Flight"
					+ " JOIN " + prefix + ".Airport apf ON (dest_from = apf.airport_id)"
					+ " JOIN " + prefix + ".Airport apt ON (dest_to = apt.airport_id)"
					+ " WHERE flight_date = TO_DATE(" + date + " || '-JUN-21') ORDER BY boarding_time ASC;";
			
			answer = stmt.executeQuery(query);

            if (answer != null) {

                System.out.println("\nThe results of the query are:\n");

                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers

                ResultSetMetaData answermetadata = answer.getMetaData();

                for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
                    System.out.print(answermetadata.getColumnName(i) + "\t");
                }
                System.out.println("\n---------------------------------------------------------");

                    // Use next() to advance cursor through the result
                    // tuples and print their attribute values

                while (answer.next()) {
                	System.out.println(answer.getString(1) + "\t" + answer.getString(2) + "\t" + 
                			answer.getString(3) + "\t" + answer.getString(4) + "\t" + answer.getString(5) + "\t" + 
                			answer.getString(6) + "\t" + answer.getString(7) + "\t" + answer.getString(8));
                }
            }
            	System.out.println("=========================================================\n");
            
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Could not execute query three due to some SQL exception.");
		}
	}
	
	public static void executeQueryFour(String prefix) {
		String[] categories = {"Student", "Frequent_Flier", "Minor"};
		for (int x = 0; x < 3; x++) {
				try {
				Statement stmt = dbconn.createStatement();	// The statement to execute the query
				ResultSet answer = null; // The answer to the query
				
				// The query to execute to oracle
				String queryP1 = "SELECT DISTINCT name"
						+ " FROM " + prefix + ".Passenger"
							+ " JOIN " + prefix + ".PassengerFlight USING (passenger_id)"
							+ " JOIN " + prefix + ".Flight USING (flight_id)"
						+ " WHERE airline = 'Delta'"
						+ " AND " + categories[x] + " = 1"
						+ " AND flight_date BETWEEN DATE '2021-03-01' AND DATE '2021-03-31'"
						+ " GROUP BY name, passenger_id"
						+ " HAVING COUNT(*) = 1";
				
				String queryP2 = "SELECT DISTINCT name"
						+ " FROM " + prefix + ".Passenger"
							+ " JOIN " + prefix + ".PassengerFlight USING (passenger_id)"
							+ " JOIN " + prefix + ".Flight USING (flight_id)"
						+ " WHERE airline = 'Delta'"
						+ " AND " + categories[x] + " = 1"
						+ " AND flight_date BETWEEN DATE '2021-06-01' AND DATE '2021-07-31'"
						+ " AND bags_checked = 1";
				
				String queryP3 = "SELECT DISTINCT name"
						+ " FROM " + prefix + ".Passenger"
							+ " JOIN " + prefix + ".PassengerFlight USING (passenger_id)"
							+ " JOIN " + prefix + ".Flight USING (flight_id)"
						+ " WHERE airline = 'Delta'"
						+ " AND " + categories[x] + " = 1"
						+ " AND ordered_snacks = 1";
				
				System.out.println("\nCATEGORY: " + categories[x] + "\n");
				
				answer = stmt.executeQuery(queryP1);
	            if (answer != null) {
	            	System.out.println("TRAVELED ONCE:");
	                ResultSetMetaData answermetadata = answer.getMetaData();
	
	                for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
	                    System.out.print(answermetadata.getColumnName(i) + "\t");
	                }
	                System.out.println("\n--------------------------");
	
	                while (answer.next()) System.out.println(answer.getString(1));
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
	
	                while (answer.next()) System.out.println(answer.getString(1));
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
	
	                while (answer.next()) System.out.println(answer.getString(1));
	            }
	            System.out.println("=========================\n");
	            
				stmt.close();
				
			} catch (SQLException e) {
				System.out.println("Could not execute query four due to some SQL exception.");
			}
		}
		
	}


    public static void executeQueryFive(String prefix, String airportID) {
        /* Given an airportID display all the staff members that are involved with that airport based on the airlines that 
        are involved in that airport. Display the employee_id and names of the staff members ordered by their names in alphabetical order */
		try {
			Statement stmt = dbconn.createStatement();	// The statement to execute the query
			ResultSet answer = null; // The answer to the query
			
			// The query to execute to oracle
            //SELECT DISTINCT Staff.employee_id, Staff.name FROM Airport JOIN Flight ON Flight.dest_from=Airport.airport_id JOIN Staff ON Flight.airline=Staff.employed_by WHERE Airport.airport_id = 1 ORDER By Staff.name;
			String query = "SELECT DISTINCT Staff.employee_id, Staff.name "
					+ "FROM " + prefix + ".Airport "
							+ "JOIN " + prefix + ".Flight ON " + prefix + "Flight.dest_from=" + prefix + "Airport.airport_id"
							+ "JOIN " + prefix + ".Staff ON " + prefix + "Flight.airline=" + prefix + "Staff.employed_by "
					+ "WHERE Airport.airport_id = " + airportID
					+ " ORDER By Staff.name";
			
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

                while (answer.next()) System.out.println(answer.getString(1));
            }
            	System.out.println("=============\n");
            
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Could not execute query one due to some SQL exception.");
		}
	}

    public void close(){
        try {
            dbconn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insertFlight(int flightId, String airlineName, String boardingGate, 
    java.sql.Date flightDate, java.sql.Time boardingTime, java.sql.Time departingTime, 
    int airportFrom, int airportTo) throws SQLException{
        String query = "INSERT INTO Flight VALUES (" + String.valueOf(flightId) + ", '"
        + airlineName + "', '" + boardingGate + "', '" + String.valueOf(flightDate) + "', '"
        + String.valueOf(boardingTime) + "', '" + String.valueOf(departingTime) + "', '"
        + String.valueOf(java.time.Duration.between(boardingTime.toLocalTime(), departingTime.toLocalTime()))
        + "'," + String.valueOf(airportFrom) + ", " + airportTo + ");";

        try{
            dbconn.createStatement().executeQuery(query);
            }catch(SQLException e){
                throw e;
            }
        }

        public void updateFlight(int flightId, String airlineName, String boardingGate, 
        java.sql.Date flightDate, java.sql.Time boardingTime, java.sql.Time departingTime, 
        int airportFrom, int airportTo) throws SQLException{
            String query = "UPDATE Flight SET airline ='" + airlineName + 
            "', boarding_gate = '" + boardingGate + "', flight_date = '" + 
            String.valueOf(flightDate) + "', boarding_time = '" + String.valueOf(boardingTime) + 
            "', departing_time = '" + String.valueOf(departingTime) + "', duration = '"
            + String.valueOf(java.time.Duration.between(boardingTime.toLocalTime(), departingTime.toLocalTime()))
            + "', dest_from = " + String.valueOf(airportFrom) + ", dest_to = " + airportTo + 
            " WHERE flight_id = " + String.valueOf(flightId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deleteFlight (int flightId) throws SQLException{
            String query = "DELETE FROM Flight WHERE flight_id = " + String.valueOf(flightId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertPassenger(int passengerId, String name, boolean frequentFlier, 
        boolean student, boolean thirdCategory) throws SQLException{
        String query = "INSERT INTO Passenger VALUES (" + String.valueOf(passengerId) + ", '"
        + name + "', '" + String.valueOf(frequentFlier) + "', '" + String.valueOf(student) + "', '"
        + String.valueOf(thirdCategory) + "');";

        try{
            dbconn.createStatement().executeQuery(query);
            }catch(SQLException e){
                throw e;
            }
        }

        public void updatePassenger(int passengerId, String name, boolean frequentFlier, 
        boolean student, boolean thirdCategory) throws SQLException{
            String query = "UPDATE Passenger SET name ='" + name + 
            "', frequent_flier = '" + String.valueOf(frequentFlier) + "', student = '" + 
            String.valueOf(student) + "', third_category = '" + String.valueOf(thirdCategory) + 
            " WHERE passenger_id = " + String.valueOf(passengerId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deletePassenger (int passengerId) throws SQLException{
            String query = "DELETE FROM Passenger WHERE passenger_id = " + String.valueOf(passengerId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertAirport(int airportId, String name,String airportCode, String location) throws SQLException{
        String query = "INSERT INTO Airport VALUES (" + String.valueOf(airportId) + ", '"
        + name + "', '" + airportCode + "', '" + location + "');";

        try{
            dbconn.createStatement().executeQuery(query);
            }catch(SQLException e){
                throw e;
            }
        }

        public void updateAirport(int airportId, String name, String airportCode, String location) throws SQLException{
            String query = "UPDATE Airport SET airport_name ='" + name + "', airport_code = '" + airportCode + 
            "', location = '" + location + "' WHERE airport_id = " + String.valueOf(airportId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deleteAirport (int airportId) throws SQLException{
            String query = "DELETE FROM Airport WHERE airport_id = " + String.valueOf(airportId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertBoardingGate(int gateId, int airport) throws SQLException{
            String query = "INSERT INTO BoardingGate VALUES (" + String.valueOf(gateId) + ", "
            + airport + ");";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }
    
        public void updateBoardingGate(int gateId, int airport) throws SQLException{
            String query = "UPDATE BoardingGate SET airport_id = " + airport + 
            " WHERE boarding_gate = " + String.valueOf(gateId) + ";";
        
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }
    
        public void deleteBoardingGate (int gateId) throws SQLException{
            String query = "DELETE FROM BoardingGate WHERE boarding_gate = " + String.valueOf(gateId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertStaff(int employeeId, String name, String job) throws SQLException{
            String query = "INSERT INTO Staff VALUES (" + String.valueOf(employeeId) + ", '"
             + name + "', '" + job + "');";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }
        
        public void updateStaff(int employeeId, String name, String job) throws SQLException{
            String query = "UPDATE Staff SET name = '" + name + "', Job = '" + job + 
            "' WHERE employee_id = " + String.valueOf(employeeId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deleteStaff (int employeeId) throws SQLException{
            String query = "DELETE FROM Staff WHERE employee_id = " + String.valueOf(employeeId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertAirline(String name) throws SQLException{
            String query = "INSERT INTO Airline VALUES ('" + name + "');";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }
            
        public void updateAirline(String name) throws SQLException{
            deleteAirline(name);
            insertAirline(name);
            }

        public void deleteAirline (String name) throws SQLException{
            String query = "DELETE FROM Airline WHERE Name = '" + name + "';";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

    }

