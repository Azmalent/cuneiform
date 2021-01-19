package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

public final class EnumOption<E extends Enum<E>> extends AbstractConfigOption<E, E> {
    private ForgeConfigSpec.EnumValue<E> value;
    private E defaultValue;
    private E[] allowedValues;

    public EnumOption(E defaultValue, E[] allowedValues) {
        this.defaultValue = defaultValue;
        this.allowedValues = allowedValues;
    }

    public E get() {
        return value.get();
    }

    public void set(E newValue) {
        value.set(newValue);
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue.toString().toUpperCase())
                .defineEnum(getName(field), defaultValue, allowedValues);
    }
}
