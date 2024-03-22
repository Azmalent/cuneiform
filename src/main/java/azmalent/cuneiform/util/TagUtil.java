package azmalent.cuneiform.util;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;

public final class TagUtil {
    public TagKey<Block> blockTag(String modid, String name) {
        return BlockTags.create(new ResourceLocation(modid, name));
    }

    public TagKey<Item> itemTag(String modid, String name) {
        return ItemTags.create(new ResourceLocation(modid, name));
    }

    public TagKey<EntityType<?>> entityTag(String modid, String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(modid, name));
    }

    public TagKey<Biome> biomeTag(String modid, String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(modid, name));
    }
}
