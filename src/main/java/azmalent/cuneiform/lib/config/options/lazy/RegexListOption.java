package azmalent.cuneiform.lib.config.options.lazy;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Pattern;

public final class RegexListOption extends LazyListOption<Pattern> {
    public RegexListOption() {
        this(Lists.newArrayList());
    }

    public RegexListOption(List<String> defaultValue) {
        super(defaultValue, Pattern::compile);
    }
}
