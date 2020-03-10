import model.Degree;
import model.UniversityPosition;
import model.Worker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class XMLController {
    private  Controller controllerWorkers=null;
    private  Controller controllerPositions=null;
    private  Controller controllerDegrees=null;

    public void createXML() throws SQLException, ClassNotFoundException {
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


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();


            Document doc = builder.newDocument();
            List<Worker> resultWorkers=controllerWorkers.selectAll();
            List<UniversityPosition> resultPositions=controllerPositions.selectAll();
            List<Degree> resultDegrees=controllerDegrees.selectAll();
            Element rootElement =
                    doc.createElement( "Tables");
            // добавляем корневой элемент в объект Document
            doc.appendChild(rootElement);
            Element rootElementWorkers=doc.createElement("Workers");
            rootElement.appendChild(rootElementWorkers);
          for(int i=0;i<resultWorkers.size();i++)
            {
                rootElementWorkers.appendChild(getWorker(doc,(Worker) controllerWorkers.select(resultWorkers.get(i).getId())));

            }


            Element rootElementPos=doc.createElement("Positions");
            rootElement.appendChild(rootElementPos);
            for(int i=0;i<resultPositions.size();i++)
            {
                rootElementPos.appendChild(getPosition(doc,(UniversityPosition) controllerPositions.select(resultPositions.get(i).getId())));

            }

            Element rootElementDeg=doc.createElement("Degrees");
            rootElement.appendChild(rootElementDeg);
            for(int i=0;i<resultDegrees.size();i++)
            {
                rootElementDeg.appendChild(getDegree(doc,(Degree) controllerDegrees.select(resultDegrees.get(i).getId())));

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            File f=new File("fileXML.xml");
            if(f.exists())
            {
                f.delete();
                f.createNewFile();
            }
            else
                f.createNewFile();
            if(validationXML(source,f.getName())) {
                StreamResult file = new StreamResult(f);
                transformer.transform(source, file);
                StreamResult console = new StreamResult(System.out);
                if(Files.exists(Paths.get("E://test/fileXML.xml")))
                    Files.delete(Paths.get("E://test/fileXML.xml"));

                Files.copy(Paths.get(f.toURI()),Paths.get("E://test/fileXML.xml"));
                //записываем данные
                transformer.transform(source, console);
                transformer.transform(source, file);

                System.out.println("Создание XML файла закончено");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    private boolean validationXML(DOMSource docSource,String fileXMLName) throws SAXException {

        SchemaFactory factory =
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");


        File schemaLocation = new File("docXSD.xsd");
        Schema schema = factory.newSchema(schemaLocation);
        Validator validator = schema.newValidator();
        try {
            validator.validate(docSource);
            System.out.println(fileXMLName + " is valid.");

        }
        catch (SAXException ex) {
            System.out.println(fileXMLName + " is not valid because ");
            System.out.println(ex.getMessage());
            return false;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }
    private static Node getPosition(Document doc, UniversityPosition p) {
        Element pos = doc.createElement("Position");


        pos.setAttribute("id",Integer.toString( p.getId()));
        pos.appendChild(getElements(doc, pos, "position", p.getPosition()));

        return pos;
    }
    private static Node getDegree(Document doc, Degree d) {
        Element pos = doc.createElement("Degree");


        pos.setAttribute("id",Integer.toString( d.getId()));
        pos.appendChild(getElements(doc, pos, "degree", d.getDegree()));

        return pos;
    }
    private static Node getWorker(Document doc, Worker w) {
        Element worker = doc.createElement("Worker");


        worker.setAttribute("id",Integer.toString( w.getId()));
        worker.appendChild(getElements(doc, worker, "lastName", w.getLastName()));
        worker.appendChild(getElements(doc, worker, "firstName", w.getFirstName()));
        worker.appendChild(getElements(doc, worker, "middleName", w.getMiddleName()));
        worker.appendChild(getElements(doc, worker, "birthDate", w.getBirthDate()));
        worker.appendChild(getElements(doc, worker, "degreeId",Integer.toString( w.getDegreeId())));
        worker.appendChild(getElements(doc, worker, "positionId",Integer.toString( w.getPositionId())));
        worker.appendChild(getElements(doc, worker, "parentId",Integer.toString( w.getParentId())));
        return worker;
    }
    private static Node getElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    public void readXML(InputStream inputStream,String xmlName) throws SQLException, ClassNotFoundException {
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

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            DOMSource source = new DOMSource(doc);
            if(validationXML(source,xmlName)) {

                NodeList nodeList = doc.getElementsByTagName("Position");


                controllerWorkers.truncate();
                controllerPositions.truncate();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    controllerPositions.insert(getPosition(nodeList.item(i)));
                }

                nodeList = doc.getElementsByTagName("Degree");

                controllerDegrees.truncate();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    controllerDegrees.insert(getDegree(nodeList.item(i)));
                }
                nodeList = doc.getElementsByTagName("Worker");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    controllerWorkers.insert(getWorker(nodeList.item(i)));
                }

            }



        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }
    private static Worker getWorker(Node node) {
        Worker worker=null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
             worker = new Worker(getTagValue("firstName", element),getTagValue("birthDate", element),Integer.parseInt(element.getAttribute("id")),
                    getTagValue("lastName", element),getTagValue("middleName", element),Integer.parseInt(getTagValue("positionId", element)),Integer.parseInt(getTagValue("degreeId", element))
            ,Integer.parseInt(getTagValue("parentId", element)));

        }

        return worker;
    }
    private static UniversityPosition getPosition(Node node) {
        UniversityPosition position=null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            position = new UniversityPosition(Integer.parseInt(element.getAttribute("id")),
                    getTagValue("position", element));

        }

        return position;
    }
    private static Degree getDegree(Node node) {
        Degree degree=null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            degree = new Degree(Integer.parseInt(element.getAttribute("id")),
                    getTagValue("degree", element));

        }

        return degree;
    }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }



}
