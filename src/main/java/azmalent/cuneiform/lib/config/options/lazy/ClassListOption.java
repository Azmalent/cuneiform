package azmalent.cuneiform.lib.config.options.lazy;

import azmalent.cuneiform.lib.util.ReflectionUtil;
import com.google.common.collect.Lists;

import java.util.List;

@SuppressWarnings("unused")
public final class ClassListOption extends LazyListOption<Class> {
    public ClassListOption() {
        this(Lists.newArrayList());
    }

    public ClassListOption(List<String> defaultValue) {
        super(defaultValue, ReflectionUtil::getClassOrNull);
    }

    public boolean containsParent(Object object) {
        Class<?> child = object.getClass();

        for (Class<?> clazz : get()) {
            if (clazz.isAssignableFrom(child)) return true;
        }

        return false;
    }
}
