package azmalent.cuneiform.lib.config.options.lazy;

import azmalent.cuneiform.lib.util.ReflectionUtil;

public final class ClassOption extends LazyOption<Class> {
    public ClassOption(String defaultValue) {
        super(defaultValue, ReflectionUtil::tryGetClass);
    }
}
