package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

/**
 * An enum config option that allows selecting a value from a predefined set.
 *
 * <p>Only the explicitly provided {@code allowedValues} will be valid choices
 * in the config file, which may be a subset of the enum's constants.</p>
 *
 * @param <E> the enum type
 */
public final class EnumOption<E extends Enum<E>> extends BasicOption<E> {
    private final E[] allowedValues;

    private EnumOption(E defaultValue, E[] allowedValues) {
        super(defaultValue);
        this.allowedValues = allowedValues;
    }

    /**
     * Creates an enum option with the given default and allowed values.
     */
    public static <E extends Enum<E>> EnumOption<E> of(E defaultValue, E[] allowedValues) {
        return new EnumOption<E>(defaultValue, allowedValues);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue.toString().toUpperCase())
                .defineEnum(getFieldName(field), defaultValue, allowedValues);
    }
}
