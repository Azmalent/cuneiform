package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class ListOption<T> extends AbstractConfigOption<List<? extends T>, List<? extends  T>> {
    private ForgeConfigSpec.ConfigValue<List<? extends T>> value;
    private List<? extends T> defaultValue;
    private Predicate<Object> validator;

    public ListOption() {
        this(new ArrayList<T>(), Objects::nonNull);
    }

    public ListOption(List<T> defaultValue) {
        this(defaultValue, Objects::nonNull);
    }

    public ListOption(Predicate<T> validator) {
        this(new ArrayList<T>(), validator);
    }

    public ListOption(List<T> defaultValue, Predicate<T> validator) {
        this.defaultValue = defaultValue;
        this.validator = (Predicate<Object>) validator;
    }

    public List<? extends T> get() {
        return value.get();
    }

    public void set(List<? extends T> newValue) {
        value.set(newValue);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field)
                .defineList(getName(field), defaultValue, Objects::nonNull);
    }
}
