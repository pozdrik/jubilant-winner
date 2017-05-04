package socket;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.Base64;

public class FileReceiver {

    public Document receive(Document document) throws ParserConfigurationException, IOException, SAXException, XMLStreamException {

        String data = document.getDocumentElement().getFirstChild().getTextContent();
        byte[] fileData = Base64.getDecoder().decode(data);

        FileOutputStream out = new FileOutputStream("sentFile.txt");
        out.write(fileData);
        out.flush();
        out.close();

        System.out.println("Получен файл");
        return document;
    }
}