package ru.job4j.dreamjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Данный класс - точка входа в приложение.
 * Отвечает за запуск встроенного сервера и инициализацию конфигурации приложения.*/

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}