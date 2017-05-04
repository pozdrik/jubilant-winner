package socket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.Socket;

public class ClientReadBack implements Runnable {
    private Socket socket;
    private String checkStatus;
    private DataInputStream in;

    public ClientReadBack(Socket socket) throws IOException, XMLStreamException, ParserConfigurationException, SAXException {
        this.socket = socket;

        XmlAuthorisation authorisation = new XmlAuthorisation();

        in = new DataInputStream(socket.getInputStream());
        Document replyFromServer = authorisation.receive(in);

        Element root = replyFromServer.getDocumentElement();
        Node nstatus = root.getAttributeNode("status");
        checkStatus = nstatus.getNodeValue();
    }

    @Override
    public void run() {
        User user = new User();
        try {
            if (checkStatus.equals("accepted"))
                while (true) {
                    Document document = user.readAnyXml(in);
                    if(document.getDocumentElement().getLastChild().getNodeName().equals("file")){
                        FileReceiver fileReceiver = new FileReceiver();
                        fileReceiver.receive(document);
                    } else user.readXmlMessage(document);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCheckStatus() {
        return checkStatus;
    }
}

