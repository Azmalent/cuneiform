package azmalent.cuneiform.common.data.conditions;

import azmalent.cuneiform.Cuneiform;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.apache.commons.lang3.StringUtils;

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
    @SuppressWarnings("removal")
    public boolean test() {
        return ConfigFlagManager.getFlag(modid, flag);
    }

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
