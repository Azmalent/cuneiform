
package azmalent.cuneiform.common.data.conditions;

import com.electronwill.nightconfig.core.utils.StringUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import javax.annotation.Nonnull;
import java.util.List;

public record LootConfigCondition(String modid, String flag) implements LootItemCondition {
    public static LootItemConditionType TYPE = new LootItemConditionType(new LootConfigCondition.Serializer());

    @Nonnull
    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return ConfigFlagManager.getFlag(modid, flag);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootConfigCondition> {
        @Override
        public void serialize(@Nonnull JsonObject json, @Nonnull LootConfigCondition value, @Nonnull JsonSerializationContext context) {
            json.addProperty("config", value.modid + ":" + value.flag);
        }

        @Nonnull
        @Override
        public LootConfigCondition deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            List<String> tokens = StringUtils.split(json.getAsJsonPrimitive("config").getAsString(), ':');
            return new LootConfigCondition(tokens.get(0), tokens.get(1));
        }
    }
}
