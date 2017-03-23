package socket;

import java.io.*;
import java.net.Socket;

public class ClientWorker implements Runnable {
    private Socket client;
    private int number;

    public ClientWorker(Socket client, int number) {
        this.client = client;
        this.number = number;
    }

    public void run() {
        DataInputStream in = null;

        try {
            in = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("in failed");
        }

        try {
            while (true) {
                //User user = new User();
                String line = in.readUTF();
                System.out.println(line);

                for (int i = 0; i < Server.getClients().size(); i++) {
                    if (i != number) {
                        DataOutputStream out = new DataOutputStream(Server.getClients().get(i).getOutputStream());
                        out.writeUTF(line);
                        out.flush();
                    }
                }
                //user.read_send(in, number);

//                Document document = user.readXmlMessage(in);
//
//                for (int i = 0; i < Server.getClients().size(); i++) {
//                    if (i != number) {
//                        DataOutputStream out = new DataOutputStream(Server.getClients().get(i).getOutputStream());
//                        user.sendXmlMessage(out, document);
//                        out.flush();
//                    }
//                }
            }
        } catch (Exception e) {
            System.out.println("Read/write failed");
        }
    }

}
