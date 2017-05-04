package socket;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

class XmlDocumentSender {

    XMLStreamWriter xmlStreamWriter;
    Transformer transformer;
    OutputStream outputStream;

    public XmlDocumentSender(OutputStream outputStream) throws XMLStreamException, TransformerException{
        this.outputStream = outputStream;
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();

        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream);
        xmlStreamWriter.writeStartDocument();
        xmlStreamWriter.writeStartElement("message");
    }

    public void send(Document document) throws XMLStreamException, TransformerException{
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        //xmlStreamWriter.writeStartElement("message");
        xmlStreamWriter.writeCData(stringWriter.toString());
        //xmlStreamWriter.writeEndElement();
        xmlStreamWriter.flush();
    }

    public void close() {
        try{
            xmlStreamWriter.writeEndDocument();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}