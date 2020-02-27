import model.Worker;
import pool.DataAccessFactory;
import pool.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    static Connection connection = null;

    public static void connectDB() throws ClassNotFoundException, SQLException {


        JDBCUtils jdbcUtils = DataAccessFactory.getJDBCUtils();


        System.out.println("Connecting to DB...");

        connection = jdbcUtils.getConnection();

        System.out.println("Database successfully connected...");

    }
    public static void createDB() throws SQLException {
        String sql="\n" +
                "CREATE DATABASE postgres\n" +
                "    WITH \n" +
                "    OWNER = postgres\n" +
                "    ENCODING = 'UTF8'\n" +
                "    LC_COLLATE = 'Russian_Russia.1251'\n" +
                "    LC_CTYPE = 'Russian_Russia.1251'\n" +
                "    TABLESPACE = pg_default\n" +
                "    CONNECTION LIMIT = -1;\n" +
                "\n" +
                "COMMENT ON DATABASE postgres\n" +
                "    IS 'default administrative connection database';";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }
}