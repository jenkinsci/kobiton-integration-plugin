package io.jenkins.plugins.kobiton.shared.utils;


public final class StringUtils {
    private StringUtils() { throw new IllegalStateException("Utils class"); }

    /**
     * Check if a string is null or empty
     *
     * @param str string to check
     * @return true if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}

