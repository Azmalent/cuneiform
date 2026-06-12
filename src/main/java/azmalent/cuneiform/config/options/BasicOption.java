package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

/**
 * Base class for simple config options that hold a single typed value.
 *
 * <p>Wraps a {@link ForgeConfigSpec.ConfigValue} and provides {@link #get()}
 * and {@link #set(Object)} accessors.</p>
 *
 * @param <T> the type of the config value
 * @see BooleanOption
 * @see StringOption
 * @see NumericOption
 */
public abstract class BasicOption<T> extends AbstractConfigOption<T, T> {
    protected final T defaultValue;
    protected ForgeConfigSpec.ConfigValue<T> value;

    /**
     * Creates a basic option with the given default value.
     */
    protected BasicOption(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the current value of this config option.
     */
    public T get() {
        return value.get();
    }

    /**
     * Sets this config option to a new value.
     */
    public void set(T newValue) {
        value.set(newValue);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field).define(getFieldName(field), defaultValue);
    }
}
