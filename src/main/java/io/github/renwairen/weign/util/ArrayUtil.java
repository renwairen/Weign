package io.github.renwairen.weign.util;

/**
 * @Author Zhang La @Created at 2023/5/15 10:05
 */
public abstract class ArrayUtil {

    public static <T> int getLength(final T[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    public static <T, V> boolean isSameLength(final T[] array1, final V[] array2) {
        return getLength(array1) == getLength(array2);
    }
}
