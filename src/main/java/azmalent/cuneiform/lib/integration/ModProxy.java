package azmalent.cuneiform.lib.integration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ModProxy {
    String value();
}
