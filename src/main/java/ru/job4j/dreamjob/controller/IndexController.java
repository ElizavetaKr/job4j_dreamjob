package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Данный класс - Контроллер для обработки запросов на главную страницу.
 * Предоставляет пользователю стартовую информацию о приложении*/

@Controller
public class IndexController {
    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }
}

