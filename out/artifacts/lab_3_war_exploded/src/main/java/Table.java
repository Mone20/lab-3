import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Table<T> {
int insert(T newInstance) throws SQLException;
int delete(int id) throws SQLException;
int update(int id, String nameColumn, String newInstance) throws SQLException;
    int update(int id, String nameColumn, int newInstance) throws SQLException;
T select(int id) throws SQLException;
List<T> select(Map<String,String> map) throws SQLException;
List selectAll() throws SQLException;
void truncate() throws SQLException;
}
