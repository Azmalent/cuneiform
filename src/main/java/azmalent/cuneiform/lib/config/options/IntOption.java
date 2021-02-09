package azmalent.cuneiform.lib.config.options;

import azmalent.cuneiform.lib.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class IntOption extends AbstractConfigOption<Integer, Integer> {
    private ForgeConfigSpec.ConfigValue<Integer> value;
    private int defaultValue;

    private boolean rangeRestricted = false;
    private int min;
    private int max;

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


    public IntOption inRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Invalid range");
        }

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
                    "Allowed values: [" + StringUtil.joinObjects(", ", allowedValues) + "]"
            ).defineInList(getName(field), defaultValue, allowedValues);
        }
        else {
            value = builder.define(getName(field), defaultValue);
        }
    }
}
