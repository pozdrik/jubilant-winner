package socket;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;

public class ClientWorker implements Runnable {
    private Socket client;
    private int number;
    private DataInputStream in = null;

    public ClientWorker(Socket client, int number) {
        this.client = client;
        this.number = number;
    }

    public void run() {
        try {
            in = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("in failed");
        }

        try {
            XmlAuthorisation authorisation = new XmlAuthorisation();
            Document docFirst = authorisation.receive(in);

            authorisation.check(docFirst);
            Document replyDocument = authorisation.statusMessage();
            DataOutputStream out = new DataOutputStream(Server.getClients().get(number).getOutputStream());
            try {
                authorisation.send(out, replyDocument);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

            if (XmlAuthorisation.isAccepted()) {
                try {
                    while (true) {
                        User user = new User();
                        Document doc = user.readAnyXml(in);
                        if(doc.getDocumentElement().getLastChild().getNodeName().equals("file")){
                            FileReceiver fileReceiver = new FileReceiver();
                            fileReceiver.receive(doc);
                        } else user.readXmlMessage(doc);

                        for (int i = 0; i < Server.getClients().size(); i++) {
                            if (i != number) {
                                DataOutputStream outt = new DataOutputStream(Server.getClients().get(i).getOutputStream());
                                if (doc.getDocumentElement().getLastChild().getNodeName().equals("file")) {
                                    FileSender fileSender = new FileSender();
                                    fileSender.send(outt, doc);
                                } else {
                                    user.sendXmlMessage(outt, doc);
                                }
                                outt.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ParserConfigurationException | XMLStreamException | SAXException | IOException |
                TransformerException e)

        {
            e.printStackTrace();
        }
    }
}
