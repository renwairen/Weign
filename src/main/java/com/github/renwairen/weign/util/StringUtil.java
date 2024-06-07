package com.github.renwairen.weign.util;

/**
 * @Author Zhang La @Created at 2023/5/15 10:14
 */
public abstract class StringUtil {

    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }

    public static int length(final String str) {
        return str == null ? 0 : str.length();
    }

    public static boolean startsWith(final String str, final String prefix) {
        if (str == null) {
            return prefix == null;
        }
        if (prefix == null) {
            return str == null;
        }

        final int strLen = str.length();
        final int preLen = prefix.length();
        if (preLen > strLen) {
            return false;
        }
        return str.substring(0, preLen).equals(prefix);
    }
}
