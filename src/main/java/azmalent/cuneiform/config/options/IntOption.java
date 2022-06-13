package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("unused")
public final class IntOption extends NumericOption<Integer> {
    private IntOption(int defaultValue) {
        super(defaultValue);
    }

    private IntOption(int defaultValue, int min, int max) {
        super(defaultValue, min, max);
    }

    public static IntOption of(int defaultValue) {
        return new IntOption(defaultValue);
    }

    public static IntOption inRange(int defaultValue, int min, int max) {
        return new IntOption(defaultValue, min, max);
    }

    public static IntOption positive(int defaultValue) {
        return inRange(defaultValue, 1, Integer.MAX_VALUE);
    }

    public static IntOption negative(int defaultValue) {
        return inRange(defaultValue, Integer.MIN_VALUE, -1);
    }

    public static IntOption nonPositive(int defaultValue) {
        return inRange(defaultValue, Integer.MIN_VALUE, 0);
    }

    public static IntOption nonNegative(int defaultValue) {
        return inRange(defaultValue, 0, Integer.MAX_VALUE);
    }

    @Override
    protected ForgeConfigSpec.ConfigValue<Integer> defineRange(ForgeConfigSpec.Builder builder, String name) {
        return builder.defineInRange(name, defaultValue, min, max);
    }
}
