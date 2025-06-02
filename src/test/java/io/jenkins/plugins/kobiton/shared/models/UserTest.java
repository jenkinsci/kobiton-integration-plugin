package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    private static final int ID = 123;
    private static final String USERNAME = "john.doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final Boolean DISABLED = false;
    private static final Boolean IS_ACTIVATED = true;

    @Test
    void ConstructorAndGetters_ShouldReturnCorrectly() {
        User user = new User(ID, USERNAME, EMAIL, DISABLED, IS_ACTIVATED);

        assertEquals(ID, user.id());
        assertEquals(USERNAME, user.username());
        assertEquals(EMAIL, user.email());
        assertEquals(DISABLED, user.disabled());
        assertEquals(IS_ACTIVATED, user.isActivated());
    }

    @Test
    void equalsAndHashCode_ShouldReturnCorrectly() {
        User user1 = new User(ID, USERNAME, EMAIL, DISABLED, IS_ACTIVATED);
        User user2 = new User(ID, USERNAME, EMAIL, DISABLED, IS_ACTIVATED);
        User user3 = null;
        User user4 = new User(456, USERNAME, EMAIL, DISABLED, IS_ACTIVATED);
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
        User user = new User(ID, USERNAME, EMAIL, DISABLED, IS_ACTIVATED);
        String expectedToString = "{id='123', user_name='john.doe', email='john.doe@example.com'}";

        assertEquals(expectedToString, user.toString());
    }
}