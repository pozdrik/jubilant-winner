package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<Socket> clients = new ArrayList<>();
    private static User user;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1300);
        user = new User();
        user.displayHistory();

        while (true) {
            Socket client = server.accept();
            clients.add(client);
            ClientWorker w = new ClientWorker(client, clients.size() - 1);
            Thread t = new Thread(w);
            t.start();
        }
    }

    public static ArrayList<Socket> getClients() {
        return clients;
    }

    public static User getUser() {
        return user;
    }
}
