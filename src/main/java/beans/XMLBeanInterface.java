package beans;

import model.Worker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.ejb.Local;
import javax.xml.transform.dom.DOMSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
@Local
public interface XMLBeanInterface <T> {
    HashMap getConflictMap();
    void createXML(List<T> list) throws SQLException, ClassNotFoundException;
    Node getObject(Document doc, T w);
    Node getElements(Document doc, Element element, String name, String value);
    boolean validationXML(DOMSource docSource, String fileXMLName) throws SAXException;
    boolean contains(int id,List<T> list);
    HashMap readXML(InputStream inputStream, String xmlName);
    T getObject(Node node);
   String getTagValue(String tag, Element element);
}
