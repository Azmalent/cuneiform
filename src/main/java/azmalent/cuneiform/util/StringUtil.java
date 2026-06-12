package azmalent.cuneiform.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Static string utility methods.
 */
public final class StringUtil {
    /**
     * Splits a camelCase string into space-separated words with the first word capitalized.
     *
     * <p>For example, {@code "myConfigOption"} becomes {@code "My Config Option"}.</p>
     *
     * @param string the camelCase string
     * @return the split, capitalized string
     */
    public static String splitCamelCase(String string) {
        var words = StringUtils.splitByCharacterTypeCamelCase(string);
        if (words.length > 0) {
            words[0] = StringUtils.capitalize(words[0]);
        }

        return StringUtils.join(words, ' ');
    }
}
