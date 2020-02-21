import model.Worker;
import pool.DataAccessFactory;
import pool.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    static Connection connection = null;

    public static void connectDB() throws ClassNotFoundException, SQLException {


        JDBCUtils jdbcUtils = DataAccessFactory.getJDBCUtils();


        System.out.println("Connecting to DB...");

        connection = jdbcUtils.getConnection();

        System.out.println("Database successfully connected...");
        
    }
}