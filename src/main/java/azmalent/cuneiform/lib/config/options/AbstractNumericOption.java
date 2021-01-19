package azmalent.cuneiform.lib.config.options;

import azmalent.cuneiform.lib.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.Collection;

public abstract class AbstractNumericOption<T extends Number> extends AbstractConfigOption<T, T> {
    protected ForgeConfigSpec.ConfigValue<T> value;

    protected T defaultValue;

    protected boolean rangeRestricted = false;
    protected T min;
    protected T max;

    protected Collection<T> allowedValues;

    public AbstractNumericOption(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public AbstractNumericOption(T defaultValue, T min, T max) {
        this.defaultValue = defaultValue;

        this.rangeRestricted = true;
        this.min = min;
        this.max = max;
    }

    public AbstractNumericOption(T defaultValue, Collection<T> allowedValues) {
        this.defaultValue = defaultValue;
        this.allowedValues = allowedValues;
    }

    @Override
    public T get() {
        return value.get();
    }

    @Override
    public void set(T newValue) {
        value.set(newValue);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        if (allowedValues != null) {
            value = addComment(builder, field,
                "Default: " + defaultValue,
                "Allowed values: [" + StringUtil.joinObjects(", ", allowedValues) + "]"
            ).defineInList(getName(field), defaultValue, allowedValues);
        }
        else {
            value = builder.define(getName(field), defaultValue);
        }
    }
}
