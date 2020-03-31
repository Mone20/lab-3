package com.db.tables;

import com.db.Database;
import com.db.Table;
import model.UniversityPosition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionTable implements Table<UniversityPosition> {
    private static Connection connect = null;
    private String selectSQL="SELECT id,position\n" +
            "\tFROM public.position\n";
    public PositionTable() throws SQLException {
        this.connect= Database.connection;
        String SQL = " CREATE TABLE IF NOT EXISTS public.position\n" +
                "(\n" +
                "    id integer NOT NULL,\n" +
                "    position character varying(20) COLLATE pg_catalog.default NOT NULL,\n" +
                "    CONSTRAINT position_pkey PRIMARY KEY (id)\n" +
                ")\n" +
                "\n" +
                "TABLESPACE pg_default;\n" +
                "\n" +
                "ALTER TABLE public.position\n" +
                "    OWNER to postgres;";
        PreparedStatement preparedStatement = connect.prepareStatement(SQL);
        preparedStatement.executeUpdate();
        System.out.println("com.db.Table successfully created..");

    }

    public int insert(UniversityPosition position) throws SQLException {

        String sql = "INSERT INTO public.position(\n" +
                "\tid, position)\n" +
                "\tVALUES (?, ?);";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, position.getId());
        preparedStatement.setString(2, position.getPosition());
        return preparedStatement.executeUpdate();
    }


    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM public.workers\n" +
                "\tWHERE positionId=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
       sql = "DELETE FROM public.position\n" +
                "\tWHERE id=?;";
         preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        return preparedStatement.executeUpdate();

    }


    public int update(int id, String nameColumn, String newInstance) throws SQLException {

        String sql = "UPDATE public.position\n" +
                "\tSET " + nameColumn + "=?\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setString(1, newInstance);
        preparedStatement.setInt(2, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(HashMap<String, String> map, int id) throws SQLException {
        String sql="UPDATE public.position\n" +
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

    public int update(int id, String nameColumn, int newInstance) throws SQLException {

        String sql = "UPDATE public.position\n" +
                "\tSET " + nameColumn + "=?\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, newInstance);
        preparedStatement.setInt(2, id);
        return preparedStatement.executeUpdate();
    }

    public UniversityPosition select(int id) throws SQLException {
        String sql = "SELECT id, position\n" +
                "\tFROM public.position\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet result = preparedStatement.executeQuery();
        UniversityPosition selectPosition = null;
        if (result.next())
            selectPosition = new UniversityPosition(result.getInt("id"), result.getString("position"));

        return selectPosition;
    }

    @Override
    public List<UniversityPosition> select(Map<String, String> map) throws SQLException {
        return null;
    }

    public List selectAll() throws SQLException {
        PreparedStatement preparedStatement = connect.prepareStatement(selectSQL);

        ResultSet result=preparedStatement.executeQuery();
        ArrayList<UniversityPosition> listDTO=new ArrayList<UniversityPosition>();

        while(result.next())
            listDTO.add(new UniversityPosition(result.getInt("id"),result.getString("position")));
        return  listDTO;

    }

    @Override
    public void truncate() throws SQLException {
        String sql = "TRUNCATE TABLE  public.position CASCADE ;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.execute();

    }


}
