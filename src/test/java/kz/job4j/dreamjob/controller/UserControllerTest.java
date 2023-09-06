package kz.job4j.dreamjob.controller;

import kz.job4j.dreamjob.model.User;
import kz.job4j.dreamjob.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestGetLoginPageThenGetLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenPostLoginThenRedirectToVacanciesPage() {
        var user = new User(1, "email", "name", "password");
        when(userService.findByEmailAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(user));
        MockHttpServletRequest request = new MockHttpServletRequest();
        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);
        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenRequestGetRegisterPageThenGetRegisterPage() {
        var view = userController.getRegistationPage();
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenPostRegisterThenRedirestToVacanciesPage() {
        var user = new User(1, "email", "name", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));
        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenRequestLogoutThenRedirestToUsersLoginPage() {
        MockHttpSession session = new MockHttpSession();
        var view = userController.logout(session);
        assertThat(view).isEqualTo("redirect:/users/login");
    }

}