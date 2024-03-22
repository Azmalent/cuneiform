package azmalent.cuneiform.common.data.conditions;

import azmalent.cuneiform.Cuneiform;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ConfigFlagManager {
    public static final Map<String, Map<String, Supplier<Boolean>>> flagsByModid = new HashMap<>();

    private static boolean initialized = false;

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        if (initialized) {
            return;
        }

        CraftingHelper.register(new RecipeConfigCondition.Serializer());
        Registry.register(Registry.LOOT_CONDITION_TYPE, RecipeConfigCondition.ID, LootConfigCondition.TYPE);

        initialized = true;
    }

    public static void putFlag(ResourceLocation flag, Supplier<Boolean> value) {
        putFlag(flag.getNamespace(), flag.getPath(), value);
    }

    public static void putFlag(String modid, String flag, Supplier<Boolean> value) {
        if (!flagsByModid.containsKey(modid)) {
            flagsByModid.put(modid, Maps.newHashMap());
        }

        flagsByModid.get(modid).put(flag, value);
    }

    public static boolean getFlag(ResourceLocation id) {
        return getFlag(id.getNamespace(), id.getPath());
    }

    public static boolean getFlag(String modid, String flag) {
        if (flagsByModid.containsKey(modid)) {
            var flagSupplier = flagsByModid.get(modid).get(flag);
            if (flagSupplier != null) {
                return flagSupplier.get();
            }
        }

        if (ModList.get().isLoaded(modid)) {
            Cuneiform.LOGGER.warn(String.format("Unknown flag '%s:%s', defaulting to false", modid, flag));
            putFlag(modid, flag, () -> false);
        }

        return false;
    }
}
