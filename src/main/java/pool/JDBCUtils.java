package pool;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class JDBCUtils {

    private DataSource dataSource;

    public JDBCUtils() {
    }

    public void init(String dataSourceName) {
        try {

           Context initContext = new InitialContext();
           dataSource = (DataSource) initContext.lookup(dataSourceName);
        } catch (NamingException e) {

        }
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is null.");
        }

        return dataSource.getConnection();
    }

}
