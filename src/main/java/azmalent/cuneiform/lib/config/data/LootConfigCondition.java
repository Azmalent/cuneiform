
package azmalent.cuneiform.lib.config.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

import javax.annotation.Nonnull;

public class LootConfigCondition implements ILootCondition {
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
    public LootConditionType func_230419_b_() {
        return ConfigFlagManager.LOOT_CONFIG_CONDITION;
    }

    public static class Serializer implements ILootSerializer<LootConfigCondition> {
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
