import model.Degree;
import model.UniversityPosition;
import model.Worker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public abstract class XmlPositionBean implements SessionBean {
    /**
     * @ejb:create-method
     */
    private Controller controllerPositions = null;

    public void ejbCreate() throws CreateException, NamingException {
        try {
            Context initial = new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void createXML() throws SQLException, ClassNotFoundException {
        if (Database.connection == null)
            Database.connectDB();

        if (controllerPositions == null) {
            controllerPositions = new Controller<UniversityPosition>(new PositionTable());
        }


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            List<UniversityPosition> resultPositions = controllerPositions.selectAll();
            Element rootElement = doc.createElement("Tables");
            // добавляем корневой элемент в объект Document
            doc.appendChild(rootElement);

            Element rootElementPos = doc.createElement("Positions");
            rootElement.appendChild(rootElementPos);
            for (int i = 0; i < resultPositions.size(); i++) {
                rootElementPos.appendChild(getPosition(doc, (UniversityPosition) controllerPositions.select(resultPositions.get(i).getId())));
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            File f = new File("fileXMLPosition.xml");
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            } else
                f.createNewFile();
            if (validationXML(source, f.getName())) {
                StreamResult file = new StreamResult(f);
                transformer.transform(source, file);
                StreamResult console = new StreamResult(System.out);
                if (Files.exists(Paths.get("E://test/fileXMLPosition.xml")))
                    Files.delete(Paths.get("E://test/fileXMLPosition.xml"));

                Files.copy(Paths.get(f.toURI()), Paths.get("E://test/fileXMLPosition.xml"));
                //записываем данные
                transformer.transform(source, console);
                transformer.transform(source, file);

                System.out.println("Создание XML файла закончено");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validationXML(DOMSource docSource, String fileXMLName) throws SAXException {

        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");


        File schemaLocation = new File("docXSD.xsd");
        Schema schema = factory.newSchema(schemaLocation);
        Validator validator = schema.newValidator();
        try {
            validator.validate(docSource);
            System.out.println(fileXMLName + " is valid.");

        } catch (SAXException ex) {
            System.out.println(fileXMLName + " is not valid because ");
            System.out.println(ex.getMessage());
            return false;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static Node getElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    private static Node getPosition(Document doc, UniversityPosition p) {
        Element pos = doc.createElement("UniversityPosition");


        pos.setAttribute("id", Integer.toString(p.getId()));
        pos.appendChild(getElements(doc, pos, "position", p.getPosition()));

        return pos;
    }

    private static UniversityPosition getPosition(Node node) {
        UniversityPosition position = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            position = new UniversityPosition(Integer.parseInt(element.getAttribute("id")),
                    getTagValue("position", element));

        }

        return position;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }


}