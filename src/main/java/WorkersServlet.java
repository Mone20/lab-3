
import model.Degree;
import model.UniversityPosition;
import model.Worker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class WorkersServlet extends HttpServlet {
    private  Controller controllerWorkers=null;
    private  Controller controllerPositions=null;
    private  Controller controllerDegrees=null;

    public WorkersServlet() throws SQLException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            if(Database.connection==null)
                Database.connectDB();
            if(controllerWorkers==null) {
                controllerWorkers = new Controller<Worker>(new WorkersTable(Database.connection));
            }
            if(controllerPositions==null) {
                controllerPositions = new Controller<UniversityPosition>(new PositionTable());
            }
            if(controllerDegrees==null) {
                controllerDegrees = new Controller<Degree>(new DegreeTable());
            }

        String action = req.getParameter("action");
            String htmlReq = "";
            boolean flag=false;

            ResultSet result = controllerPositions.selectAll();
            String htmlReqPos = "";
            while (result.next()) {
                htmlReqPos += " <option value=\""+Integer.toString(result.getInt("id"))+"\">"+result.getString("position")+"</option>\n" ;
            }
            req.setAttribute("htmlPos",htmlReqPos);


            result = controllerDegrees.selectAll();
            String htmlReqDeg = "";
            while (result.next()) {
                htmlReqDeg += " <option value=\""+Integer.toString(result.getInt("Id"))+"\">"+result.getString("degree")+"</option>\n" ;
            }
            req.setAttribute("htmlDeg",htmlReqDeg);



        if ("info".equals(action)) {
            flag=true;
            int id=Integer.parseInt(req.getParameter("id"));
            if(id>=0) {

                Worker worker = (Worker) controllerWorkers.select(id);

if(null==((Degree) controllerDegrees.select(worker.getDegreeId())).getDegree()
       ||0>worker.getDegreeId())
    req.setAttribute("degree", "empty");


else
    req.setAttribute("degree", ((Degree) controllerDegrees.select(worker.getDegreeId())).getDegree());

if(null==((UniversityPosition) controllerPositions.select(worker.getPositionId())).getPosition()|| 0>worker.getPositionId())
    req.setAttribute("position", "empty");
    else
    req.setAttribute("position", ((UniversityPosition) controllerPositions.select(worker.getPositionId())).getPosition());


                req.setAttribute("worker", worker);
                req.getRequestDispatcher("info.jsp").forward(req, resp);
            }
            else
                flag=false;
        } else if("update".equals(action)) {

            flag=true;
            int id=Integer.parseInt(req.getParameter("id"));

            result = controllerWorkers.selectAll();
            String htmlReqParent = "";
            while (result.next()) {
                if(result.getInt("id")!=id)
                htmlReqParent += " <option value=\""+Integer.toString(result.getInt("id"))+"\">"+result.getString("lastname")+" "+result.getString("firstname")+" "+
                        result.getString("middlename")+" "+result.getString("birthdate")+"</option>\n" ;
            }
            req.setAttribute("htmlParent",htmlReqParent);

            Worker worker=(Worker)controllerWorkers.select(id);
            req.setAttribute("position", ((UniversityPosition)controllerPositions.select(worker.getPositionId())).getPosition());

            req.setAttribute("degree", ((Degree)controllerDegrees.select(worker.getDegreeId())).getDegree());

            req.setAttribute("worker", worker);
            req.getRequestDispatcher("update.jsp").forward(req, resp);

        }
        else if("create".equals(action))
        {
           /* */
            result = controllerWorkers.selectAll();
            String htmlReqParent = "";
            while (result.next()) {
                htmlReqParent += " <option value=\""+Integer.toString(result.getInt("id"))+"\">"+result.getString("lastname")+" "+result.getString("firstname")+" "+
                        result.getString("middlename")+" "+result.getString("birthdate")+"</option>\n" ;
            }
            req.setAttribute("htmlParent",htmlReqParent);
            flag=true;
            req.getRequestDispatcher("create.jsp").forward(req, resp);

        }
        else if("delete".equals(action))
        {
            int id=Integer.parseInt(req.getParameter("id"));


            Worker w=(Worker)controllerWorkers.select(id);
            ResultSet resultDelete=null;
            if(w.getParentId()<0)
            {
                resultDelete=controllerWorkers.selectAll();
                while( resultDelete.next())
                {
                    if(resultDelete.getInt(8)==id)
                    {
                        controllerWorkers.update(resultDelete.getInt("id"),"\"parentId\"",-2);
                    }
                }
            }

            else
            {
                resultDelete=controllerWorkers.selectAll();
                while( resultDelete.next())
                {
                    if(resultDelete.getInt(8)==id)
                    {
                        controllerWorkers.update(resultDelete.getInt("id"),"\"parentId\"",w.getParentId());
                    }
                }
            }
            controllerWorkers.delete(id);
        }
       if(flag==false)
        {

            result=controllerWorkers.selectAll();

            while (result.next()) {
                htmlReq += "\n<tr>\n" +
                        "        <td>" + Integer.toString(result.getInt("id")) + "</td>\n" +
                        "        <td>" + result.getString("lastname") + "</td>\n" +
                        "        <td>" + result.getString("firstname") + "</td>\n" +
                        "        <td>" + result.getString("middlename") + "</td>\n" +
                        "        <td><a href=\"time?action=info&id=" + Integer.toString(result.getInt("id")) + "\">more...</a></td>\n" +
                        "    </tr>";
            }
            req.setAttribute("select", htmlReq);
            req.getRequestDispatcher("time.jsp").forward(req, resp);
        }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Enter doPost");
        if(Database.connection==null) {
            try {
                Database.connectDB();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        ResultSet result=null;
        PreparedStatement preparedStatement = null;

        try {
            if("createSubmit".equals(action))
            {
                if(controllerWorkers==null)
                    controllerWorkers = new Controller(new WorkersTable(Database.connection));


                ResultSet resultWorkers=controllerWorkers.selectAll();
                int maxId=-1;
                while(resultWorkers.next())
                {
                    if(resultWorkers.getInt("id")>maxId)
                        maxId=resultWorkers.getInt("id");
                }
                String lastName =req.getParameter("lastName");
                String firstName=req.getParameter("firstName");
                String middleName=req.getParameter("middleName");
                String birthDate=req.getParameter("birthDate");
                int positionId=Integer.parseInt(req.getParameter("selectPos"));
                int degreeId=Integer.parseInt(req.getParameter("selectDeg"));
                int parentId=Integer.parseInt(req.getParameter("selectParent"));
                Worker newWorker=new Worker(firstName,birthDate,maxId+1,lastName,middleName,positionId,degreeId,parentId);
                controllerWorkers.insert(newWorker);
                doGet(req,resp);


            }

        if ("submit".equals(action)) {

            int id=Integer.parseInt(req.getParameter("id"));
                if(controllerWorkers==null)
                    controllerWorkers = new Controller(new WorkersTable(Database.connection));

                controllerWorkers.update(id,"lastname",req.getParameter("lastName"));
                controllerWorkers.update(id,"firstname",req.getParameter("firstName"));
                controllerWorkers.update(id,"middlename",req.getParameter("middleName"));
                controllerWorkers.update(id,"birthdate",req.getParameter("birthDate"));
                controllerWorkers.update(id,"\"positionId\"",Integer.parseInt(req.getParameter("selectPos")));
                controllerWorkers.update(id,"\"degreeId\"",Integer.parseInt(req.getParameter("selectDeg")));
                controllerWorkers.update(id,"\"parentId\"",Integer.parseInt(req.getParameter("selectParent")));
                req.setAttribute("worker", controllerWorkers.select(id));
                doGet(req,resp);

        }
        else if("show".equals(action))
        {
            String sql = "";
            if (Database.connection == null)
                Database.connectDB();

           result = controllerPositions.selectAll();
            String htmlReqPos = "";
            while (result.next()) {
                htmlReqPos += " <option value=\""+Integer.toString(result.getInt("id"))+"\">"+result.getString("position")+"</option>\n" ;
            }
            req.setAttribute("htmlPos",htmlReqPos);


            result = controllerDegrees.selectAll();
            String htmlReqDeg = "";
            while (result.next()) {
                htmlReqDeg += " <option value=\""+Integer.toString(result.getInt("Id"))+"\">"+result.getString("degree")+"</option>\n" ;
            }
            req.setAttribute("htmlDeg",htmlReqDeg);


            if ("empty".equals(req.getParameter("selectPos"))||req.getParameter("selectPos")==null) {
                if ("empty".equals(req.getParameter("selectDeg"))) {
                    result = controllerWorkers.selectAll();
                } else {
                    int degId = Integer.parseInt(req.getParameter("selectDeg"));
                    sql = "SELECT id, firstname, lastname, middlename, birthdate, \"positionId\", \"degreeId\", \"parentId\"\n" +
                            "\tFROM public.workers\n" +
                            "\tWHERE  \"degreeId\"=?;";
                    preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, degId);
                    result=preparedStatement.executeQuery();
                }
            } else {
                if ("empty".equals(req.getParameter("selectDeg"))||req.getParameter("selectDeg")==null) {
                    int posId = Integer.parseInt(req.getParameter("selectPos"));
                    sql = "SELECT id, firstname, lastname, middlename, birthdate, \"positionId\", \"degreeId\", \"parentId\"\n" +
                            "\tFROM public.workers\n" +
                            "\tWHERE  \"positionId\"=?;";
                    preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, posId);
                    result=preparedStatement.executeQuery();
                } else {
                    sql = "SELECT id, firstname, lastname, middlename, birthdate, \"positionId\", \"degreeId\", \"parentId\"\n" +
                            "\tFROM public.workers\n" +
                            "\tWHERE  \"positionId\"=? AND \"degreeId\"=? ;";
                    int posId = Integer.parseInt(req.getParameter("selectPos"));
                    int degId = Integer.parseInt(req.getParameter("selectDeg"));
                    preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, posId);
                    preparedStatement.setInt(2, degId);
                    result=preparedStatement.executeQuery();

                }
            }

            String htmlReq = "";
            while (result.next()) {
                htmlReq += "\n<tr>\n" +
                        "        <td>" + Integer.toString(result.getInt("id")) + "</td>\n" +
                        "        <td>" + result.getString("lastname") + "</td>\n" +
                        "        <td>" + result.getString("firstname") + "</td>\n" +
                        "        <td>" + result.getString("middlename") + "</td>\n" +
                        "        <td><a href=\"time?action=info&id=" + Integer.toString(result.getInt("id")) + "\">more...</a></td>\n" +
                        "    </tr>";
            }
            req.setAttribute("select", htmlReq);

        }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/time.jsp").forward(req, resp);
    }

    }
