package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class ConfigOption<T> extends AbstractConfigOption<T, T> {
    private T defaultValue;
    private ForgeConfigSpec.ConfigValue<T> value;

    public ConfigOption(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T get() {
        return value.get();
    }

    public void set(T newValue) {
        value.set(newValue);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field)
                .define(getName(field), defaultValue);
    }
}
