package azmalent.cuneiform.lib.util;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;

@SuppressWarnings("unused")
public final class LootUtil {
    public static LootPool getPoolByIndex(LootTable table, int index) {
        return table.pools.get(index);
    }

    public static void addEntry(LootPool pool, LootEntry entry) {
        pool.lootEntries.add(entry);
    }
}
