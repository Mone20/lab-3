import java.sql.ResultSet;
import java.sql.SQLException;

import model.Worker;

/**
 *
 * @author Rodion
 */
public class Controller<T> {
  public Table table;
    public Controller(Table t)
    {
       this.table=t;

    }

    public  void insert(Worker newWorker) throws  SQLException {

               if(table.insert(newWorker)>0)
                   System.out.println("New instance  succesfully created");

    }
    public void delete(int id) throws SQLException {

       if(table.delete(id)>0)
           System.out.println(" instance  succesfully deleted..");
}
public void update(int id, String nameColumn, String newInstance) throws SQLException {
    if(table.update(id,nameColumn,newInstance)>0)
        System.out.println("New instance insert succesfully");
}
    public void update(int id, String nameColumn, int newInstance) throws SQLException {
        if(table.update(id,nameColumn,newInstance)>0)
            System.out.println("New instance insert succesfully");
    }
public T select(int id) throws SQLException {
    return (T)table.select(id);

}
public ResultSet selectAll() throws SQLException {
        return table.selectAll();
    }

}