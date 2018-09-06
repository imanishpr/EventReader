

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Statement;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


public class DBArrow {
	//private static final Logger LOGGER = LoggerFactory.getLogger(DBArrow.class);
    private static final String dbUser="";
	private static final String dbHost="";
    private static final String dbName="";
    private static final String dbPass="";
	
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
        	//LOGGER.error("ERROR WHILE GETTING PREPARED STATEMENT :" + e.getMessage());
        }
        return null;
    }
    public PreparedStatement getPreparedStatementForId(String s) {
        try {
            dbConnection = getConnection();
            dbConnection.setAutoCommit(false);
            return dbConnection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
        	//LOGGER.error("ERROR WHILE GETTING PREPARED STATEMENT :" + e.getMessage());
        }
        return null;
    }
    public ResultSet fire(PreparedStatement statement) throws SQLException {
        try {
            rs = statement.executeQuery();
        } catch (SQLException e) {
        	//LOGGER.error("ERROR WHILE FIRING PREPARED STATEMENT :" + e.getMessage());
        }
        return rs;
    }
    public int fireBowfishing(PreparedStatement statement) throws SQLException {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        	//LOGGER.error("ERROR WHILE FIRING BOWFISHING FOR PREPARED STATEMENT :" + e.getMessage());
        }
        return 0;
    }

    public void relax(ResultSet rs) throws SQLException {
    	dbConnection.commit();
    	System.out.println("closing");
        dbConnection.close();
        dbConnection = null;
        if (preparedStatement != null)
            preparedStatement.close();
        if (rs != null)
        	rs.close();
    }
    public void rollBack(ResultSet rs) throws SQLException {
    	dbConnection.rollback();;
        dbConnection.close();
        dbConnection = null;
        if (preparedStatement != null)
            preparedStatement.close();
        if (rs != null)
        	rs.close();
    }
    
    public static Connection getConnection() throws SQLException {
        if (dbConnection == null || dbConnection.isClosed()) {
            return DriverManager.getConnection(
                    "jdbc:mysql://"+dbHost+"/"+dbName+"?user="+dbUser+"&password="+dbPass);
        } else {
            return dbConnection;
        }

    }
}