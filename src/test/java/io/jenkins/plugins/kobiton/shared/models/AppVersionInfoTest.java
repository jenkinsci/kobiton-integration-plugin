package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppVersionInfoTest {
    private final Integer appId = 123;
    private final String state = "OK";

    @Test
    void ConstructorAndGetters_ShouldReturnCorrectly() {
        AppVersionInfo appVersionInfo = new AppVersionInfo(appId, state);

        assertEquals(appId, appVersionInfo.appId());
        assertEquals(state, appVersionInfo.state());
    }

    @Test
    void equalsAndHashCode_ShouldReturnCorrectly() {
        AppVersionInfo appVersionInfo1 = new AppVersionInfo(appId, state);
        AppVersionInfo appVersionInfo2 = new AppVersionInfo(appId, state);
        AppVersionInfo appVersionInfo3 = new AppVersionInfo(456, state);
        AppVersionInfo appVersionInfo4 = new AppVersionInfo(appId, "PARSING");
        AppVersionInfo appVersionInfo5 = null;
        Object appVersionInfo6 = new Object();

        assertEquals(appVersionInfo1, appVersionInfo1);
        assertEquals(appVersionInfo1, appVersionInfo2);
        assertNotEquals(appVersionInfo1, appVersionInfo3);
        assertNotEquals(appVersionInfo1, appVersionInfo4);
        assertNotEquals(appVersionInfo1, appVersionInfo5);
        assertNotEquals(appVersionInfo1, appVersionInfo6);
        assertEquals(appVersionInfo1.hashCode(), appVersionInfo2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectly() {
        String expectedToString = "{appId='123', state='OK'}";
        AppVersionInfo appVersionInfo = new AppVersionInfo(appId, state);

        assertEquals(expectedToString, appVersionInfo.toString());
    }
}