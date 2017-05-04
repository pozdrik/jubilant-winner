package socket;

/*
Предполагается, что файл хранится в той же папке, откуда запущена программа,или отправитель указал
абсолютный путь.
Получателю файл сохраняется в ту же папку, откуда запущена программа.


jar для клиента
jar для сервера

сервер-адрес порта
клиент-адрес и порт, куда подключаться
 */

/*
сервер работает как servlet
нужно перпеисать клиента
сервлет должен обрабатывать запросы
до следующего раза: сервлет, который принимает от пользователя строчку,
два запроса get и post
в ответ на get получает сообщение
в теле запроса лежит некоторое сообщение post и возвращает ему эту строку
способ отправки данных мы будем использовать форму
html показаться, форма сервлет выдает сразу собщение, есть форма куда можно вводить
 */

import org.w3c.dom.Document;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {

        User user = new User();
        Database database = new Database();

        Socket socket = new Socket("127.0.0.1", 1300);
        OutputStream out = socket.getOutputStream();
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        XmlAuthorisation authorisation = new XmlAuthorisation();
        Document document = authorisation.createMessage(keyboard);
        authorisation.send(out, document);

        ClientReadBack readBack = new ClientReadBack(socket);
        Thread t = new Thread(readBack);
        t.start();

        String checkStatus = readBack.getCheckStatus();
        if (checkStatus.equals("accepted")) {
            database.displayHistoryForClient(XmlAuthorisation.getLoginIn());
            while (true) {
                String line = keyboard.readLine();
                if (line.startsWith("/sendfile")) {
                    line = line.substring("/sendfile".length() + 1);
                    FileSender fileSender = new FileSender(line);
                    Document docFile = fileSender.create();
                    fileSender.send(out, docFile);
                } else {
                    user.sendXmlMessage(out, user.create(line));

                    database.write(XmlAuthorisation.getLoginIn(), line);
                    out.flush();

                }
            }
        }
    }
}
