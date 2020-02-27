import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DBServlet extends HttpServlet {
    private Controller controllerWorkers = null;
    private Controller controllerPositions = null;
    private Controller controllerDegrees = null;

    public DBServlet() throws SQLException {

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("xmlCR.jsp").forward(req, resp);
        String action=req.getParameter("action");
        try {
            XMLController xmlC=new XMLController();
        if("submitSaveXML".equals(action))
        {


                xmlC.createXML();

            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        if("submitDownloadXML".equals(action))
        {


            xmlC.readXML();
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}