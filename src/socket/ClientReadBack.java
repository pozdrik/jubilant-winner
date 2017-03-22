package socket;

import java.io.*;
import java.net.Socket;

public class ClientReadBack implements Runnable {
    private Socket socket;

    public ClientReadBack(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        User user = new User();
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            while (true) {
               user.readXmlMessage(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
