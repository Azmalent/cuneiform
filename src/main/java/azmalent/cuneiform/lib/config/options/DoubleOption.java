package azmalent.cuneiform.lib.config.options;

import azmalent.cuneiform.lib.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class DoubleOption extends AbstractConfigOption<Double, Double> {
    protected ForgeConfigSpec.ConfigValue<Double> value;
    protected double defaultValue;

    private boolean rangeRestricted = false;
    private double min;
    private double max;

    private List<Double> allowedValues;

    public DoubleOption(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Double get() {
        return value.get();
    }

    @Override
    public void set(Double newValue) {
        value.set(newValue);
    }

    public DoubleOption inUnitRange() {
        return inRange(0, 1);
    }

    public DoubleOption inRange(double min, double max) {
        this.rangeRestricted = true;
        this.min = min;
        this.max = max;

        return this;
    }

    public DoubleOption withAllowedValues(double... allowedValues) {
        this.allowedValues = Arrays.stream(allowedValues).boxed().collect(Collectors.toList());
        return this;
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        if (rangeRestricted) {
            value = addComment(builder, field, "Default: " + defaultValue)
                    .defineInRange(getName(field), defaultValue, min, max);
        }
        else if (allowedValues != null) {
            value = addComment(builder, field,
                    "Default: " + defaultValue,
                    "Allowed values: [" + StringUtil.joinObjects(", ", allowedValues) + "]"
            ).defineInList(getName(field), defaultValue, allowedValues);
        }
        else {
            value = addComment(builder, field, "Default: " + defaultValue).define(getName(field), defaultValue);
        }
    }

    public float getAsFloat() {
        return (float) (double) get();
    }
}
