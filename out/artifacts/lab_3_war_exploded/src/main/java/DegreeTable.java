import model.Degree;
import model.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DegreeTable implements Table<Degree> {
    private static Connection connect=null;
    public DegreeTable() throws SQLException {
        this.connect = Database.connection;
        String SQL=" CREATE TABLE IF NOT EXISTS public.degrees\n" +
                "(\n" +
                "    Id integer NOT NULL,\n" +
                "    degree character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    CONSTRAINT degree_pkey PRIMARY KEY (Id)\n" +
                ")\n" +
                "\n" +
                "TABLESPACE pg_default;\n" +
                "\n" +
                "ALTER TABLE public.degrees\n" +
                "    OWNER to postgres;";
        PreparedStatement preparedStatement = connect.prepareStatement(SQL);
        preparedStatement.executeUpdate();


        System.out.println("Table successfully created..");

    }
    public int insert(Degree d) throws SQLException {

        String sql="INSERT INTO public.degrees(\n" +
                "\tId, degree)\n" +
                "\tVALUES (?, ?);";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, d.getId());
        preparedStatement.setString(2, d.getDegree());
        return preparedStatement.executeUpdate();
    }


    public int delete(int id) throws SQLException {
        Connection connect=null;
        String sql="DELETE FROM public.degrees\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        return preparedStatement.executeUpdate();
    }
    public int update(int id, String nameColumn, int newInstance) throws SQLException {

        String sql="UPDATE public.degrees\n" +
                "\tSET "+nameColumn+"=?\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1,newInstance);
        preparedStatement.setInt(2,id);
        return preparedStatement.executeUpdate();
    }

    public int update(int id, String nameColumn, String newInstance) throws SQLException {

        String sql="UPDATE public.degrees\n" +
                "\tSET "+nameColumn+"=?\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setString(1,newInstance);
        preparedStatement.setInt(2,id);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(HashMap<String, String> map, int id) throws SQLException {
        return 0;
    }

    public Degree select(int id) throws SQLException {
        String sql="SELECT Id, degree\n" +
                "\tFROM public.degrees" +
                "\tWHERE Id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet result=preparedStatement.executeQuery();
        Degree selectDegree=null;
        if(result.next())
         selectDegree =new Degree( result.getInt("id"),result.getString("degree"));
        return selectDegree;

    }

    @Override
    public List<Degree> select(Map<String, String> map) throws SQLException {
        return null;
    }

    public List selectAll() throws SQLException {
        String sql="SELECT Id, degree\n" +
                "\tFROM public.degrees";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);

        ResultSet result=preparedStatement.executeQuery();
        ArrayList<Degree> listDTO=new ArrayList<Degree>();

        while(result.next())
            listDTO.add(new Degree( result.getInt("id"),result.getString("degree")));
        return  listDTO;


    }
    @Override
    public void truncate() throws SQLException {
        String sql="TRUNCATE TABLE   public.degrees CASCADE ;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.execute();

    }
}
