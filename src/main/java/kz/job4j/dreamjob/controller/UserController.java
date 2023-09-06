package kz.job4j.dreamjob.controller;

import kz.job4j.dreamjob.model.User;
import kz.job4j.dreamjob.service.UserService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private static final String REDIRECT_VACANCIES = "redirect:/vacancies";
    private static final String MESSAGE_ATTRIBUTE = "message";

    private static final String ERROR_ATTRIBUTE = "error";

    private static final String INCORRECT_AUTHENTICATION_MESSAGE = "Почта или пароль введены неверно";
    private static final String NOT_FOUND_PAGE = "errors/404";
    private static final String NOT_FOUND_MESSAGE = "Пользователь с такой почтой уже существует";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional =
                userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute(ERROR_ATTRIBUTE, INCORRECT_AUTHENTICATION_MESSAGE);
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", userOptional.get());
        model.addAttribute("user", userOptional.get());
        return REDIRECT_VACANCIES;
    }

    @GetMapping("/register")
    public String getRegistationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user) {
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE);
            return NOT_FOUND_PAGE;
        }
        model.addAttribute("user", user);
        return REDIRECT_VACANCIES;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

}
