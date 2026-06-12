package azmalent.cuneiform.common.data.conditions;

import azmalent.cuneiform.Cuneiform;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.apache.commons.lang3.StringUtils;

/**
 * A recipe condition that checks a boolean config flag registered with
 * {@link ConfigFlagManager}.
 *
 * <p>JSON format:</p>
 * <pre>{@code
 * {"type": "cuneiform:config", "config": "modid:flag_name"}
 * }</pre>
 *
 * <p>When the flag evaluates to {@code true}, the recipe is loaded. When
 * {@code false}, the recipe is excluded.</p>
 *
 * @param modid the mod ID the flag belongs to
 * @param flag the flag name
 */
public record RecipeConfigCondition(String modid, String flag) implements ICondition {
    public static final ResourceLocation ID = Cuneiform.prefix("config");

    @Deprecated(forRemoval = true)
    public RecipeConfigCondition(String modid, String flag, ResourceLocation id) {
        this(modid, flag);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        return ConfigFlagManager.getFlag(modid, flag);
    }

    /**
     * Serializer for {@link RecipeConfigCondition}.
     */
    public record Serializer() implements IConditionSerializer<RecipeConfigCondition> {
        @Override
        public void write(JsonObject json, RecipeConfigCondition value) {
            json.addProperty("config", value.modid + ":" + value.flag);
        }

        @Override
        public RecipeConfigCondition read(JsonObject json) {
            String string = json.getAsJsonPrimitive("config").getAsString();
            String[] tokens = StringUtils.split(string, ":", 2);
            return new RecipeConfigCondition(tokens[0], tokens[1]);
        }

        @Override
        public ResourceLocation getID() {
            return ID;
        }
    }
}
