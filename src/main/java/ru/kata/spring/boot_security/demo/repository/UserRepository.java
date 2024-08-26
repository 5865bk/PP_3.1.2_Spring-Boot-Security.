package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.roles where u.userName = :userName")
    Optional<User> findUserAndFetchRoles(String userName);

    @Override
    User save(User user);

    void delete(User user);
}