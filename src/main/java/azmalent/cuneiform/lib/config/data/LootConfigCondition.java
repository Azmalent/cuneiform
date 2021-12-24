
package azmalent.cuneiform.lib.config.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import javax.annotation.Nonnull;

public class LootConfigCondition implements LootItemCondition {
    private final String modid;
    private final String flag;

    public LootConfigCondition(String modid, String flag) {
        this.modid = modid;
        this.flag = flag;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return ConfigFlagManager.getFlag(modid, flag);
    }

    @Nonnull
    @Override
    public LootItemConditionType getType() {
        return ConfigFlagManager.LOOT_CONFIG_CONDITION;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootConfigCondition> {
        @Override
        public void serialize(@Nonnull JsonObject json, @Nonnull LootConfigCondition value, @Nonnull JsonSerializationContext context) {
            json.addProperty("config", value.modid + ":" + value.flag);
        }

        @Nonnull
        @Override
        public LootConfigCondition deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            String[] tokens = json.getAsJsonPrimitive("config").getAsString().split(":", 2);
            return new LootConfigCondition(tokens[0], tokens[1]);
        }
    }
}
