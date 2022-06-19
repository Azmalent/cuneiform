package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class StringOption extends BasicOption<String> {
    private StringOption(String defaultValue) {
        super(defaultValue);
    }

    public static StringOption of(String defaultValue) {
        return new StringOption(defaultValue);
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue)
                .define(getFieldName(field), defaultValue);
    }
}
