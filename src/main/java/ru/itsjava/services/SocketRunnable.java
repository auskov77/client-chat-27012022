package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class SocketRunnable implements Runnable {
    public final MessageInputService serverReader;

    @SneakyThrows
    @Override
    public void run() {
        // получение сообщения от сервера
        while (true) {
            String messageFromServer = serverReader.getMessage();
            System.out.println(messageFromServer);
        }
    }
}
