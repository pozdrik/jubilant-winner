package socket;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Base64;

public class FileSender {
    private String line;

    public FileSender(String line) {
        this.line = line;
    }

    public FileSender() {}

    public Document create() throws TransformerException, IOException, ParserConfigurationException {
        File file = new File(line);
        byte[] fileData = new byte[(int)file.length()];

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("message");
        CDATASection cdata = document.createCDATASection(Base64.getEncoder().encodeToString(fileData));
        document.appendChild(root);
        Attr author = document.createAttribute("author"); //атрибут к корневому элементу
        author.setValue(XmlAuthorisation.getLoginIn());
        root.setAttributeNode(author);

        Element elementFile = document.createElement("file");
        Attr name = document.createAttribute("name");
        name.setValue(line);
        elementFile.setAttributeNode(name);
        root.appendChild(elementFile);
        elementFile.appendChild(cdata);
        return document;
    }

    public void send(OutputStream out, Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(out));
    }
}
