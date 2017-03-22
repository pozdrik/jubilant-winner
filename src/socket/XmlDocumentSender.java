package socket;

import org.w3c.dom.Document;

import javax.xml.crypto.dsig.Transform;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.OutputStream;

public class XmlDocumentSender {
    private XMLStreamWriter xmlStreamWriter;
    private Transformer transformer;

    public XmlDocumentSender(OutputStream outputStream) throws Exception {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        xmlStreamWriter = factory.createXMLStreamWriter(outputStream);
        xmlStreamWriter.writeStartDocument("1.0", "UTF-8");
        xmlStreamWriter.writeStartElement("CONNECTION");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();
    }



    public void read(Document document) {

    }
}
