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
			e.printStackTrace();
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
            String query = "UPDATE Flight SET 'AirLine Name' ='" + airlineName + 
            "', 'Boarding Gate' = '" + boardingGate + "', 'Flight Date' = '" + 
            String.valueOf(flightDate) + "', 'Boarding Time' = '" + String.valueOf(boardingTime) + 
            "', 'Departing Time' = '" + String.valueOf(departingTime) + "', Duration = '"
            + String.valueOf(java.time.Duration.between(boardingTime.toLocalTime(), departingTime.toLocalTime()))
            + "', 'Airport From' = " + String.valueOf(airportFrom) + ", 'Airport To' = " + airportTo + 
            " WHERE 'Flight ID' = " + String.valueOf(flightId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deleteFlight (int flightId) throws SQLException{
            String query = "DELETE FROM Flight WHERE 'Flight ID' = " + String.valueOf(flightId) + ";";
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
            String query = "UPDATE Passenger SET Name ='" + name + 
            "', 'Frequent Flier' = '" + String.valueOf(frequentFlier) + "', Student = '" + 
            String.valueOf(student) + "', 'Third Category' = '" + String.valueOf(thirdCategory) + 
            " WHERE 'Passenger ID' = " + String.valueOf(passengerId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deletePassenger (int passengerId) throws SQLException{
            String query = "DELETE FROM Passenger WHERE 'Passenger ID' = " + String.valueOf(passengerId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertAirport(int airportId, String name, String location) throws SQLException{
        String query = "INSERT INTO Airport VALUES (" + String.valueOf(airportId) + ", '"
        + name + "', '" + location + "');";

        try{
            dbconn.createStatement().executeQuery(query);
            }catch(SQLException e){
                throw e;
            }
        }

        public void updateAirport(int airportId, String name, String location) throws SQLException{
            String query = "UPDATE Airport SET 'Airport Name' ='" + name + 
            "', Location = '" + location + "' WHERE 'Passenger ID' = " + String.valueOf(airportId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deleteAirport (int airportId) throws SQLException{
            String query = "DELETE FROM Airport WHERE 'Airport ID' = " + String.valueOf(airportId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertBoardingGate(int gateId, int airport) throws SQLException{
            String query = "INSERT INTO 'Boarding Gate' VALUES (" + String.valueOf(gateId) + ", "
            + airport + ");";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }
    
        public void updateBoardingGate(int gateId, int airport) throws SQLException{
            String query = "UPDATE 'Boarding Gate' SET Airport = " + airport + 
            " WHERE 'Gate ID' = " + String.valueOf(gateId) + ";";
        
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }
    
        public void deleteBoardingGate (int gateId) throws SQLException{
            String query = "DELETE FROM 'Boarding Gate' WHERE 'Gate ID' = " + String.valueOf(gateId) + ";";
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
        }

        public void insertStaff(int employeeId, int employedBy, String name, String job) throws SQLException{
            String query = "INSERT INTO Staff VALUES (" + String.valueOf(employeeId) + ", "
            + employedBy + ", '" + name + "', '" + job + "');";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }
        
        public void updateStaff(int employeeId, int employedBy, String name, String job) throws SQLException{
            String query = "UPDATE Staff SET 'Employed By' =" + employedBy + 
            ", Name = '" + name + "', Job = '" + job + "' WHERE 'Employee ID' = " + String.valueOf(employeeId) + ";";
    
            try{
                dbconn.createStatement().executeQuery(query);
                }catch(SQLException e){
                    throw e;
                }
            }

        public void deleteStaff (int employeeId) throws SQLException{
            String query = "DELETE FROM Staff WHERE 'Employee ID' = " + String.valueOf(employeeId) + ";";
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

