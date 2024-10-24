package com.company.carrental.repository;

import com.company.carrental.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldFindUserByUsername() {
        // Create test user
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");
        user.setPassword("password123");

        userRepository.save(user);

        // Test finding by username
        Optional<User> foundUser = userRepository.findByUsername("johndoe");

        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
        assertEquals("Doe", foundUser.get().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenUsernameNotFound() {
        Optional<User> notFoundUser = userRepository.findByUsername("nonexistent");
        assertTrue(notFoundUser.isEmpty());
    }

    @Test
    void shouldSaveAndRetrieveUser() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setUsername("janesmith");
        user.setPassword("securepass456");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getUserId());

        Optional<User> retrievedUser = userRepository.findById(savedUser.getUserId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("Jane", retrievedUser.get().getFirstName());
        assertEquals("Smith", retrievedUser.get().getLastName());
        assertEquals("janesmith", retrievedUser.get().getUsername());
    }
}
