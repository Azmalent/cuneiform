package azmalent.cuneiform.util;

import azmalent.cuneiform.mixin.accessor.FireBlockAccessor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.List;
import java.util.function.Supplier;

public final class DataUtil {
    public static void registerFlammable(Supplier<? extends Block> block, int flameOdds, int burnOdds) {
        FireBlockAccessor fire = (FireBlockAccessor) Blocks.FIRE;
        fire.cuneiform_setFlammable(block.get(), flameOdds, burnOdds);
    }

    public static void registerCompostable(ItemLike itemProvider, float value) {
        ComposterBlock.COMPOSTABLES.put(itemProvider.asItem(), value);
    }

    @SuppressWarnings("ConstantConditions")
    public static void addLoot(LootTable table, LootPoolEntryContainer... addedEntries) {
        List<LootPool> pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "f_79109_");
        if (!pools.isEmpty() && addedEntries.length > 0) {
            LootPool pool = pools.get(0);
            LootPoolEntryContainer[] entries = pool.entries;

            var newEntries = new LootPoolEntryContainer[entries.length + addedEntries.length];
            System.arraycopy(entries, 0, newEntries, 0, entries.length);
            System.arraycopy(addedEntries, 0, newEntries, entries.length, addedEntries.length);

            pool.entries = newEntries;
        }
    }
}
