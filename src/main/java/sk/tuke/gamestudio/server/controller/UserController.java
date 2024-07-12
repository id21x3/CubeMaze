package sk.tuke.gamestudio.server.controller;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;

@Getter
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private User loggedUser;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(String login, String password) {
        if ("password".equals(password)) {
            loggedUser = new User(login);
            return "redirect:/cubemaze";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Название HTML-шаблона для страницы входа
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

}
