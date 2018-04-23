

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBArrow {
    private static final String dbUser="root";
	private static final String dbHost="localhost";
    private static final String dbName="caper";
    private static final String dbPass="Wsquare";
	
    private static  DBArrow dbArrow;

    static {
        try {
            dbArrow = new DBArrow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection dbConnection = null;

    private PreparedStatement preparedStatement = null;

    private ResultSet rs = null;

    public DBArrow() throws SQLException {
    	System.out.println(dbUser +"testt");
    }

    public static DBArrow getArrow() {
        return dbArrow;
    }

    public PreparedStatement getPreparedStatement(String s) {
        try {
            dbConnection = getConnection();
            dbConnection.setAutoCommit(false);
            return dbConnection.prepareStatement(s);
        } catch (SQLException e) {
        	System.out.println("ERROR WHILE GETTING PREPARED STATEMENT :" + e.getMessage());
        }
        return null;
    }

    public ResultSet fire(PreparedStatement statement) throws SQLException {
        try {
            rs = statement.executeQuery();
        } catch (SQLException e) {
        	System.out.println("ERROR WHILE FIRING PREPARED STATEMENT :" + e.getMessage());
        }
        return rs;
    }
    public int fireBowfishing(PreparedStatement statement) throws SQLException {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
        	System.out.println("ERROR WHILE FIRING BOWFISHING FOR PREPARED STATEMENT :" + e.getMessage());
        }
        return 0;
    }

    public void relax(ResultSet rs) throws SQLException {
    	dbConnection.commit();
        dbConnection.close();
        dbConnection = null;
        if (preparedStatement != null)
            preparedStatement.close();
        if(rs != null)
        	rs.close();
    }
    
    public static Connection getConnection() throws SQLException {
        if (dbConnection == null) {
            return DriverManager.getConnection(
                    "jdbc:mysql://"+dbHost+"/"+dbName+"?user="+dbUser+"&password="+dbPass);
        } else {
            return dbConnection;
        }

    }
}
