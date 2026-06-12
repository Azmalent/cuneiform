package azmalent.cuneiform.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a display name for a config option field or a config category class.
 *
 * <p>When applied to a field, it overrides the default camelCase-derived name
 * in the generated config file. When applied to an inner static class (category),
 * it sets the category name in the config file.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @Name("My Setting")
 * public static BooleanOption mySetting = BooleanOption.of(true);
 *
 * @Name("General Settings")
 * public static class General {
 *     // ...
 * }
 * }</pre>
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Name {
    String value();
}
