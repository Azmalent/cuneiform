package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

public abstract class NumericOption<T extends Number> extends BasicConfigOption<T> {
    protected T min;
    protected T max;

    protected NumericOption(T defaultValue) {
        super(defaultValue);
    }

    protected NumericOption(T defaultValue, T min, T max) {
        super(defaultValue);
        this.min = min;
        this.max = max;
    }

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
