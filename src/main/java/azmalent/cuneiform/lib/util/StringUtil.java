package azmalent.cuneiform.lib.util;

import java.util.StringJoiner;

@SuppressWarnings("unused")
public final class StringUtil {
    public static String splitCamelCase(String string) {
        String result = string.trim().replaceAll(String.format("%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
        ), " ");

        return capitalize(result);
    }

    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        if (string.length() == 1) {
            return string.toUpperCase();
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static <T> String joinObjects(String separator, T[] items) {
        StringJoiner joiner = new StringJoiner(separator);
        for (int i = 0; i < items.length; i++) {
            T item = items[i];
            joiner.add(item.toString());
        }

        return joiner.toString();
    }

    public static <T> String joinObjects(String separator, Iterable<T> items) {
        StringJoiner joiner = new StringJoiner(separator);
        for (T item : items) {
            joiner.add(item.toString());
        }

        return joiner.toString();
    }
}
