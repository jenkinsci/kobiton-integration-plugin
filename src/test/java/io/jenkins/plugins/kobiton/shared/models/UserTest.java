package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private final int id = 123;
    private final String username = "john.doe";
    private final String email = "john.doe@example.com";
    private final Boolean disabled = false;
    private final Boolean isActivated = true;

    @Test
    void ConstructorAndGetters_ShouldReturnCorrectly() {
        User user = new User(id, username, email, disabled, isActivated);

        assertEquals(id, user.id());
        assertEquals(username, user.username());
        assertEquals(email, user.email());
        assertEquals(disabled, user.disabled());
        assertEquals(isActivated, user.isActivated());
    }

    @Test
    void equalsAndHashCode_ShouldReturnCorrectly() {
        User user1 = new User(id, username, email, disabled, isActivated);
        User user2 = new User(id, username, email, disabled, isActivated);
        User user3 = null;
        User user4 = new User(456, username, email, disabled, isActivated);
        Object user5 = new Object();

        assertEquals(user1, user2);
        assertEquals(user1, user1);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, user4);
        assertNotEquals(user1, user5);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectly() {
        User user = new User(id, username, email, disabled, isActivated);
        String expectedToString = "{id='123', user_name='john.doe', email='john.doe@example.com'}";

        assertEquals(expectedToString, user.toString());
    }
}