package servlets;

import beans.XMLWorkerStatelessBean;
import com.db.Controller;
import com.db.Database;
import com.db.tables.DegreeTable;
import com.db.tables.PositionTable;
import com.db.tables.WorkersTable;
import model.Degree;
import model.UniversityPosition;
import model.Worker;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MultipartConfig

public class DBServlet extends HttpServlet {
    @EJB
    XMLWorkerStatelessBean xmlWorkerStatelessBean;
    private Controller controllerWorkers;
    private Controller controllerPositions;
    private Controller controllerDegrees;
    public DBServlet() throws SQLException {

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("text"," ");
        String action=req.getParameter("action");
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("xmlCR.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action=request.getParameter("action");
        try {
            if (Database.connection == null)
                Database.connectDB();
            if (controllerWorkers == null) {
                controllerWorkers = new Controller<Worker>(new WorkersTable(Database.connection));
            }

            switch (action) {
            case "importXML": Part filePart = request.getPart("file");
            InputStream fileContent = filePart.getInputStream();

               HashMap<Worker,Worker> conflictMap= xmlWorkerStatelessBean.readXML(fileContent, filePart.getSubmittedFileName());
               String htmlConflict="";
                Worker newInstance;
                Worker prevInstance;
               for(Map.Entry entry:conflictMap.entrySet())
               {
                   newInstance=(Worker)entry.getValue();
                   prevInstance=(Worker)entry.getKey();
                   htmlConflict+="<section>"+"СОТРУДНИК №"+prevInstance.getId()+"</section>";
                   String prevBoss;
                   if(controllerWorkers.select(prevInstance.getId())==null)
                    prevBoss=((Worker)controllerWorkers.select(prevInstance.getParentId())).getLastName()+" "
                           +((Worker)controllerWorkers.select(prevInstance.getParentId())).getFirstName()+" "+((Worker)controllerWorkers.select(prevInstance.getParentId())).getMiddleName();
                   else
                       prevBoss="EMPTY";
                   String newBoss;
                   if(controllerWorkers.select(newInstance.getId())==null)
                   newBoss="|BOSS:"+((Worker)controllerWorkers.select(newInstance.getParentId())).getLastName()+" "
                           +((Worker)controllerWorkers.select(newInstance.getParentId())).getFirstName()+" "+((Worker)controllerWorkers.select(newInstance.getParentId())).getMiddleName();
                   else
                       newBoss="EMPTY";

                   htmlConflict+="<p>"+prevInstance.getInf()+"|"+((UniversityPosition)controllerPositions.select(prevInstance.getPositionId())).getPosition()+
                           "|"+((Degree)controllerDegrees.select(prevInstance.getDegreeId())).getDegree()+"|BOSS:"+prevBoss;

                   htmlConflict+="\n<section></section>\n"+"  <section><input  type=\"checkbox\" name=\"checkbox\" value=\""+prevInstance.getId()+"\" />Заменить</section>";
                   htmlConflict+=newInstance.getInf()+"|"+((UniversityPosition)controllerPositions.select(newInstance.getPositionId())).getPosition()+
                           "|"+((Degree)controllerDegrees.select(newInstance.getDegreeId())).getDegree()+"|BOSS:"+newBoss;

                   htmlConflict+="</p>";


               }
               fileContent.close();

               if(!conflictMap.isEmpty()) {
                   request.setAttribute("htmlConflict", htmlConflict);
                   request.getRequestDispatcher("conflict.jsp").forward(request, response);
               }
               else
                   request.getRequestDispatcher("index.jsp").forward(request, response);
            break;

                case "submitConflict":
                   int id;
                   String [] arrayOfCheck=request.getParameterValues("checkbox");
                   if(arrayOfCheck!=null) {
                       for (String s : arrayOfCheck) {
                           id = Integer.parseInt(s);
                           for (Worker w : xmlWorkerStatelessBean.getConflictMap().values()) {
                               if (w.getId() == id) {
                                   controllerWorkers.delete(id);
                                   controllerWorkers.insert(w);
                               }
                           }
                       }
                   }
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    break;
            case "exportXML":
                HashMap<String,String> map= new HashMap<>();
                Enumeration<String> attributesNames=request.getParameterNames();
                while(attributesNames.hasMoreElements())
                {
                    String item=attributesNames.nextElement();
                    int p=item.indexOf("_");
                    if(p>0) {
                        String suff=item.substring(p,item.length());
                        if("_filter".equals(suff)) {
                            String name = item.substring(0, p);
                            System.out.println(name);
                            map.put(name, request.getParameter(item));
                        }
                    }
                }
                if(!map.isEmpty()) {
                    System.out.println(controllerWorkers.select(map));
                    List<Worker> list=controllerWorkers.select(map);
                    xmlWorkerStatelessBean.createXML(list);
                }
                else
                    xmlWorkerStatelessBean.createXML(controllerWorkers.selectAll());
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }





    }
}