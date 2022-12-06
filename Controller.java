import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller{
	
	public static void executeQueryOne(Connection con, String prefix) {
		try {
			Statement stmt = con.createStatement();	// The statement to execute the query
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
	
	public static void executeQueryTwo(Connection con, String prefix, String date, String airline) {
		try {
			Statement stmt = con.createStatement();	// The statement to execute the query
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
}