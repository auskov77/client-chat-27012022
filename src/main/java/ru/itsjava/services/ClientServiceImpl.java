package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    // создаем константы
    public final static int PORT = 8081; // порт подключения к серверу
    public final static String HOST = "localhost"; // строка подключения е серверу
    boolean isExit = false; // создание переменной isExit для проверки ввода "exit" для выхода из чата
    private String login;
    private String password;
    public MenuServiceImpl menuService;
    public Socket socket;
    private int statusAutho = 0;
    //    public SocketRunnable socketRunnable;

    @SneakyThrows
    @Override
    public void start() {
        // хотим подключиться к серверу
        // чтобы работать на стороне Клиента необходим Socket
        socket = new Socket(HOST, PORT); // в Socket необходимо передать HOST - где находится сервер и PORT - порт на котором он слушает

        // у socket'a проверить, если он подключился
        if (socket.isConnected()) {
            // как только подсоединились, создаем фоновый поток
            new Thread(new SocketRunnable(socket)).start();

            // у socket'a есть InputStream (что-то писать) и OutputStream (что-то получать)
            // в данном случае берем InputStream и отправляем что-то на сервер
            // чтобы что-то отправить нам подойдет PrintWriter оболочка над InputStream и OutputStream
            // теперь должны считать с консоли, слушать сообщения с сервера и делать это одновременно
            // реализуем с помощью потоков
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream());

            // считываем с консоли
            MessageInputService messageInputService = new MessageInputServiceImpl(System.in);

            MenuService menuService = new MenuServiceImpl(this);
            menuService.menu();


            switch (menuService.getNumMenu()) {
                case 1: {
                    authorizationUser();

//                    System.out.println("messageInputService.getMessage() = " + messageInputService.getMessage());
//                    System.out.println("socketRunnable.getStatus() = " + socketRunnable.getStatus());

//                    Thread.sleep(1000);
//                    System.out.println("statusAutho = " + statusAutho);
                    SocketRunnable socketRunnable = new SocketRunnable(socket);
                    System.out.println("socketRunnable.getStatus() = " + socketRunnable.getStatus());
//                    while (socketRunnable.getAuthoStatus() == 1){
////                    statusAutho = socketRunnable.getAuthoStatus();
//                    System.out.println("metka ot SocketRunnable");
//                    while (socketRunnable.getStatus() != 0){
//                        statusAutho = socketRunnable.getStatus();
//                        System.out.println(statusAutho);
////                        authorizationUser();
//                    }
//                    System.out.println(statusAutho);
//                    }
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

//            // из messageInputService должны получать сообщение, которое написал пользователь
//            String consoleMessage = messageInputService.getMessage();
//
//            // кто-то ввел сообщение consoleMessage, и мы должны отправить - serverWriter
//            serverWriter.println(consoleMessage);
//            serverWriter.flush(); // скинуть буферизированные данные в поток

            // считывать в цикле и отправлять сообщения
            while (!isExit) {
//                System.out.println("Введите сообщение");

                // из messageInputService должны получать сообщение, которое написал пользователь
                String consoleMessage = messageInputService.getMessage();

                isExit = consoleMessage.equals("exit");
                // проверка ввода клиентом слова "exit", чтобы покинуть чат
                if (isExit) {
                    serverWriter.println("Всем пока!");
                    serverWriter.flush(); // скинуть буферизированные данные в поток
//                    System.exit(0);
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
    public void authorizationUser() {
//        Socket socket = new Socket(HOST, PORT);
        PrintWriter serverWriter = new PrintWriter(socket.getOutputStream());
//        MessageInputService messageInputService = new MessageInputServiceImpl(System.in);
        Scanner console = new Scanner(System.in);
        System.out.println("Введите свой логин:");
//        login = messageInputService.getMessage();
        login = console.nextLine();
        System.out.println("Введите свой пароль:");
//        password = messageInputService.getMessage();
        password = console.nextLine();
        // после ввода логина и пароля - их нужно отправить на сервер
        // !autho!login:password
        // теперь конкатенируем - все собираем
        serverWriter.println("!autho!" + login + ":" + password);
        // теперь отправляем все это на сервер
        serverWriter.flush();
    }

    @SneakyThrows
    @Override
    public void registrationNewUser() {
//        Socket socket = new Socket(HOST, PORT);
        PrintWriter serverWriter = new PrintWriter(socket.getOutputStream());
//        MessageInputService messageInputService = new MessageInputServiceImpl(System.in);
        Scanner console = new Scanner(System.in);
        System.out.println("Введите свой логин:");
//        login = messageInputService.getMessage();
        login = console.nextLine();
        System.out.println("Введите свой пароль:");
//        password = messageInputService.getMessage();
        password = console.nextLine();
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
//        Socket socket = new Socket(HOST, PORT);
//        PrintWriter serverWriter = new PrintWriter(socket.getOutputStream());
//        serverWriter.println("Не зарегистрированный клиент покинул чат");
//        serverWriter.flush(); // скинуть буферизированные данные в поток
        System.exit(0);
    }

    @Override
    public int getStatusAutho() {
        return statusAutho = 1;
//        return statusAutho;
    }
}