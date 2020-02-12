package pool;

public class DataAccessFactory {

    private static final DataAccessFactory instance = new DataAccessFactory();
    private JDBCUtils jdbcUtil;

    private DataAccessFactory() {
    }

    public static DataAccessFactory getInstance() {
        return instance;
    }

    private JDBCUtils prepareJDBCUtils() {
        if (jdbcUtil == null) {
            jdbcUtil = new JDBCUtils();
            jdbcUtil.init("java:comp/env/jdbc/postgres");
        }

        return jdbcUtil;
    }

    public static synchronized JDBCUtils getJDBCUtils() {
        return getInstance().prepareJDBCUtils();
    }
}
