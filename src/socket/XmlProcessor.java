package socket;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XmlProcessor {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        Document newDocument = builder.newDocument(); //создал пустой документ и вернул
        Element root = newDocument.createElement("message"); //корневой элемент
        newDocument.appendChild(root); // корневой элемент добавили в документ
        Attr author = newDocument.createAttribute("author"); //атрибут к корневому элементу
        author.setValue("masha");
        root.setAttributeNode(author);
        Text messageText = newDocument.createTextNode("Hi!");
        root.appendChild(messageText); //узел к корню

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(newDocument), new StreamResult(new OutputStreamWriter(System.out)));



//        Document document = builder.parse(new File("firstXml.xml"));
//        Element root = document.getDocumentElement();
//        System.out.println("Root element name: " + root.getTagName());
//        NodeList children = root.getChildNodes();
//        for(int i = 0; i < children.getLength(); i++) {
//            Node node = children.item(i);
//            if(node.getNodeType() == Node.ELEMENT_NODE) {
//                Element element = (Element) node;
//                System.out.println(element.getTagName());
//            }
//        }
//    }
    }
}
