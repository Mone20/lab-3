import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.text.View;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Rodion
 */
public class Controller {
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
public Worker select(int id) throws SQLException {
    return (Worker)table.select(id);

}

}