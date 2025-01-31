package ru.job4j.dreamjob.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dreamjob.model.User;

/**
 * Данный класс - Контроллер для обработки запросов на главную страницу.
 * Предоставляет пользователю стартовую информацию о приложении
 */

@Controller
public class IndexController {
    @GetMapping({"/", "/index"})
    public String getIndex() {
        return "index";
    }
}