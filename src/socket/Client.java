package socket;
/*
для обмена сообщениями между пользователями использовать XML-форматированные сообщения
<message author="masha">
Hello!
</message>

Для авторизации использовать XML-форматированные сообщения
<authorisation login="LOGIN" password="PASSWORD" />

Форма ответа:
<authorisation status="accepted" />
<authorisation status="rejected" />
 */

/*
Добавить к программе возможность отправки файлов между собеседниками
Для инициаизации отправки пользователь вводит команду и имя файла:
/sendfile <имя_файла>
Предполагается, что файл хранится в той же папке, откуда запущена программа,или отправитель указал
абсолютный путь.
Получателю файл сохраняется в ту же папку, откуда запущена программа.
В истории сообщения не высвечивается.
Формат передачи файла:
<message author="ИМЯ">
<file name="ИМЯ_ФАЙЛА">
<![CDATA[СОДЕРЖИМОЕ_ФАЙЛА]]> CDATA предназначен для передачи данных, которые не являются частью xml, xml парсер проигнорирует кусок
из CDATA вынуть содержимоеи положить в файл
</file>
</message>
 */
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {

        User user = new User();
        if (user.authorizationCheck()) {
            user.displayHistory();

            Socket socket = new Socket("127.0.0.1", 1300);
            ClientReadBack readBack = new ClientReadBack(socket);
            Thread t = new Thread(readBack);
            t.start();

            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            try (FileWriter fileWriter = new FileWriter("history.txt", true)) {
                while (true) {
                    String line = keyboard.readLine();
                    //Document document = user.createXmlMessage(line);
                    //user.sendXmlMessage(out, document);
                    user.create_send(line, out);

                    fileWriter.write(line);
                    fileWriter.append('\n');
                    fileWriter.flush();

                    out.flush();
                }
            }
        }
    }
}
