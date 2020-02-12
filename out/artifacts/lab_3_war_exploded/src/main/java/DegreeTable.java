import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DegreeTable implements Table<Degree> {
    private static Connection connect=null;
    public DegreeTable() throws SQLException {
        String SQL=" CREATE TABLE IF NOT EXISTS public.degrees\n" +
                "(\n" +
                "    \"Id\" integer NOT NULL,\n" +
                "    degree character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    CONSTRAINT degree_pkey PRIMARY KEY (\"Id\")\n" +
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
                "\t\"Id\", degree)\n" +
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


    public int update(int id, String nameColumn, String newInstance) throws SQLException {

        String sql="UPDATE public.degrees\n" +
                "\tSET "+nameColumn+"=?\n" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setString(1,newInstance);
        preparedStatement.setInt(2,id);
        return preparedStatement.executeUpdate();
    }
    public Degree select(int id) throws SQLException {
        String sql="SELECT \"Id\", degree\n" +
                "\tFROM public.degrees" +
                "\tWHERE id=?;";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet result=preparedStatement.executeQuery(sql);
        Degree selectDegree =new Degree( result.getInt("id"),result.getString("degree"));
        return selectDegree;

    }
}
