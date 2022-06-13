package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

@SuppressWarnings({"unused"})
public final class BooleanOption extends BasicConfigOption<Boolean> {
    private final String configFlag;

    private BooleanOption(boolean defaultValue, String configFlag) {
        super(defaultValue);
        this.configFlag = configFlag;
    }

    public static BooleanOption of(boolean defaultValue) {
        return of(defaultValue, null);
    }

    public static BooleanOption of(boolean defaultValue, String configFlag) {
        return new BooleanOption(defaultValue, configFlag);
    }

    public boolean hasFlag() {
        return configFlag != null;
    }

    public String getConfigFlag() {
        return configFlag;
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue).define(getFieldName(field), defaultValue);
    }
}
