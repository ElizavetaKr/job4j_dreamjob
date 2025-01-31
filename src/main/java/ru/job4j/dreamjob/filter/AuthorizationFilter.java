package ru.job4j.dreamjob.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * обеспечивает правило, что только вошедшие в систему пользователи имеют права на все операции
 */

@Component
@Order(1)
public class AuthorizationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var uri = request.getRequestURI();
        if (isAlwaysPermitted(uri)) {
            chain.doFilter(request, response);
            return;
        }
        var userLoggedIn = request.getSession().getAttribute("user") != null;
        if (!userLoggedIn) {
            var loginPageUrl = request.getContextPath() + "/users/login";
            response.sendRedirect(loginPageUrl);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * проверяем обращается ли пользователь к общедоступным адресам
     */
    private boolean isAlwaysPermitted(String uri) {
        return uri.startsWith("/users/register")
                || uri.startsWith("/users/login")
                || uri.startsWith("/js")
                || uri.startsWith("/css");
    }
}