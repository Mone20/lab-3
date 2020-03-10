import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

@MultipartConfig
public class DBServlet extends HttpServlet {
    private  XMLController xmlC=new XMLController();
    public DBServlet() throws SQLException {

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("xmlCR.jsp").forward(req, resp);
        req.setAttribute("text"," ");
        String action=req.getParameter("action");
        try {

        if("submitSaveXML".equals(action))
        {

            xmlC.createXML();


        }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            try {

                    Part filePart = request.getPart("file");
                    InputStream fileContent = filePart.getInputStream();
                    xmlC.readXML(fileContent,filePart.getSubmittedFileName());
                    fileContent.close();


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher("index.jsp").forward(request, response);




    }
}