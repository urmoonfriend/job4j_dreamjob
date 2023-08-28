package kz.job4j.dreamjob.repository;

import kz.job4j.dreamjob.configuration.DatasourceConfiguration;
import kz.job4j.dreamjob.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(0, "name", "email@mail.ru", "password"));
        var savedUser = sql2oUserRepository.findById(user.get().getId()).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user.get());
    }

    @Test
    public void whenSaveThenAlreadyExistError() {
        var user = sql2oUserRepository.save(new User(0, "name", "email@mail.ru", "password"));
        var savedUser = sql2oUserRepository.findById(user.get().getId()).get();
        var user2 = sql2oUserRepository.save(new User(0, "name", "email@mail.ru", "password"));
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user.get());
        assertThat(user2).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(0, "name1", "email1@mail.ru", "password1"));
        var user2 = sql2oUserRepository.save(new User(0, "name2", "email2@mail.ru", "password2"));
        var user3 = sql2oUserRepository.save(new User(0, "name3", "email3@mail.ru", "password3"));
        var result = sql2oUserRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1.get(), user2.get(), user3.get()));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oUserRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var user = sql2oUserRepository.save(new User(0, "name", "email@mail.ru", "password"));
        var isDeleted = sql2oUserRepository.deleteById(user.get().getId());
        var savedUser = sql2oUserRepository.findById(user.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedUser).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oUserRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var user = sql2oUserRepository.save(new User(0, "name", "email@mail.ru", "password"));
        var updatedUser = new User(
                user.get().getId(), "new name", "new_email@mail.ru", "new_password");
        var isUpdated = sql2oUserRepository.update(updatedUser);
        var savedUser = sql2oUserRepository.findById(updatedUser.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    public void whenUpdateUnExistingUserThenGetFalse() {
        var user = new User(0, "name", "email@mail.ru", "password");
        var isUpdated = sql2oUserRepository.update(user);
        assertThat(isUpdated).isFalse();
    }

}