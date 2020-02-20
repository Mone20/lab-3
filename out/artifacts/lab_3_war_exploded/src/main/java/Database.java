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
        Controller c = new Controller(new WorkersTable(connection));
        c.insert(new Worker("rodion", "19052000", 5, "asadulin", "radikovich", 0, 0, 1));
        c.delete(5);
        c.update(4, "lastname", "asadulin");



    }
}