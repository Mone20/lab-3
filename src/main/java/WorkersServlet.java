
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;



public class WorkersServlet extends HttpServlet {
    public static final String POSITION_ID = "positionId";
    public static final String DEGREE_ID = "degreeId";
    public static final String PARENT_ID = "parentId";
    public static final String FILTER="_filter";
    private  Controller controllerWorkers=null;
    private  Controller controllerPositions=null;
    private  Controller controllerDegrees=null;

    public WorkersServlet() throws SQLException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            if (Database.connection == null)
                Database.connectDB();
            if (controllerPositions == null) {
                controllerPositions = new Controller<UniversityPosition>(new PositionTable());
            }
            if (controllerDegrees == null) {
                controllerDegrees = new Controller<Degree>(new DegreeTable());
            }
            if (controllerWorkers == null) {
                controllerWorkers = new Controller<Worker>(new WorkersTable(Database.connection));
            }


            String action = req.getParameter("action");
            String htmlReq = "";
            boolean flag = false;
            int id;
            List result = controllerPositions.selectAll();
            String htmlReqPos = "";
            String htmlReqParent = " ";
            for (int i = 0; i < result.size(); i++) {
                id = (((UniversityPosition) result.get(i)).getId());
                String pos = (((UniversityPosition) result.get(i)).getPosition());
                htmlReqPos += " <option value=\"" + Integer.toString(id) + "\">" + pos + "</option>\n";
            }
            req.setAttribute("htmlPos", htmlReqPos);


            result = controllerDegrees.selectAll();
            String htmlReqDeg = "";
            for (int i = 0; i < result.size(); i++) {
                id = (((Degree) result.get(i)).getId());
                String deg = (((Degree) result.get(i)).getDegree());
                htmlReqDeg += " <option value=\"" + Integer.toString(id) + "\">" + deg + "</option>\n";
            }
            req.setAttribute("htmlDeg", htmlReqDeg);
            if (action != null)
            {
                switch (action) {

                    case "info":
                        flag = true;
                        id = Integer.parseInt(req.getParameter("id"));
                        if (id >= 0) {

                            Worker worker = (Worker) controllerWorkers.select(id);

                            if (null == ((Degree) controllerDegrees.select(worker.getDegreeId())).getDegree()
                                    || 0 > worker.getDegreeId())
                                req.setAttribute("degree", "empty");


                            else
                                req.setAttribute("degree", ((Degree) controllerDegrees.select(worker.getDegreeId())).getDegree());

                            if (null == ((UniversityPosition) controllerPositions.select(worker.getPositionId())).getPosition() || 0 > worker.getPositionId())
                                req.setAttribute("position", "empty");
                            else
                                req.setAttribute("position", ((UniversityPosition) controllerPositions.select(worker.getPositionId())).getPosition());


                            req.setAttribute("worker", worker);
                            req.getRequestDispatcher("info.jsp").forward(req, resp);
                        } else
                            flag = false;
                        break;


                    case "update":
                        flag = true;
                        id = Integer.parseInt(req.getParameter("id"));

                        result = controllerWorkers.selectAll();
                        htmlReqParent = "";
                        for (int i = 0; i < result.size(); i++) {
                            int idSelect = (((Worker) result.get(i)).getId());

                            if (idSelect != id) {
                                String lastName = (((Worker) result.get(i)).getLastName());
                                String firstName = (((Worker) result.get(i)).getFirstName());
                                String middleName = (((Worker) result.get(i)).getMiddleName());
                                String birthDate = (((Worker) result.get(i)).getBirthDate());
                                htmlReqParent += " <option value=\"" + Integer.toString(idSelect) + "\">" + lastName + " " + firstName + " " +
                                        middleName + " " + birthDate + "</option>\n";
                            }
                        }
                        req.setAttribute("htmlParent", htmlReqParent);

                        Worker worker = (Worker) controllerWorkers.select(id);
                        req.setAttribute("position", ((UniversityPosition) controllerPositions.select(worker.getPositionId())).getPosition());

                        req.setAttribute("degree", ((Degree) controllerDegrees.select(worker.getDegreeId())).getDegree());

                        req.setAttribute("worker", worker);
                        req.getRequestDispatcher("update.jsp").forward(req, resp);
                        break;


                    case "create":
                        /* */
                        result = controllerWorkers.selectAll();
                        htmlReqParent = "";
                        for (int i = 0; i < result.size(); i++) {
                            int idSelect = (((Worker) result.get(i)).getId());


                            String lastName = (((Worker) result.get(i)).getLastName());
                            String firstName = (((Worker) result.get(i)).getFirstName());
                            String middleName = (((Worker) result.get(i)).getMiddleName());
                            String birthDate = (((Worker) result.get(i)).getBirthDate());
                            htmlReqParent += " <option value=\"" + Integer.toString(idSelect) + "\">" + lastName + " " + firstName + " " +
                                    middleName + " " + birthDate + "</option>\n";

                        }
                        req.setAttribute("htmlParent", htmlReqParent);
                        flag = true;
                        req.getRequestDispatcher("create.jsp").forward(req, resp);
                        break;


                    case "delete":
                        id = Integer.parseInt(req.getParameter("id"));


                        Worker w = (Worker) controllerWorkers.select(id);
                        List resultDelete = null;
                        if (w.getParentId() < 0) {
                            resultDelete = controllerWorkers.selectAll();
                            for (int i = 0; i < resultDelete.size(); i++) {
                                if (((Worker) resultDelete.get(i)).getId() == id) {
                                    controllerWorkers.update(((Worker) resultDelete.get(i)).getId(), PARENT_ID, -2);
                                }

                            }

                        } else {
                            resultDelete = controllerWorkers.selectAll();
                            for (int i = 0; i < resultDelete.size(); i++) {
                                if (((Worker) resultDelete.get(i)).getId() == id) {
                                    controllerWorkers.update(((Worker) resultDelete.get(i)).getId(), PARENT_ID, w.getParentId());
                                }

                            }
                        }
                        controllerWorkers.delete(id);
                        break;

                }
        }

       if(flag==false)
        {

            result=controllerWorkers.selectAll();
            for(int i=0;i<result.size();i++)
            {
                Worker w=(Worker)result.get(i);
                htmlReq += "\n<tr>\n" +
                        "        <td>" + Integer.toString(w.getId()) + "</td>\n" +
                        "        <td>" + w.getLastName() + "</td>\n" +
                        "        <td>" + w.getFirstName() + "</td>\n" +
                        "        <td>" + w.getMiddleName() + "</td>\n" +
                        "        <td><a href=\"time?action=info&id=" + Integer.toString(w.getId()) + "\">more...</a></td>\n" +
                        "    </tr>";
            }
            req.setAttribute("select", htmlReq);
            req.getRequestDispatcher("time.jsp").forward(req, resp);
        }
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        List result=null;
        PreparedStatement preparedStatement = null;

        try {
            switch(action)
            {
                case "createSubmit":
                    if(controllerWorkers==null)
                        controllerWorkers = new Controller(new WorkersTable(Database.connection));


                    List resultWorkers=controllerWorkers.selectAll();

                    int maxId=-1;
                    for(int i=0;i<resultWorkers.size();i++)
                    {
                        Worker w=(Worker)resultWorkers.get(i);
                        if(w.getId()>maxId)
                            maxId=w.getId();
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
                    break;




                case "submit":
                    int id=Integer.parseInt(req.getParameter("id"));
                    if(controllerWorkers==null)
                        controllerWorkers = new Controller(new WorkersTable(Database.connection));
                    HashMap<String,String> mapUpdate=new HashMap<>();
                    mapUpdate.put("lastname",req.getParameter("lastName"));
                    mapUpdate.put("firstname",req.getParameter("firstName"));
                    mapUpdate.put("middlename",req.getParameter("middleName"));
                    mapUpdate.put("birthdate",req.getParameter("birthDate"));
                    mapUpdate.put(POSITION_ID,req.getParameter(POSITION_ID));
                    mapUpdate.put( DEGREE_ID,req.getParameter(DEGREE_ID));
                    mapUpdate.put( PARENT_ID,req.getParameter(PARENT_ID));
                    controllerWorkers.update(mapUpdate,id);
                    req.setAttribute("worker", controllerWorkers.select(id));
                    doGet(req,resp);
                    break;




                case "show":
                    String sql = "";
                    if (Database.connection == null)
                        Database.connectDB();

                    result = controllerPositions.selectAll();
                    String htmlReqPos = "";
                    for(int i=0;i<result.size();i++) {
                        UniversityPosition up=(UniversityPosition) result.get(i);
                        htmlReqPos += " <option value=\""+Integer.toString(up.getId())+"\">"+up.getPosition()+"</option>\n" ;
                    }
                    req.setAttribute("htmlPos",htmlReqPos);


                    result = controllerDegrees.selectAll();
                    String htmlReqDeg = "";
                    for(int i=0;i<result.size();i++)  {
                        Degree d=(Degree)result.get(i);
                        htmlReqDeg += " <option value=\""+Integer.toString(d.getId())+"\">"+d.getDegree()+"</option>\n" ;
                    }
                    req.setAttribute("htmlDeg",htmlReqDeg);
                    HashMap <String,String> map= new HashMap<>();
                    Enumeration<String> attributesNames=req.getParameterNames();
                    while(attributesNames.hasMoreElements())
                    {
                        String item=attributesNames.nextElement();
                        int p=item.indexOf("_");
                        if(p>0) {
                            String suff=item.substring(p,item.length());
                            if("_filter".equals(suff)) {
                                String name = item.substring(0, p);
                                System.out.println(name);
                                map.put(name, req.getParameter(item));
                            }
                        }
                    }
                    List<Worker> list=controllerWorkers.select(map);
                    ResultSet  resultQuery=null;
                    String htmlReq = "";
                    for(Worker w:list) {
                        htmlReq += "\n<tr>\n" +
                                "        <td>" + Integer.toString(w.getId()) + "</td>\n" +
                                "        <td>" + w.getLastName()+ "</td>\n" +

                                "        <td>" + w.getFirstName()+ "</td>\n" +
                                "        <td>" + w.getMiddleName() + "</td>\n" +
                                "        <td><a href=\"time?action=info&id=" + Integer.toString(w.getId()) + "\">more...</a></td>\n" +
                                "    </tr>";
                    }
                    req.setAttribute("select", htmlReq);
                    break;

                default:break;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/time.jsp").forward(req, resp);
    }

    }
