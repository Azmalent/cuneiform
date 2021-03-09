package azmalent.cuneiform.lib.util;

import azmalent.cuneiform.lib.registry.BlockEntry;
import azmalent.cuneiform.mixin.accessor.FireBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

@SuppressWarnings("unused")
public final class DataUtil {
    public static void registerFlammable(Block block, int encouragement, int flammability) {
        if (block != null) {
            FireBlockAccessor fire = (FireBlockAccessor) Blocks.FIRE;
            fire.cuneiform_setFireInfo(block, encouragement, flammability);
        }
    }

    public static void registerFlammable(BlockEntry blockEntry, int encouragement, int flammability) {
        if (blockEntry != null) {
            registerFlammable(blockEntry.getBlock(), encouragement, flammability);
        }
    }
}
