package kz.job4j.dreamjob.repository;

import kz.job4j.dreamjob.model.Candidate;
import kz.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    Collection<User> findAll();

    boolean deleteById(int id);

    Optional<User> findById(int id);

    boolean update(User user);
}