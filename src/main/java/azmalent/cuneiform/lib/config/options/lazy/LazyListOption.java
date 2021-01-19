package azmalent.cuneiform.lib.config.options.lazy;

import azmalent.cuneiform.lib.config.options.AbstractConfigOption;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LazyListOption<T> extends AbstractConfigOption<List<T>, List<? extends String>> implements ILazyOption {
    protected ForgeConfigSpec.ConfigValue<List<? extends String>> stringValues;
    private final List<String> defaultValue;
    protected final Function<String, T> constructor;
    protected List<T> value;
    private boolean initialized = false;

    protected LazyListOption(List<String> defaultValue) {
        this.defaultValue = defaultValue;
        this.constructor = null;
    }

    public LazyListOption(Function<String, T> constructor) {
        this(Lists.newArrayList(), constructor);
    }

    public LazyListOption(List<String> defaultValue, Function<String, T> constructor) {
        this.defaultValue = defaultValue;
        this.constructor = constructor;
    }

    @Override
    public List<T> get() {
        if (!initialized) {
            initValue();
        }

        return value;
    }

    @Override
    public void set(List<? extends String> newValue) {
        stringValues.set(newValue);
        invalidate();
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        stringValues = addComment(builder, field, "Default: " + defaultValue)
                .defineList(getName(field), defaultValue, Objects::nonNull);
    }

    @Override
    public void invalidate() {
        value = null;
        initialized = false;
    }

    @Override
    public void initValue() {
        initialized = true;
        value = stringValues.get().stream().map(constructor).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
