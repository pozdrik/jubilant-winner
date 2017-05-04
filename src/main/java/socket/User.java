package socket;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class User {

    public Document readAnyXml(InputStream in) throws IOException, XMLStreamException, SAXException, ParserConfigurationException {
        XmlDocumentReciever receiver = new XmlDocumentReciever(in);
        return receiver.receive();
    }

    public Document readXmlMessage(Document document) throws Exception {
        Element message = document.getDocumentElement();
        String line = message.getTextContent();

        String author = message.getAttributeNode("author").getValue();
        System.out.println(author + ":" + line);
        return document;
    }

    public void sendXmlMessage(OutputStream out, Document document) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(out));
        out.flush();
    }

    public Document create(String line) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        Document newDocument = builder.newDocument(); //создал пустой документ и вернул
        Element root = newDocument.createElement("message"); //корневой элемент
        newDocument.appendChild(root); // корневой элемент добавили в документ
        Attr author = newDocument.createAttribute("author"); //атрибут к корневому элементу

        author.setValue(XmlAuthorisation.getLoginIn());
        root.setAttributeNode(author);
        Text messageText = newDocument.createTextNode(line);
        root.appendChild(messageText); //узел к корню

        return newDocument;
    }
}