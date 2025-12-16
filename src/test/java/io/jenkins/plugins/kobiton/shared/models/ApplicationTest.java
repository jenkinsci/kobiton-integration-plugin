package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ApplicationTest {

    private static final Integer APP_ID = 123;
    private static final Integer VERSION_ID = 48;

    @Test
    void constructorAndGetters_ShouldReturnCorrectly() {
        Application application = new Application(APP_ID, VERSION_ID);

        assertEquals(APP_ID, application.appId());
        assertEquals(VERSION_ID, application.versionId());
    }

    @Test
    void setAppId_ShouldSetCorrectly() {
        Integer initialAppId = 123;
        Integer newAppId = 456;
        Integer versionId = 48;
        Application application = new Application(initialAppId, versionId);

        application.setAppId(newAppId);

        assertEquals(newAppId, application.appId());
    }

    @Test
    void equalsAndHashCode_ShouldReturnCorrectly() {
        Application app1 = new Application(APP_ID, VERSION_ID);
        Application app2 = new Application(APP_ID, VERSION_ID);
        Application app3 = new Application(456, VERSION_ID);
        Application app4 = new Application(APP_ID, 456);
        Application app5 = null;
        Object app6 = new Object();

        assertEquals(app1, app2);
        assertEquals(app1, app1);
        assertNotEquals(app1, app3);
        assertNotEquals(app1, app4);
        assertNotEquals(app1, app5);
        assertNotEquals(app1, app6);
        assertEquals(app1.hashCode(), app2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectly() {
        Application appWithAppId = new Application(APP_ID, VERSION_ID);
        String expected1 = "{appId='123', versionId='48'}";
        Application appWithVersionId = new Application(null, VERSION_ID);
        String expected2 = "{versionId='48'}";

        assertEquals(expected1, appWithAppId.toString());
        assertEquals(expected2, appWithVersionId.toString());
    }

    @Test
    void getAppOrVersionId_shouldReturnCorrectly() {
        String expectedAppOrVersionId = "kobiton-store:123";

        Application appWithAppId = new Application(APP_ID, VERSION_ID);
        Application appWithVersionId = new Application(null, VERSION_ID);

        assertEquals(expectedAppOrVersionId, appWithAppId.getAppOrVersionId());
        assertEquals("kobiton-store:v48", appWithVersionId.getAppOrVersionId());
    }

}