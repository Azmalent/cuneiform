package azmalent.cuneiform.lib.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

@SuppressWarnings({"unused", "deprecation"})
public final class BiomeUtil {
    public static Biome getBiomeKey(int id) {
        return WorldGenRegistries.BIOME.getByValue(id);
    }

    public static RegistryKey<Biome> getBiomeKey(ResourceLocation id) {
        return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, id);
    }

    @SuppressWarnings("ConstantConditions")
    public static RegistryKey<Biome> getBiomeKey(Biome biome) {
        return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
    }

    @SuppressWarnings("ConstantConditions")
    public static RegistryKey<Biome> getBiomeKey(IWorldReader world, BlockPos pos) {
        return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, world.getBiome(pos).getRegistryName());
    }

    public static boolean hasAnyType(RegistryKey<Biome> biome, BiomeDictionary.Type... types) {
        for (BiomeDictionary.Type type : types) {
            if (BiomeDictionary.hasType(biome, type)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasAllTypes(RegistryKey<Biome> biome, BiomeDictionary.Type... types) {
        for (BiomeDictionary.Type type : types) {
            if (!BiomeDictionary.hasType(biome, type)) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasNoneOfTypes(RegistryKey<Biome> biome, BiomeDictionary.Type... types) {
        return !hasAnyType(biome, types);
    }
}
