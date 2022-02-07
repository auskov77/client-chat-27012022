package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    // создаем константы
    public final static int PORT = 8081; // порт подключения к серверу
    public final static String HOST = "localhost"; // строка подключения е серверу
    private PrintWriter serverWriter;
    private MessageInputService messageInputService;
    public Socket socket;

    // Инициализация логера
    private static final Logger log = Logger.getLogger(ClientServiceImpl.class);

    @SneakyThrows
    @Override
    public void start() {
        socket = new Socket(HOST, PORT); // в Socket необходимо передать HOST - где находится сервер и PORT - порт на котором он слушает

        MessageInputService serverReader = new MessageInputServiceImpl(socket.getInputStream());
        messageInputService = new MessageInputServiceImpl(System.in);
        serverWriter = new PrintWriter(socket.getOutputStream());

        if (socket.isConnected()) {

            MenuService menuService = new MenuServiceImpl(this);
            menuService.menu();

            switch (menuService.getNumMenu()) {
                case 1: {
                    Thread thread = new Thread(
                            new AuthorizationUser(messageInputService, serverWriter, serverReader)
                    );
                    thread.start();
                    thread.join();
                    break;
                }
                case 2: {
                    registrationNewUser();
                    break;
                }
                case 3: {
                    exitChat();
                    break;
                }
            }
            SocketRunnable socketRunnable = new SocketRunnable(serverReader);
            new Thread(socketRunnable).start();

            boolean isExit = false;// isExit - переменная для проверки ввода "exit"

            // считывать в цикле и отправлять сообщения
            while (!isExit) {

                // из messageInputService должны получать сообщение, которое написал пользователь
                String consoleMessage = messageInputService.getMessage();

                // логируем инфо
                log.info("Сообщение отправлено на сервер: " + consoleMessage);

                isExit = consoleMessage.equals("exit");
                // проверка ввода клиентом слова "exit", чтобы покинуть чат
                if (isExit) {
                    serverWriter.println("Всем пока!");
                    serverWriter.flush(); // скинуть буферизированные данные в поток
                    exitChat();
                }

                // кто-то ввел сообщение consoleMessage, и мы должны отправить - serverWriter
                serverWriter.println(consoleMessage);
                serverWriter.flush(); // скинуть буферизированные данные в поток

           }
        }
    }

    @SneakyThrows
    @Override
    public void registrationNewUser() {
        System.out.println("Введите свой логин:");
        String login = messageInputService.getMessage();
        System.out.println("Введите свой пароль:");
        String password = messageInputService.getMessage();
        // после ввода логина и пароля - их нужно отправить на сервер
        // !reg!login:password
        // теперь конкатенируем - все собираем
        // теперь отправляем все это на сервер
        serverWriter.println("!reg!" + login + ":" + password);
        serverWriter.flush();
    }

    @SneakyThrows
    @Override
    public void exitChat() {
        System.exit(0);

        // логируем инфо
        log.info("Клиент покинул чат");
    }
}