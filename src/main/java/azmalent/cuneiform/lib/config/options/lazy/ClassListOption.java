package azmalent.cuneiform.lib.config.options.lazy;

import azmalent.cuneiform.lib.util.ReflectionUtil;
import com.google.common.collect.Lists;

import java.util.List;

public final class ClassListOption extends LazyListOption<Class> {
    public ClassListOption() {
        this(Lists.newArrayList());
    }

    public ClassListOption(List<String> defaultValue) {
        super(defaultValue, ReflectionUtil::tryGetClass);
    }
}
