package ru.itsjava.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
@Getter
public class MenuServiceImpl implements MenuService {
    private final ClientService clientService;
    private int numMenu = 0;

    @Override
    public void menu() {
        printMenu();
        Scanner console = new Scanner(System.in);

        System.out.println("Выберите пункт из меню");

        switch (console.nextInt()) {
            case 1: {
                System.out.println("Вы выбрали авторизацию");
                numMenu = 1;
                break;
            }
            case 2: {
                System.out.println("Вы выбрали регистрацию");
                numMenu = 2;
                break;
            }
            case 3: {
                System.out.println("Вы покидаете чат");
                numMenu = 3;
                break;
            }
        }
    }

    @Override
    public void printMenu() {
        System.out.println("1 - авторизация; 2 - регистрация; 3 - выход");
    }

    @Override
    public int getNumMenu() {
        return numMenu;
    }
}