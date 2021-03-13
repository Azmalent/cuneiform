package azmalent.cuneiform.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class CeilingOrFloorItem extends BlockItem {
    protected final Block floorBlock;

    public CeilingOrFloorItem(Block ceilingBlock, Block floorBlock, Item.Properties properties) {
        super(ceilingBlock, properties);
        this.floorBlock = floorBlock;
    }

    @Nullable
    protected BlockState getStateForPlacement(BlockItemUseContext context) {
        IWorldReader world = context.getWorld();
        BlockPos pos = context.getPos();

        for(Direction direction : context.getNearestLookingDirections()) {
            BlockState state = null;
            if (direction == Direction.DOWN) {
                state = this.floorBlock.getStateForPlacement(context);
            }
            else if (direction == Direction.UP) {
                state = this.getBlock().getStateForPlacement(context);
            }

            if (state != null && state.isValidPosition(world, pos)) {
                return world.placedBlockCollides(state, pos, ISelectionContext.dummy()) ? state : null;
            }
        }

        return null;
    }

    @Override
    public void addToBlockToItemMap(Map<Block, Item> blockToItemMap, @Nonnull Item itemIn) {
        super.addToBlockToItemMap(blockToItemMap, itemIn);
        blockToItemMap.put(this.floorBlock, itemIn);
    }

    @Override
    public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn) {
        super.removeFromBlockToItemMap(blockToItemMap, itemIn);
        blockToItemMap.remove(this.floorBlock);
    }
}

