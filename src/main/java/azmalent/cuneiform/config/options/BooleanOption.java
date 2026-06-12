package azmalent.cuneiform.config.options;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

/**
 * A boolean config option that can optionally serve as a config flag for
 * gating recipes and loot tables via JSON conditions.
 *
 * <p>When created with {@link #of(boolean, String)}, the option registers a
 * named flag with the {@link azmalent.cuneiform.common.data.conditions.ConfigFlagManager},
 * making it usable in recipe and loot table JSON files:</p>
 *
 * <pre>{@code
 * {"type": "cuneiform:config", "config": "modid:flag_name"}
 * }</pre>
 *
 * <p>Flags are only valid in COMMON config files; they are ignored in CLIENT
 * and SERVER configs with a warning.</p>
 */
public final class BooleanOption extends BasicOption<Boolean> {
    private final String configFlag;

    private BooleanOption(boolean defaultValue, String configFlag) {
        super(defaultValue);
        this.configFlag = configFlag;
    }

    /**
     * Creates a boolean option with no config flag.
     */
    public static BooleanOption of(boolean defaultValue) {
        return of(defaultValue, null);
    }

    /**
     * Creates a boolean option with an optional config flag name.
     */
    public static BooleanOption of(boolean defaultValue, String configFlag) {
        return new BooleanOption(defaultValue, configFlag);
    }

    /**
     * Whether this option has an associated config flag.
     */
    public boolean hasFlag() {
        return configFlag != null;
    }

    /**
     * Returns the config flag name.
     */
    public String getConfigFlag() {
        return configFlag;
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field, "Default: " + defaultValue).define(getFieldName(field), defaultValue);
    }
}
