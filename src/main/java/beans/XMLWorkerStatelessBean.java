package beans;

import com.db.Controller;
import com.db.Database;
import com.db.tables.WorkersTable;
import model.Worker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
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
import java.util.HashMap;
import java.util.List;

@Stateless
@LocalBean
public class XMLWorkerStatelessBean implements XMLBeanInterface<Worker> {
private HashMap<Worker,Worker> confMap;
public HashMap<Worker,Worker> getConflictMap()
{
    return confMap;
}

    @Override
    public void createXML(List<Worker> list)  {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement =
                    doc.createElement( "Tables");
            doc.appendChild(rootElement);
            Element rootElementWorkers=doc.createElement("Workers");
            rootElement.appendChild(rootElementWorkers);
            for(int i=0;i<list.size();i++)
            {
                rootElementWorkers.appendChild(getObject(doc,list.get(i)));

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
               /* if(Files.exists(Paths.get("E://test/fileXML.xml")))
                    Files.delete(Paths.get("E://test/fileXML.xml"));
                Files.copy(Paths.get(f.toURI()),Paths.get("E://test/fileXML.xml"));*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Node getObject(Document doc, Worker w) {
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
    public Node getElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    public boolean validationXML(DOMSource docSource, String fileXMLName) throws SAXException {

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

    @Override
    public boolean contains(int id,List<Worker> list) {
        for(Worker w:list)
        {
            if(w.getId()==id)
                return true;
        }
        return false;
    }
    public HashMap readXML(InputStream inputStream, String xmlName)  {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        HashMap<Worker,Worker> conflictMap=new HashMap<>();
        Controller<Worker> controllerWorkers=null;
        try {
            controllerWorkers=new Controller<Worker>(new WorkersTable(Database.connection));
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            DOMSource source = new DOMSource(doc);

            if(validationXML(source,xmlName)) {
                NodeList nodeList = doc.getElementsByTagName("Worker");
                List<Worker> listWorkers=controllerWorkers.selectAll();
                int nodeId=0;

                for (int i = 0; i < nodeList.getLength(); i++) {
                    nodeId= getObject(nodeList.item(i)).getId();
                    if(contains(nodeId,listWorkers))
                    {
                        if(!controllerWorkers.select(nodeId).equals(getObject(nodeList.item(i)))) {
                            conflictMap.put(controllerWorkers.select(nodeId),getObject(nodeList.item(i)));

                        }
                    }
                    else
                    controllerWorkers.insert(getObject(nodeList.item(i)));
                }

            }




        } catch (Exception exc) {
            exc.printStackTrace();
        }
        confMap=conflictMap;
        return conflictMap;
    }

    public Worker getObject(Node node) {
        Worker worker=null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            worker = new Worker(getTagValue("firstName", element),getTagValue("birthDate", element),Integer.parseInt(element.getAttribute("id")),
                    getTagValue("lastName", element),getTagValue("middleName", element),Integer.parseInt(getTagValue("positionId", element)),Integer.parseInt(getTagValue("degreeId", element))
                    ,Integer.parseInt(getTagValue("parentId", element)));

        }

        return worker;
    }
    public String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
