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

/**
 * Utility methods for registering flammable blocks, compostable items, and
 * manipulating loot tables.
 */
public final class DataUtil {
    /**
     * Registers a block as flammable (can catch fire and burn).
     *
     * <p>Uses a Mixin accessor to call {@code FireBlock.setFlammable}.</p>
     *
     * @param block the block supplier
     * @param flameOdds the probability of catching fire (higher = more flammable)
     * @param burnOdds the probability of being consumed by fire (higher = burns faster)
     */
    public static void registerFlammable(Supplier<? extends Block> block, int flameOdds, int burnOdds) {
        var fire = (FireBlockAccessor) Blocks.FIRE;
        fire.cuneiform_setFlammable(block.get(), flameOdds, burnOdds);
    }

    /**
     * Registers an item as compostable in the composter.
     *
     * @param itemProvider the item to register
     * @param value the chance increase per composting layer (0.0–1.0)
     */
    public static void registerCompostable(ItemLike itemProvider, float value) {
        ComposterBlock.COMPOSTABLES.put(itemProvider.asItem(), value);
    }

    /**
     * Appends additional entries to the first loot pool of a loot table.
     */
    @SuppressWarnings("ConstantConditions")
    public static void addLoot(LootTable table, LootPoolEntryContainer... addedEntries) {
        List<LootPool> pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "f_79109_");
        if (!pools.isEmpty() && addedEntries.length > 0) {
            var pool = pools.get(0);
            var entries = pool.entries;

            var newEntries = new LootPoolEntryContainer[entries.length + addedEntries.length];
            System.arraycopy(entries, 0, newEntries, 0, entries.length);
            System.arraycopy(addedEntries, 0, newEntries, entries.length, addedEntries.length);

            pool.entries = newEntries;
        }
    }
}
