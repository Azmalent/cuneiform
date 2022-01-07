package azmalent.cuneiform.common.data.conditions;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.apache.commons.lang3.StringUtils;

public record RecipeConfigCondition(String modid, String flag, ResourceLocation id) implements ICondition {
    @Override
    public ResourceLocation getID() {
        return id;
    }

    @Override
    public boolean test() {
        return ConfigFlagManager.getFlag(modid, flag);
    }

    public record Serializer(ResourceLocation location) implements IConditionSerializer<RecipeConfigCondition> {
        @Override
        public void write(JsonObject json, RecipeConfigCondition value) {
            json.addProperty("config", value.modid + ":" + value.flag);
        }

        @Override
        public RecipeConfigCondition read(JsonObject json) {
            String string = json.getAsJsonPrimitive("config").getAsString();
            String[] tokens = StringUtils.split(string, ":", 2);
            return new RecipeConfigCondition(tokens[0], tokens[1], location);
        }

        @Override
        public ResourceLocation getID() {
            return location;
        }
    }
}
