package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;

@RequiredArgsConstructor
public class SocketRunnable implements Runnable {
    public final MessageInputService serverReader;

    // Инициализация логера
    private static final Logger log = Logger.getLogger(SocketRunnable.class);

    @SneakyThrows
    @Override
    public void run() {
        // получение сообщения от сервера
        while (true) {
            String messageFromServer = serverReader.getMessage();

            // логируем инфо
            log.info("Получено сообщение от сервера: " + messageFromServer);

            System.out.println(messageFromServer);

        }
    }
}
