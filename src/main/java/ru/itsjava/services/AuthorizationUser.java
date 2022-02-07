package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class AuthorizationUser implements Runnable {
    private final MessageInputService console;
    private final PrintWriter serverWriter;
    private final MessageInputService serverReader;

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            System.out.println("Введите свой логин:");
            String login = console.getMessage();
            System.out.println("Введите свой пароль:");
            String password = console.getMessage();
            serverWriter.println("!autho!" + login + ":" + password);
            serverWriter.flush();

            String messageFromSerever = serverReader.getMessage();
            System.out.println(messageFromSerever);
            if (!messageFromSerever.contains("Вы не авторизованы")){
                break;
            }
        }
    }
}
