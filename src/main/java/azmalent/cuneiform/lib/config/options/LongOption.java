package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.Collection;

public final class LongOption extends AbstractNumericOption<Long> {
    public LongOption(long defaultValue) {
        super(defaultValue);
    }

    public LongOption(long defaultValue, long min, long max) {
        super(defaultValue, min, max);
    }

    public LongOption(long defaultValue, Collection<Long> allowedValues) {
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
}
