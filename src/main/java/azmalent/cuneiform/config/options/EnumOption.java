package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class EnumOption<E extends Enum<E>> extends BasicOption<E> {
    private final E[] allowedValues;

    private EnumOption(E defaultValue, E[] allowedValues) {
        super(defaultValue);
        this.allowedValues = allowedValues;
    }

    public static <E extends Enum<E>> EnumOption<E> of(E defaultValue, E[] allowedValues) {
        return new EnumOption<E>(defaultValue, allowedValues);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue.toString().toUpperCase())
                .defineEnum(getFieldName(field), defaultValue, allowedValues);
    }
}
