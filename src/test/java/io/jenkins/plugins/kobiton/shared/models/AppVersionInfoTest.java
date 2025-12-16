package io.jenkins.plugins.kobiton.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AppVersionInfoTest {

    private static final Integer APP_ID = 123;
    private static final String STATE = "OK";

    @Test
    void constructorAndGetters_ShouldReturnCorrectly() {
        AppVersionInfo appVersionInfo = new AppVersionInfo(APP_ID, STATE);

        assertEquals(APP_ID, appVersionInfo.appId());
        assertEquals(STATE, appVersionInfo.state());
    }

    @Test
    void equalsAndHashCode_ShouldReturnCorrectly() {
        AppVersionInfo appVersionInfo1 = new AppVersionInfo(APP_ID, STATE);
        AppVersionInfo appVersionInfo2 = new AppVersionInfo(APP_ID, STATE);
        AppVersionInfo appVersionInfo3 = new AppVersionInfo(456, STATE);
        AppVersionInfo appVersionInfo4 = new AppVersionInfo(APP_ID, "PARSING");
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
        AppVersionInfo appVersionInfo = new AppVersionInfo(APP_ID, STATE);

        assertEquals(expectedToString, appVersionInfo.toString());
    }
}