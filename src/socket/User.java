package socket;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;

public class User {
    private String checkedLogin;

    public boolean authorizationCheck() {
        //Input by keyboard
        BufferedReader authorise = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Login: ");

        String login = null;
        try {
            login = authorise.readLine();
        } catch (IOException e) {
            System.out.println("input error");
        }

        System.out.println("Password: ");
        String password = null;
        try {
            password = authorise.readLine();
        } catch (IOException e) {
            System.out.println("input error");
        }
        // Read from login.txt

        try (FileReader readLogin = new FileReader("login.txt")) {
            BufferedReader bufferedReader = new BufferedReader(readLogin);
            String lineFromLogin;
            while ((lineFromLogin = bufferedReader.readLine()) != null) {
                String[] userData = lineFromLogin.split(" ");
                if (userData[0].equals(login) && userData[1].equals(password)) {
                    checkedLogin = login;
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read from login.txt");
            System.exit(-1);
        }
        return false;
    }

    public void displayHistory() {
        try (FileReader fileReader = new FileReader("history.txt")) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lineFromHistory;
            while ((lineFromHistory = bufferedReader.readLine()) != null) {
                System.out.println(lineFromHistory);
            }
        } catch (IOException e) {
            System.out.println("Can't read the history");
        }
    }

    public Document readXmlMessage(DataInputStream in) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(in);

        Element message = document.getDocumentElement();
        System.out.println("Root element name: " + message.getTagName());
        String line = message.getTextContent();
        System.out.println(line);
        return document;
    }

    public void create_send(String line, BufferedOutputStream out) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        Document newDocument = builder.newDocument(); //создал пустой документ и вернул
        Element root = newDocument.createElement("message"); //корневой элемент
        newDocument.appendChild(root); // корневой элемент добавили в документ
        Attr author = newDocument.createAttribute("author"); //атрибут к корневому элементу
        author.setValue(checkedLogin);
        root.setAttributeNode(author);
        Text messageText = newDocument.createTextNode(line);
        root.appendChild(messageText); //узел к корню

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(newDocument), new StreamResult(out));

        out.flush();
    }

    public void read_send(Socket client, int number) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(client.getInputStream());


        Element message = document.getDocumentElement();

        System.out.println(document.getElementsByTagName("message").item(0));


        System.out.println("Root element name: " + message.getTagName());
        String line = message.getTextContent();
        System.out.println(line);

        for (int i = 0; i < Server.getClients().size(); i++) {
            if (i != number) {
                DataOutputStream out = new DataOutputStream(Server.getClients().get(i).getOutputStream());
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.transform(new DOMSource(document), new StreamResult(out));
                out.flush();
            }
        }
    }
}
