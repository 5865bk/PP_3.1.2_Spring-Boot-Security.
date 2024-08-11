package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean update(User user) {
        return userRepository.findById(user.getId())
                .map(entity -> {
                    entity.setUserName(user.getUserName());
                    entity.setFirstName(user.getFirstName());
                    entity.setLastName(user.getLastName());
                    entity.setBirthDate(user.getBirthDate());
                    entity.setPassword(user.getPassword());
                    entity.setRoles(user.getRoles());
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean removeById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
