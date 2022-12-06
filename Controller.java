import java.sql.*;

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
					+ "HAVING COUNT(DISTING airline) = 4";
			
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
					+ "FROM Passenger "
						+ "NATURAL JOIN PassengerFlight "
						+ "NATURAL JOIN Flight "
					+ "WHERE flight_date = TO_DATE(" + date + " || '-MAR-21') AND airline = " + airline
					+ " ORDER BY flight_id, bags_checked";
			
			answer = stmt.executeQuery(query);

            if (answer != null) {

                System.out.println("\nThe results of the query are:\n");

                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers

                ResultSetMetaData answermetadata = answer.getMetaData();

                for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
                    System.out.print(answermetadata.getColumnName(i) + "\t");
                }
                System.out.println("\n----------------------------------");

                    // Use next() to advance cursor through the result
                    // tuples and print their attribute values

                String currID = "";
                while (answer.next()) {
                	String flight = answer.getString(1);
                	if (flight != currID) {
                		currID = flight;
                		System.out.println("FLIGHT #" + currID);
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
	
	public static void executeQueryTwo(String prefix, String date) {
		try {
			Statement stmt = dbconn.createStatement();	// The statement to execute the query
			ResultSet answer = null; // The answer to the query
			
			// The query to execute to oracle
			String query = "SELECT DISTINCT flight_id, boarding_gate, airline, flight_date, "
					+ "boarding_time, departing_time, duration, route "
						+ "FROM Flight "
						+ "WHERE flight_date = TO_DATE(" + date + " || '-JUN-21')"
					+ "ORDER BY boarding_time ASC";
			
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

    public void close(){
        try {
            dbconn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}