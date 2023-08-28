package kz.job4j.dreamjob.service;

import kz.job4j.dreamjob.model.User;
import kz.job4j.dreamjob.repository.Sql2oUserRepository;
import kz.job4j.dreamjob.repository.UserRepository;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ThreadSafe
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    public SimpleUserService(Sql2oUserRepository sql2oUserRepository) {
        this.userRepository = sql2oUserRepository;
    }

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
