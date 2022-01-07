package azmalent.cuneiform.common.data.conditions;

import azmalent.cuneiform.Cuneiform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public final class ConfigFlagManager {
    public static final Map<String, Map<String, Boolean>> flagsByModid = new HashMap<>();

    static {
        ResourceLocation id = Cuneiform.prefix("config");
        CraftingHelper.register(new RecipeConfigCondition.Serializer(id));

        Registry.register(Registry.LOOT_CONDITION_TYPE, id, LootConfigCondition.TYPE);
    }

    public static void putFlag(ResourceLocation flag, boolean value) {
        String[] tokens = StringUtils.split(flag.toString(), ":", 2);
        putFlag(tokens[0], tokens[1], value);
    }

    public static void putFlag(String modid, String flag, boolean value) {
        if (!flagsByModid.containsKey(modid)) flagsByModid.put(modid, new HashMap<>());
        flagsByModid.get(modid).put(flag, value);
    }

    public static boolean getFlag(String modid, String flag) {
        if (flagsByModid.containsKey(modid)) {
            Map<String, Boolean> modFlags = flagsByModid.get(modid);
            if (modFlags.containsKey(flag)) {
                return modFlags.get(flag);
            }
        }

        if (ModList.get().isLoaded(modid)) {
            Cuneiform.LOGGER.warn(String.format("Unknown flag '%s:%s', defaulting to false", modid, flag));
        }

        return false;
    }
}
