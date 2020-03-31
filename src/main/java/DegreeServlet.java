import model.Degree;
import model.Worker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DegreeServlet extends HttpServlet {

    private Controller controllerDegrees = null;
    private  Controller controllerWorkers=null;


    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            if (Database.connection == null)
                Database.connectDB();
            if (controllerDegrees == null) {
                controllerDegrees = new Controller<Degree>(new DegreeTable(Database.connection));
            }
            if (controllerWorkers == null) {
                controllerWorkers = new Controller<Worker>(new WorkersTable(Database.connection));
            }

            String action = req.getParameter("action");
            String htmlReq = "";
            ResultSet result = null;

            if (action != null) {
                switch (action) {
                    case ("update"): {
                        int id = Integer.parseInt(req.getParameter("id"));
                        result = controllerDegrees.selectAll();
                        String htmlReqParent = "";
                        while (result.next()) {
                            if (result.getInt("id") != id) {
                                htmlReqParent += " <option value=\"" + Integer.toString(result.getInt("id")) + "\">" + result.getString("degree") + "</option>\n";
                            }
                        }
                        req.setAttribute("htmlParent", htmlReqParent);
                        break;
                    }
                    case ("createDegree"): {
                        result = controllerDegrees.selectAll();
                        String htmlReqParent = "";
                        while (result.next()) {
                            htmlReqParent += " <option value=\"" + Integer.toString(result.getInt("id")) + "\">" + result.getString("degree") + "</option>\n";
                        }
                        req.setAttribute("htmlParent", htmlReqParent);
                        req.getRequestDispatcher("createDegree.jsp").forward(req, resp);
                        break;
                    }
                    case ("delete"): {
                        int id = Integer.parseInt(req.getParameter("id"));
                        controllerDegrees.delete(id);
                        break;
                    }
                }
            }
                result = controllerDegrees.selectAll();
                while (result.next()) {
                    htmlReq += "\n<tr>\n" +
                            "        <td>" + Integer.toString(result.getInt("id")) + "</td>\n" +
                            "        <td>" + result.getString("degree") + "</td>\n" +
                            "        <td><a href=\"degree?action=delete&id=" + Integer.toString(result.getInt("id")) + "\">del...</a></td>\n" +
                            "        <td><a href=\"updateD?action=submit&id=" + Integer.toString(result.getInt("id")) + "\">update</a></td>\n" +
                            "    </tr>";
                }
                req.setAttribute("select", htmlReq);
               req.getRequestDispatcher("degree.jsp").forward(req, resp);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Enter doPost");
        if (Database.connection == null) {
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
        ResultSet result = null;
        PreparedStatement preparedStatement = null;

        try {
            if (action!= null) {
                switch (action) {
                    case ("createDegree"): {
                        if (controllerDegrees == null)
                            controllerDegrees = new Controller(new DegreeTable(Database.connection));

                        controllerDegrees.selectAll();
                        ResultSet resultDegree = controllerDegrees.selectAll();
                        int maxId = -1;
                        String htmlReq1 = "";
                        while (resultDegree.next()) {
                            if (resultDegree.getInt("id") > maxId)
                                maxId = resultDegree.getInt("id");
                        }
                        String degree = req.getParameter("degree");
                        Degree newDegree = new Degree(maxId + 1, degree);
                        controllerDegrees.insert(newDegree);
//                        ResultSet resultDegree1 = controllerDegrees.selectAll();
//                        while (resultDegree1.next()) {
//                            htmlReq1 += "\n<tr>\n" +
//                                    "        <td>" + Integer.toString(resultDegree1.getInt("id")) + "</td>\n" +
//                                    "        <td>" + resultDegree1.getString("degree") + "</td>\n" +
//                                    "        <td><a href=\"degree?action=delete&id=" + Integer.toString(resultDegree1.getInt("id")) + "\">delete</a></td>\n" +
//                                    "        <td><a href=\"updateD?action=submit&id=" + Integer.toString(resultDegree1.getInt("id")) + "\">update</a></td>\n" +
//                                    "    </tr>";
//                        }
//                        req.setAttribute("select", htmlReq1);
//
//                        req.getRequestDispatcher("degree.jsp").forward(req, resp);
                    break;
                    }
                    //update
                    case ("submit"): {
                        int id = Integer.parseInt(req.getParameter("id"));
                        if (controllerDegrees == null)
                            controllerDegrees = new Controller(new DegreeTable(Database.connection));

                        controllerDegrees.update(id, "degree", req.getParameter("degree"));

                        req.getRequestDispatcher("degree.jsp").forward(req, resp);
                        break;
                    }
                }
            }
            String htmlReq = "";
            result = controllerDegrees.selectAll();
            while (result.next()) {
                htmlReq += "\n<tr>\n" +
                        "        <td>" + Integer.toString(result.getInt("id")) + "</td>\n" +
                        "        <td>" + result.getString("degree") + "</td>\n" +
                        "        <td><a href=\"degree?action=delete&id=" + Integer.toString(result.getInt("id")) + "\">del...</a></td>\n" +
                        "        <td><a href=\"updateD?action=submit&id=" + Integer.toString(result.getInt("id")) + "\">update</a></td>\n" +
                        "    </tr>";
                req.setAttribute("select", htmlReq);
            }
            req.getRequestDispatcher("degree.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}