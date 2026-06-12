package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * A floating-point config option, optionally constrained to a range.
 *
 * <p>Provides factory methods for common range patterns:</p>
 * <ul>
 *   <li>{@link #of(double)} — no range constraint</li>
 *   <li>{@link #inRange(double, double, double)} — custom range</li>
 *   <li>{@link #inUnitRange(double)} — constrained to [0, 1]</li>
 * </ul>
 */
public final class DoubleOption extends NumericOption<Double> {
    private DoubleOption(double defaultValue) {
        super(defaultValue);
    }

    private DoubleOption(double defaultValue, double min, double max) {
        super(defaultValue, min, max);
    }

    /**
     * Creates a double option with no range constraint.
     */
    public static DoubleOption of(double defaultValue) {
        return new DoubleOption(defaultValue);
    }

    /**
     * Creates a double option constrained to the given range (inclusive).
     */
    public static DoubleOption inRange(double defaultValue, double min, double max) {
        return new DoubleOption(defaultValue, min, max);
    }

    /**
     * Creates a double option constrained to the unit range [0, 1].
     */
    public static DoubleOption inUnitRange(double defaultValue) {
        return inRange(defaultValue, 0, 1);
    }

    @Override
    protected ForgeConfigSpec.ConfigValue<Double> defineRange(ForgeConfigSpec.Builder builder, String name) {
        return builder.defineInRange(name, defaultValue, min, max);
    }
}
