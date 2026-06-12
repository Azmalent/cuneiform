package azmalent.cuneiform.common.data.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

/**
 * A loot condition that checks a boolean config flag registered with
 * {@link ConfigFlagManager}.
 *
 * <p>JSON format:</p>
 * <pre>{@code
 * {"condition": "cuneiform:config", "config": "modid:flag_name"}
 * }</pre>
 *
 * <p>When the flag evaluates to {@code true}, the loot entry is active. When
 * {@code false}, the loot entry is excluded.</p>
 *
 * @param modid the mod ID the flag belongs to
 * @param flag the flag name
 */
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

    /**
     * Serializer for {@link LootConfigCondition}.
     */
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootConfigCondition> {
        @Override
        public void serialize(@Nonnull JsonObject json, @Nonnull LootConfigCondition value, @Nonnull JsonSerializationContext context) {
            json.addProperty("config", value.modid + ":" + value.flag);
        }

        @Nonnull
        @Override
        public LootConfigCondition deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            String[] tokens = StringUtils.split(json.getAsJsonPrimitive("config").getAsString(), ":", 2);
            return new LootConfigCondition(tokens[0], tokens[1]);
        }
    }
}
