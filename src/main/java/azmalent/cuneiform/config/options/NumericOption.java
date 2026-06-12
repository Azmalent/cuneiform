package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

/**
 * Abstract base class for numeric config options ({@link IntOption}, {@link DoubleOption}).
 *
 * <p>Supports optional min/max range constraints. When both {@link #min} and
 * {@link #max} are set, the option is defined with a range constraint in the
 * config spec.</p>
 *
 * @param <T> the numeric type (must extend {@link Number})
 * @see IntOption
 * @see DoubleOption
 */
public abstract class NumericOption<T extends Number> extends BasicOption<T> {
    protected T min;
    protected T max;

    /**
     * Creates a numeric option with no range constraint.
     */
    protected NumericOption(T defaultValue) {
        super(defaultValue);
    }

    protected NumericOption(T defaultValue, T min, T max) {
        super(defaultValue);
        this.min = min;
        this.max = max;
    }

    /**
     * Defines the config value with a range constraint in the builder.
     * Called by {@link #init} when both min and max are set.
     */
    abstract protected ForgeConfigSpec.ConfigValue<T> defineRange(ForgeConfigSpec.Builder builder, String name);

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        String name = getFieldName(field);

        builder = addComment(builder, field, "Default: " + defaultValue);
        if (min != null && max != null) {
            value = defineRange(builder, name);
        } else {
            value = builder.define(name, defaultValue);
        }
    }
}
