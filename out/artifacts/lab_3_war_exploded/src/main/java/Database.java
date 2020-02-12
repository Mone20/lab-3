import pool.DataAccessFactory;
import pool.JDBCUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    static Connection connection = null;

    public static Worker testDB() throws ClassNotFoundException, SQLException {


        JDBCUtils jdbcUtils = DataAccessFactory.getJDBCUtils();


        System.out.println("Connecting to DB...");

        connection = jdbcUtils.getConnection();

        System.out.println("Database successfully connected...");
        Controller c = new Controller(new WorkersTable(connection));
        c.insert(new Worker("rodion", "19052000", 5, "asadulin", "radikovich", 0, 0, 1));
        c.delete(5);
        c.update(4, "lastname", "asadulin");
        return c.select(4);


    }
}