package azmalent.cuneiform.lib.config.options.lazy;

import azmalent.cuneiform.lib.util.ReflectionUtil;

@SuppressWarnings("unused")
public final class ClassOption extends LazyOption<Class<?>> {
    public ClassOption(String defaultValue) {
        super(defaultValue, ReflectionUtil::getClassOrNull);
    }

    public boolean isParent(Object object) {
        return value != null && get().isAssignableFrom(object.getClass());
    }
}
