package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.Collection;

public final class DoubleOption extends AbstractNumericOption<Double> {
    public DoubleOption(double defaultValue) {
        super(defaultValue);
    }

    public DoubleOption(double defaultValue, double min, double max) {
        super(defaultValue, min, max);
    }

    public DoubleOption(double defaultValue, Collection<Double> allowedValues) {
        super(defaultValue, allowedValues);
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        if (rangeRestricted) {
            value = addComment(builder, field, "Default: " + defaultValue)
                    .defineInRange(getName(field), defaultValue, min, max);
        }
        else {
            super.init(builder, field);
        }
    }

    public float getAsFloat() {
        return (float) (double) get();
    }
}
