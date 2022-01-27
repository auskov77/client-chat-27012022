package ru.itsjava.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.Socket;

@RequiredArgsConstructor
//@Data
public class SocketRunnable implements Runnable {
    private final Socket socket;
    private int authoStatus;
    private int st = 0;
    public ClientServiceImpl clientService;

    @SneakyThrows
    @Override
    public void run() {
        // задаем объект serverReader для идентификации, считывания сообщения с сервера
        // serverReader зависит от socket.getInputStream()
        MessageInputService serverReader = new MessageInputServiceImpl(socket.getInputStream());

        // получение сообщения от сервера
        while (true) {
            String messageFromServer = serverReader.getMessage();
            System.out.println(messageFromServer);
            if (messageFromServer.contains("Вы не авторизованы")) {
                clientService = new ClientServiceImpl();
                authoStatus = 1;
                clientService.getStatusAutho();
                System.out.println("clientService.getStatusAutho() = " + clientService.getStatusAutho());
                System.out.println("authoStatus SocketRunnable = " + authoStatus);
//                System.out.println("st SocketRunnable= " + st);
//                System.out.println("вы не в теме от сервера");
//                clientService.getStatusAutho();
//                System.out.println(clientService.getStatusAutho());
            }


        }
    }

    public int getStatus(){
//        st = status;
        return authoStatus;
    }
}
