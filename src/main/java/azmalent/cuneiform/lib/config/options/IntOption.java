package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class IntOption extends AbstractConfigOption<Integer, Integer> {
    private ForgeConfigSpec.ConfigValue<Integer> value;
    private int defaultValue;

    private boolean rangeRestricted = false;
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    private List<Integer> allowedValues;

    public IntOption(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Integer get() {
        return value.get();
    }

    @Override
    public void set(Integer newValue) {
        value.set(newValue);
    }

    public IntOption nonNegative() {
        return inRange(0, Integer.MAX_VALUE);
    }

    public IntOption positive() {
        return inRange(1, Integer.MAX_VALUE);
    }

    public IntOption inRange(int min, int max) {
        this.rangeRestricted = true;
        this.min = min;
        this.max = max;

        return this;
    }

    public IntOption allowedValues(int... allowedValues) {
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
                    "Allowed values: [" + StringUtils.join(allowedValues, ", ") + "]"
            ).defineInList(getName(field), defaultValue, allowedValues);
        }
        else {
            value = addComment(builder, field, "Default: " + defaultValue).define(getName(field), defaultValue);
        }
    }
}
