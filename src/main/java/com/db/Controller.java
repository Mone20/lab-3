package com.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public  void insert(T newInstance) throws  SQLException {

               if(table.insert(newInstance)>0)
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
    public void update(HashMap map,int id)throws SQLException {
        if(table.update(map,id)>0)
            System.out.println("New instance insert succesfully");
    }
public T select(int id) throws SQLException {
    return (T)table.select(id);

}
    public List<T> select(Map<String,String> map) throws SQLException
    {
        return table.select(map);
    }
public List selectAll() throws SQLException {
        return table.selectAll();
    }
    public void truncate() throws SQLException {
        table.truncate();
    }


}