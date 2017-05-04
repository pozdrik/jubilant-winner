package socket;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class XmlAuthorisation {
    private Document newDocument;
    private static String userName;
    private static boolean accepted;
    private static String loginIn;

    public Document createMessage(BufferedReader in) throws ParserConfigurationException, IOException, TransformerException {
        System.out.println("Login: ");
        loginIn = in.readLine();
        System.out.println("Password: ");
        String passwordIn = in.readLine();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        newDocument = builder.newDocument(); //создал пустой документ и вернул
        Element root = newDocument.createElement("authorisation"); //корневой элемент
        newDocument.appendChild(root); // корневой элемент добавили в документ
        Attr login = newDocument.createAttribute("login"); //атрибут к корневому элементу
        Attr password = newDocument.createAttribute("password");

        login.setValue(loginIn);
        password.setValue(passwordIn);

        root.setAttributeNode(login);
        root.setAttributeNode(password);
        return newDocument;
    }

    public void send(OutputStream out, Document doc) throws TransformerException, IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(out);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        //transformer.transform(new DOMSource(doc), new StreamResult(System.out));
        outputStream.flush();
    }

    public Document receive(InputStream in) throws ParserConfigurationException, XMLStreamException, IOException, SAXException {
        XmlDocumentReciever receiver = new XmlDocumentReciever(in);
        return receiver.receive();
    }

    public Document statusMessage() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        newDocument = builder.newDocument(); //создал пустой документ и вернул
        Element root = newDocument.createElement("authorisation"); //корневой элемент
        newDocument.appendChild(root); // корневой элемент добавили в документ
        Attr status = newDocument.createAttribute("status"); //атрибут к корневому элементу

        root.setAttributeNode(status);

        if (isAccepted())
            status.setValue("accepted");
        else {
            status.setValue("rejected");
        }
        return newDocument;

    }

    public void check(Document document) throws TransformerException {
        accepted = false;
        Element root = document.getDocumentElement();
        Node nlogin = root.getAttributeNode("login");
        String checkLogin = nlogin.getNodeValue();

        Node npassword = root.getAttributeNode("password");
        String checkPassword = npassword.getNodeValue();

        try (FileReader readLogin = new FileReader("login.txt")) {
            BufferedReader bufferedReader = new BufferedReader(readLogin);
            String lineFromLogin;
            while ((lineFromLogin = bufferedReader.readLine()) != null) {
                String[] userData = lineFromLogin.split(" ");
                if (userData[0].equals(checkLogin) && userData[1].equals(checkPassword)) {
                    userName = checkLogin;
                    accepted = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read from login.txt");
            System.exit(-1);
        }
    }

    public static boolean isAccepted() {
        return accepted;
    }

    public static String getLoginIn() {
        return loginIn;
    }
}
