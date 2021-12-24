package azmalent.cuneiform.lib.config.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class RecipeConfigCondition implements ICondition {
    private final String modid;
    private final String flag;
    private final ResourceLocation id;

    public RecipeConfigCondition(String modid, String flag, ResourceLocation id) {
        this.modid = modid;
        this.flag = flag;
        this.id = id;
    }

    @Override
    public ResourceLocation getID() {
        return id;
    }

    @Override
    public boolean test() {
        return ConfigFlagManager.getFlag(modid, flag);
    }

    public static class Serializer implements IConditionSerializer<RecipeConfigCondition> {
        private final ResourceLocation location;

        public Serializer(ResourceLocation location) {
            this.location = location;
        }

        @Override
        public void write(JsonObject json, RecipeConfigCondition value) {
            json.addProperty("config", value.modid + ":" + value.flag);
        }

        @Override
        public RecipeConfigCondition read(JsonObject json) {
            String[] tokens = json.getAsJsonPrimitive("config").getAsString().split(":", 2);
            return new RecipeConfigCondition(tokens[0], tokens[1], location);
        }

        @Override
        public ResourceLocation getID() {
            return location;
        }
    }
}
