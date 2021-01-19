package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

public final class StringOption extends AbstractConfigOption<String, String> {
    protected ForgeConfigSpec.ConfigValue<String> value;
    private final String defaultValue;

    public StringOption(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String get() {
        return value.get();
    }

    @Override
    public void set(String newValue) {
        value.set(newValue);
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue)
                .define(getName(field), defaultValue);
    }
}
