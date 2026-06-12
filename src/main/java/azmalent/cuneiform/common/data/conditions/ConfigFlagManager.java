package azmalent.cuneiform.common.data.conditions;

import azmalent.cuneiform.Cuneiform;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Manages boolean config flags that can be referenced in recipe and loot table
 * JSON conditions.
 *
 * <p>Flags are registered automatically from {@link azmalent.cuneiform.config.options.BooleanOption}
 * instances with a non-null config flag name (COMMON config only). They can then
 * be referenced in JSON files:</p>
 *
 * <pre>{@cuneiform
 * {"type": "cuneiform:config", "config": "modid:flag_name"}
 * }</pre>
 *
 * <p>During {@link FMLCommonSetupEvent}, the condition serializers are registered
 * with Forge's {@link CraftingHelper} and the loot condition type registry.</p>
 */
public final class ConfigFlagManager {
    public static final Map<String, Map<String, Supplier<Boolean>>> flagsByModid = new HashMap<>();

    private static boolean initialized = false;

    /**
     * Sets up the config condition serializers during {@link FMLCommonSetupEvent}.
     * Only runs once; subsequent calls are ignored.
     *
     * @param event the common setup event
     */
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        if (initialized) {
            return;
        }

        CraftingHelper.register(new RecipeConfigCondition.Serializer());
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, RecipeConfigCondition.ID, LootConfigCondition.TYPE);

        initialized = true;
    }

    /**
     * Registers a config flag from a {@link ResourceLocation}.
     *
     * @param flag the flag as a resource location (namespace = modid, path = flag name)
     * @param value a supplier returning the flag's current value
     */
    public static void putFlag(ResourceLocation flag, Supplier<Boolean> value) {
        putFlag(flag.getNamespace(), flag.getPath(), value);
    }

    /**
     * Registers a config flag.
     *
     * @param modid the mod ID the flag belongs to
     * @param flag the flag name
     * @param value a supplier returning the flag's current value
     */
    public static void putFlag(String modid, String flag, Supplier<Boolean> value) {
        if (!flagsByModid.containsKey(modid)) {
            flagsByModid.put(modid, Maps.newHashMap());
        }

        flagsByModid.get(modid).put(flag, value);
    }

    /**
     * Gets the current value of a config flag.
     *
     * @param id the flag as a resource location (namespace = modid, path = flag name)
     * @return the flag's value, or {@code false} if not found
     */
    public static boolean getFlag(ResourceLocation id) {
        return getFlag(id.getNamespace(), id.getPath());
    }

    /**
     * Gets the current value of a config flag.
     *
     * <p>If the flag is not found and the mod is loaded, a warning is logged
     * and the flag is registered with a default value of {@code false}.</p>
     *
     * @param modid the mod ID the flag belongs to
     * @param flag the flag name
     * @return the flag's value, or {@code false} if not found
     */
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
