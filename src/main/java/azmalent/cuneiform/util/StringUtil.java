package azmalent.cuneiform.util;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {
    public static String splitCamelCase(String string) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(string);
        if (words.length > 0) {
            words[0] = StringUtils.capitalize(words[0]);
        }

        return StringUtils.join(words, ' ');
    }
}
