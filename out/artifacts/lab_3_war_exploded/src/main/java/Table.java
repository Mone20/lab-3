import java.sql.SQLException;

public interface Table<T> {
int insert(T newInstance) throws SQLException;
int delete(int id) throws SQLException;
int update(int id, String nameColumn, String newInstance) throws SQLException;
T select(int id) throws SQLException;
}
