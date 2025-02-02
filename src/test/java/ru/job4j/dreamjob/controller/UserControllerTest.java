package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetRegistrationPageThenPageWithUsersRegistration() {
        assertThat(userController.getRegistrationPage()).isEqualTo("users/register");
    }

    @Test
    public void whenGetLoginPageThenPageWithUsersLogin() {
        assertThat(userController.getLoginPage()).isEqualTo("users/login");
    }

    @Test
    public void whenRegisterThenPageWithVacancies() throws Exception {
        User user = new User(1, "email", "name", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        assertThat(userController.register(user, new ConcurrentModel())).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenUserExistWithExceptionThenErrorPageWithMessage() {
        Model model = new ConcurrentModel();

        assertThat(userController.register(new User(), model)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Пользователь с такой почтой уже существует");
    }
}