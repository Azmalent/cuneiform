package azmalent.cuneiform.lib.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class BooleanOption extends AbstractConfigOption<Boolean, Boolean> {
    private ForgeConfigSpec.BooleanValue value;
    private final boolean defaultValue;
    private String flag;

    public BooleanOption(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean get() {
        return value.get();
    }

    public void set(Boolean value) {
        this.value.set(value);
    }

    public BooleanOption withFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public boolean hasFlag() {
        return flag != null;
    }

    public String getFlag() {
        return flag;
    }

    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue)
                .define(getName(field), defaultValue);
    }
}
