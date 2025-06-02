package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    private final Integer appId = 123;
    private final Integer versionId = 48;
    @Test
    void ConstructorAndGetters_ShouldReturnCorrectly() {
        Application application = new Application(appId, versionId);

        assertEquals(appId, application.appId());
        assertEquals(versionId, application.versionId());
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
        Application app1 = new Application(appId, versionId);
        Application app2 = new Application(appId, versionId);
        Application app3 = new Application(456, versionId);
        Application app4 = new Application(appId, 456);
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
        Application appWithAppId = new Application(appId, versionId);
        String expected1 = "{appId='123', versionId='48'}";
        Application appWithVersionId = new Application(null, versionId);
        String expected2 = "{versionId='48'}";

        assertEquals(expected1, appWithAppId.toString());
        assertEquals(expected2, appWithVersionId.toString());
    }

    @Test
    void getAppOrVersionId_shouldReturnCorrectly() {
        String expectedAppOrVersionId = "kobiton-store:123";

        Application appWithAppId = new Application(appId, versionId);
        Application appWithVersionId = new Application(null, versionId);

        assertEquals(expectedAppOrVersionId, appWithAppId.getAppOrVersionId());
        assertEquals("kobiton-store:v48", appWithVersionId.getAppOrVersionId());
    }

}