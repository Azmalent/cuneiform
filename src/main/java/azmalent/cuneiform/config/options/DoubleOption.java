package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("unused")
public final class DoubleOption extends NumericOption<Double> {
    private DoubleOption(double defaultValue) {
        super(defaultValue);
    }

    private DoubleOption(double defaultValue, double min, double max) {
        super(defaultValue, min, max);
    }

    public static DoubleOption of(double defaultValue) {
        return new DoubleOption(defaultValue);
    }

    public static DoubleOption inRange(double defaultValue, double min, double max) {
        return new DoubleOption(defaultValue, min, max);
    }

    public static DoubleOption inUnitRange(double defaultValue) {
        return inRange(defaultValue, 0, 1);
    }

    @Override
    protected ForgeConfigSpec.ConfigValue<Double> defineRange(ForgeConfigSpec.Builder builder, String name) {
        return builder.defineInRange(name, defaultValue, min, max);
    }
}
