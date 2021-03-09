package azmalent.cuneiform.lib.config.options.lazy;

import azmalent.cuneiform.lib.config.options.AbstractConfigOption;
import azmalent.cuneiform.lib.config.options.lazy.ILazyOption;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.function.Function;

@SuppressWarnings("unused")
public class LazyOption<T> extends AbstractConfigOption<T, String> implements ILazyOption {
    protected ForgeConfigSpec.ConfigValue<String> stringValue;
    protected final String defaultValue;
    protected final Function<String, T> constructor;
    protected T value;
    protected boolean initialized = false;

    protected LazyOption(String defaultValue) {
        this.defaultValue = defaultValue;
        this.constructor = null;
    }

    public LazyOption(String defaultValue, Function<String, T> constructor) {
        this.defaultValue = defaultValue;
        this.constructor = constructor;
    }

    @Override
    public T get() {
        if (!initialized) {
            initValue();
        }

        return value;
    }

    protected T getDefault() {
        String defaultString = stringValue.get();
        return constructor.apply(defaultString);
    }

    @Override
    public void set(String newValue) {
        stringValue.set(newValue);
        invalidate();
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        stringValue = addComment(builder, field, "Default: " + defaultValue)
                .define(getName(field), defaultValue);
    }

    @Override
    public void invalidate() {
        value = null;
        initialized = false;
    }

    @Override
    public void initValue() {
        initialized = true;

        String stringValue = this.stringValue.get();
        value = constructor.apply(stringValue);
        if (value == null) value = getDefault();
    }
}
