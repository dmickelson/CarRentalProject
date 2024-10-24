package com.company.carrental.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserTest {

    @Test
    void shouldCreateValidUser() {
        User user = new User("John", "Doe", "johndoe", "password123");

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe", user.getUsername());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void shouldUpdateUserDetailsCorrectly() {
        User user = new User("John", "Doe", "johndoe", "password123");

        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setUsername("janesmith");
        user.setPassword("newpassword456");

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("janesmith", user.getUsername());
        assertEquals("newpassword456", user.getPassword());
    }

    @Test
    void shouldNotAllowNullValues() {
        User user = new User();

        assertThrows(NullPointerException.class, () -> {
            user.setFirstName(null);
            user.setLastName(null);
            user.setUsername(null);
            user.setPassword(null);
        });
    }

    @Test
    void shouldHandleUserIdCorrectly() {
        User user = new User();
        user.setUserId(1);

        assertEquals(1, user.getUserId());
    }
}
