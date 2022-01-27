package ru.itsjava.services;

import lombok.SneakyThrows;

public interface ClientService {
    void start();
    void authorizationUser();
    void registrationNewUser();

    @SneakyThrows
    void exitChat();

    int getStatusAutho();
}
