package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * An integer config option, optionally constrained to a range.
 *
 * <p>Provides factory methods for common range patterns:</p>
 * <ul>
 *   <li>{@link #of(int)} — no range constraint</li>
 *   <li>{@link #inRange(int, int, int)} — custom range</li>
 *   <li>{@link #positive(int)} — minimum 1</li>
 *   <li>{@link #negative(int)} — maximum -1</li>
 *   <li>{@link #nonNegative(int)} — minimum 0</li>
 *   <li>{@link #nonPositive(int)} — maximum 0</li>
 * </ul>
 */
public final class IntOption extends NumericOption<Integer> {
    private IntOption(int defaultValue) {
        super(defaultValue);
    }

    private IntOption(int defaultValue, int min, int max) {
        super(defaultValue, min, max);
    }

    /**
     * Creates an integer option with no range constraint.
     */
    public static IntOption of(int defaultValue) {
        return new IntOption(defaultValue);
    }

    /**
     * Creates an integer option constrained to the given range (inclusive).
     */
    public static IntOption inRange(int defaultValue, int min, int max) {
        return new IntOption(defaultValue, min, max);
    }

    /**
     * Creates an integer option with a minimum value of 1.
     */
    public static IntOption positive(int defaultValue) {
        return inRange(defaultValue, 1, Integer.MAX_VALUE);
    }

    /**
     * Creates an integer option with a maximum value of -1.
     */
    public static IntOption negative(int defaultValue) {
        return inRange(defaultValue, Integer.MIN_VALUE, -1);
    }

    /**
     * Creates an integer option with a maximum value of 0.
     */
    public static IntOption nonPositive(int defaultValue) {
        return inRange(defaultValue, Integer.MIN_VALUE, 0);
    }

    /**
     * Creates an integer option with a minimum value of 0.
     */
    public static IntOption nonNegative(int defaultValue) {
        return inRange(defaultValue, 0, Integer.MAX_VALUE);
    }

    @Override
    protected ForgeConfigSpec.ConfigValue<Integer> defineRange(ForgeConfigSpec.Builder builder, String name) {
        return builder.defineInRange(name, defaultValue, min, max);
    }
}
