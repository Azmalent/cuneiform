package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

public abstract class BasicOption<T> extends AbstractConfigOption<T, T> {
    protected final T defaultValue;
    protected ForgeConfigSpec.ConfigValue<T> value;

    protected BasicOption(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T get() {
        return value.get();
    }

    public void set(T newValue) {
        value.set(newValue);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field).define(getFieldName(field), defaultValue);
    }
}
