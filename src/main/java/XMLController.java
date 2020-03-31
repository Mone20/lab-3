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
import java.sql.ResultSet;
import java.sql.SQLException;

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
            controllerDegrees = new Controller<Degree>(new DegreeTable(Database.connection));
        }


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();


            Document doc = builder.newDocument();
            ResultSet resultWorkers=controllerWorkers.selectAll();
            ResultSet resultPositions=controllerPositions.selectAll();
            ResultSet resultDegrees=controllerDegrees.selectAll();
            Element rootElement =
                    doc.createElement( "Tables");
            // добавляем корневой элемент в объект Document
            doc.appendChild(rootElement);
            Element rootElementWorkers=doc.createElement("Workers");
            rootElement.appendChild(rootElementWorkers);
            while(resultWorkers.next())
            {
                rootElementWorkers.appendChild(getWorker(doc,(Worker) controllerWorkers.select(resultWorkers.getInt("id"))));

            }


            Element rootElementPos=doc.createElement("Positions");
            rootElement.appendChild(rootElementPos);
            while(resultPositions.next())
            {
                rootElementPos.appendChild(getPosition(doc,(UniversityPosition) controllerPositions.select(resultPositions.getInt("id"))));

            }

            Element rootElementDeg=doc.createElement("Degrees");
            rootElement.appendChild(rootElementDeg);
            while(resultDegrees.next())
            {
                rootElementDeg.appendChild(getDegree(doc,(Degree) controllerDegrees.select(resultDegrees.getInt("id"))));

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
        } catch (IOException e) {
            e.printStackTrace();
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
    public void readXML() throws SQLException, ClassNotFoundException {
        if(Database.connection==null)
            Database.connectDB();
        if(controllerWorkers==null) {
            controllerWorkers = new Controller<Worker>(new WorkersTable(Database.connection));
        }
        if(controllerPositions==null) {
            controllerPositions = new Controller<UniversityPosition>(new PositionTable());
        }
        if(controllerDegrees==null) {
            controllerDegrees = new Controller<Degree>(new DegreeTable(Database.connection));
        }
        String filepath = "fileXML.xml";
        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            DOMSource source = new DOMSource(doc);
            if(validationXML(source,xmlFile.getName())) {

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
