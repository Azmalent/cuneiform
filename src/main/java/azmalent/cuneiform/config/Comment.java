package azmalent.cuneiform.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attaches a comment to a config option field or a config category class.
 *
 * <p>The comment appears above the option or category in the generated TOML
 * config file. Multiple strings produce multiple comment lines.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @Comment({"This is a useful setting.", "Be careful when changing it!"})
 * public static BooleanOption usefulSetting = BooleanOption.of(true);
 * }</pre>
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Comment {
    String[] value();
}