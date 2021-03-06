package azmalent.cuneiform.lib.config.options.lazy;

import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class RegexOption extends LazyOption<Pattern> {
    public RegexOption(String defaultValue) {
        super(defaultValue, Pattern::compile);
    }
}
