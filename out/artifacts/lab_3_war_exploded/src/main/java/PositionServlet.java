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
import java.util.List;

public class PositionServlet extends HttpServlet {

    private Controller controllerPositions = null;

    public PositionServlet() throws SQLException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            if (Database.connection == null)
                Database.connectDB();
            if (controllerPositions == null) {
                controllerPositions = new Controller<UniversityPosition>(new PositionTable());
            }

            String action = req.getParameter("action");
            String htmlReq = "";
            List result = null;

            if (action != null) {
                switch (action) {
                    case "update":
                        int id = Integer.parseInt(req.getParameter("id"));
                        result = controllerPositions.selectAll();
                        String htmlReqParent = "";
                        for (int i = 0; i < result.size(); i++) {
                            int idSelect = (((UniversityPosition) result.get(i)).getId());
                            if (idSelect != id) {
                                String position = (((UniversityPosition) result.get(i)).getPosition());
                                htmlReqParent += " <option value=\"" + Integer.toString(idSelect) + "\">" + position + "</option>\n";
                            }
                        }
                        req.setAttribute("htmlParent", htmlReqParent);
                        break;
                    case "createPosition":
                        result = controllerPositions.selectAll();
                        htmlReqParent = "";
                        for (int i = 0; i < result.size(); i++) {
                            int idSelect = (((Worker) result.get(i)).getId());
                            String position = (((UniversityPosition) result.get(i)).getPosition());
                            htmlReqParent += " <option value=\"" + Integer.toString(idSelect) + "\">" + position + "</option>\n";
                        }
                        req.setAttribute("htmlParent", htmlReqParent);
                        req.getRequestDispatcher("createPosition.jsp").forward(req, resp);
                        break;
                    case "delete":
                        id = Integer.parseInt(req.getParameter("id"));
//                        System.out.println("Here comes the trouble");
                        System.out.println(id);
                        controllerPositions.delete(id);
                        break;
                }
            }
            result = controllerPositions.selectAll();
            for (int i = 0; i < result.size(); i++) {
                UniversityPosition pos = (UniversityPosition) result.get(i);
                htmlReq += "\n<tr>\n" +
                        " <td>" + Integer.toString(pos.getId()) + "</td>\n" +
                        " <td>" + pos.getPosition() + "</td>\n" +
                        " <td><a href=\"position?action=delete&id=" + Integer.toString(pos.getId()) + "\">delete</a></td>\n" +
                        " </tr>";
            }
            req.setAttribute("select", htmlReq);
            System.out.println("Что то сделали");
            req.getRequestDispatcher("position.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Enter doPost after something");
        if (Database.connection == null) {
            try {
                Database.connectDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        List result = null;
        PreparedStatement preparedStatement = null;

        try {
            if ("createPosition".equals(action)) {
//                if (controllerPositions == null)
//                    controllerPositions = new Controller(new PositionTable(Database.connection));

                controllerPositions.selectAll();
                List resultPosition = controllerPositions.selectAll();
                int maxId = -1;
                for (int i = 0; i < result.size(); i++) {
                    UniversityPosition pos = (UniversityPosition) resultPosition.get(i);
                    if (pos.getId() > maxId)
                        maxId = pos.getId();
                }
                String position = req.getParameter("position");
                UniversityPosition newPosition = new UniversityPosition(maxId + 1, position);
                controllerPositions.insert(newPosition);
                System.out.println("forward here");
                result = controllerPositions.selectAll();
                String htmlReq = "";
                for (int i = 0; i < result.size(); i++) {
                    UniversityPosition pos = (UniversityPosition) resultPosition.get(i);
                    htmlReq += "\n<tr>\n" +
                            " <td>" + Integer.toString(pos.getId()) + "</td>\n" +
                            " <td>" + pos.getPosition() + "</td>\n" +
                            " <td><a href=\"position?action=delete&id=" + Integer.toString(pos.getId()) + "\">delete</a></td>\n" +
                            " </tr>";
                }
                req.setAttribute("select", htmlReq);

                req.getRequestDispatcher("position.jsp").forward(req, resp);

            }
//update
            if ("submit".equals(action)) {

                int id = Integer.parseInt(req.getParameter("id"));
                if (controllerPositions == null)
                    controllerPositions = new Controller(new PositionTable());

                controllerPositions.update(id, "position", req.getParameter("position"));
                doGet(req, resp);
                req.getRequestDispatcher("position.jsp").forward(req, resp);
            } //TODO СТАРОЕ
//                if ("submit".equals(action)) {
//
//                    int id = Integer.parseInt(req.getParameter("id"));
//                    if (controllerPositions == null)
//                        controllerPositions = new Controller(new DegreeTable(Database.connection));
//
//                    controllerPositions.update(id, "degree", req.getParameter("degree"));
//                    doGet(req, resp);
//                    req.getRequestDispatcher("degree.jsp").forward(req, resp);
//                }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}

