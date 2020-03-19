import model.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class WorkersTable  implements Table<Worker>{
    private Connection connect=null;
    private String selectSQL="SELECT id, firstname, lastname, middlename, birthdate, positionId, degreeId, parentId\n" +
            "\tFROM public.workers\n";
    public WorkersTable(Connection connect) throws SQLException {
        this.connect=connect;
        String SQL="  CREATE TABLE IF NOT EXISTS public.workers\n" +
                "(\n" +
                "    id integer NOT NULL,\n" +
                "    firstname character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    lastname character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    middlename character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    birthdate character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    positionId integer,\n" +
                "    degreeId integer,\n" +
                "    parentId integer,\n" +
                "    CONSTRAINT workers_pkey PRIMARY KEY (id),\n" +
                "    CONSTRAINT positionId FOREIGN KEY (positionId)\n" +
                "        REFERENCES public.position (id) MATCH SIMPLE\n" +
                "        ON UPDATE NO ACTION\n" +
                "        ON DELETE NO ACTION,\n" +
                "    CONSTRAINT workers_degreeId_fkey FOREIGN KEY (degreeId)\n" +
                "        REFERENCES public.degrees (Id) MATCH SIMPLE\n" +
                "        ON UPDATE NO ACTION\n" +
                "        ON DELETE NO ACTION\n" +
                ")\n" +
                "\n" +
                "TABLESPACE pg_default;\n" +
                "\n" +
                "ALTER TABLE public.workers\n" +
                "    OWNER to postgres;";
        PreparedStatement preparedStatement = connect.prepareStatement(SQL);
        preparedStatement.executeUpdate();
        System.out.println("Table successfully created..");

    }
    public int insert(Worker newInstance) throws SQLException {

        String sql="INSERT INTO public.workers(\n" +
                "\tid, firstname, lastname, middlename, birthdate, positionId, degreeId, parentId)\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, newInstance.getId());
        preparedStatement.setString(2, newInstance.getFirstName());
        preparedStatement.setString(3, newInstance.getLastName());
        preparedStatement.setString(4, newInstance.getMiddleName());
        preparedStatement.setString(5, newInstance.getBirthDate());
        preparedStatement.setInt(6, newInstance.getPositionId());
        preparedStatement.setInt(7, newInstance.getDegreeId());
        preparedStatement.setInt(8, newInstance.getParentId());
        return preparedStatement.executeUpdate();
    }


    public int delete(int id) throws SQLException {

        String sql="DELETE FROM public.workers\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        return preparedStatement.executeUpdate();
    }

    public int update(int id, String nameColumn, int newInstance) throws SQLException {

        String sql="UPDATE public.workers\n" +
                "\tSET "+nameColumn+"=?\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1,newInstance);
        preparedStatement.setInt(2,id);
        return preparedStatement.executeUpdate();
    }
    public int update(int id, String nameColumn, String newInstance) throws SQLException {

      String sql="UPDATE public.workers\n" +
              "\tSET "+nameColumn+"=?\n" +
              "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setString(1,newInstance);
        preparedStatement.setInt(2,id);
        return preparedStatement.executeUpdate();
    }
    public int update(HashMap<String,String> map,int id) throws SQLException {
        String sql="UPDATE public.workers\n" +
                "\tSET ";

        int index=map.size();
        for( Map.Entry entry: map.entrySet())
        {
                sql+=entry.getKey()+"=?";
                index--;
                if(index>0)
                    sql+=",";

        }
        sql+=  "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        index=1;
        for( Map.Entry entry: map.entrySet())
        {
            if(((String)entry.getKey()).indexOf("Id")<0)
            {
                preparedStatement.setString(index,(String)entry.getValue());
                index++;
            }
            else
            {
                preparedStatement.setInt(index,Integer.parseInt((String)entry.getValue()));
                index++;
            }

        }
        preparedStatement.setInt(map.size()+1,id);
        return preparedStatement.executeUpdate();
    }

    public List<Worker> select(Map<String,String> map) throws SQLException {
      String sql=selectSQL;
      String whereSQL="\tWHERE ";
      String param="=?";
      String andSQL=" AND ";
      int index=0;
      int number=0;
      for( Map.Entry entry: map.entrySet())
      {
          if(entry.getValue()!=null&&!"empty".equals(entry.getValue())) {
              number++;
          }
      }
          if(number>=1) {
              sql += whereSQL;
              int i=0;
              for (Map.Entry entry : map.entrySet()) {
                  if (entry.getValue() != null && !"empty".equals(entry.getValue())) {
                      i++;
                      sql += entry.getKey()+param;
                      if(number>i)
                          sql+=andSQL;
                  }
              }
          }
        sql+=";";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        for (Map.Entry entry : map.entrySet()) {
            if(entry.getValue()!=null&&!"empty".equals(entry.getValue())) {
                index++;
                preparedStatement.setInt(index,Integer.parseInt((String)entry.getValue()));
            }
        }
        ResultSet result=preparedStatement.executeQuery();
        ArrayList<Worker> listDTO=new ArrayList<Worker>();

        while(result.next())
            listDTO.add(new Worker(result.getString("firstname"),result.getString("birthdate"),result.getInt("id"),result.getString("lastname"),
                    result.getString("middlename"),result.getInt("positionId"),result.getInt("degreeId"),result.getInt("parentId")));
        return listDTO;
    }
    public Worker select(int id) throws SQLException {
        String sql=selectSQL +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet result=preparedStatement.executeQuery();
        Worker selectWorker=null;
        if(result.next())
            selectWorker=new Worker(result.getString("firstname"),result.getString("birthdate"),result.getInt("id"),result.getString("lastname"),
                    result.getString("middlename"),result.getInt("positionId"),result.getInt("degreeId"),result.getInt("parentId"));

        return selectWorker;
    }
    public List selectAll() throws SQLException {
        PreparedStatement preparedStatement = connect.prepareStatement(selectSQL);

        ResultSet result=preparedStatement.executeQuery();
        ArrayList<Worker> listDTO=new ArrayList<Worker>();

        while(result.next())
            listDTO.add(new Worker(result.getString("firstname"),result.getString("birthdate"),result.getInt("id"),result.getString("lastname"),
                    result.getString("middlename"),result.getInt("positionId"),result.getInt("degreeId"),result.getInt("parentId")));
        return  listDTO;

    }

    @Override
    public void truncate() throws SQLException {
        String sql="TRUNCATE TABLE  public.workers;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.execute();

    }
}
