import model.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkersTable  implements Table<Worker>{
    private Connection connect=null;
    public WorkersTable(Connection connect) throws SQLException {
        this.connect=connect;
        String SQL="  CREATE TABLE IF NOT EXISTS public.workers\n" +
                "(\n" +
                "    id integer NOT NULL,\n" +
                "    firstname character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    lastname character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    middlename character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    birthdate character varying(20) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    \"positionId\" integer,\n" +
                "    \"degreeId\" integer,\n" +
                "    \"parentId\" integer,\n" +
                "    CONSTRAINT workers_pkey PRIMARY KEY (id),\n" +
                "    CONSTRAINT \"positionId\" FOREIGN KEY (\"positionId\")\n" +
                "        REFERENCES public.\"position\" (id) MATCH SIMPLE\n" +
                "        ON UPDATE NO ACTION\n" +
                "        ON DELETE NO ACTION,\n" +
                "    CONSTRAINT \"workers_degreeId_fkey\" FOREIGN KEY (\"degreeId\")\n" +
                "        REFERENCES public.degrees (\"Id\") MATCH SIMPLE\n" +
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
                "\tid, firstname, lastname, middlename, birthdate, \"positionId\", \"degreeId\", \"parentId\")\n" +
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

    public Worker select(int id) throws SQLException {
String sql="SELECT id, firstname, lastname, middlename, birthdate, \"positionId\", \"degreeId\", \"parentId\"\n" +
        "\tFROM public.workers\n" +
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
    public ResultSet selectAll() throws SQLException {
        String sql="SELECT id, firstname, lastname, middlename, birthdate, \"positionId\", \"degreeId\", \"parentId\"\n" +
                "\tFROM public.workers\n"
                ;
        PreparedStatement preparedStatement = connect.prepareStatement(sql);

        ResultSet result=preparedStatement.executeQuery();
        return  result;

    }
}
