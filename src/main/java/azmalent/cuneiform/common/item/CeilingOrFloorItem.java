package azmalent.cuneiform.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class CeilingOrFloorItem extends BlockItem {
    protected final Block floorBlock;

    public CeilingOrFloorItem(Block ceilingBlock, Block floorBlock, Item.Properties properties) {
        super(ceilingBlock, properties);
        this.floorBlock = floorBlock;
    }

    @Nullable
    public BlockState getPlacementState(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        for(Direction direction : context.getNearestLookingDirections()) {
            BlockState state = null;
            if (direction == Direction.DOWN) {
                state = this.floorBlock.getStateForPlacement(context);
            }
            else if (direction == Direction.UP) {
                state = this.getBlock().getStateForPlacement(context);
            }

            if (state != null && state.canSurvive(world, pos)) {
                return world.isUnobstructed(state, pos, CollisionContext.empty()) ? state : null;
            }
        }

        return null;
    }

    @Override
    public void registerBlocks(@NotNull Map<Block, Item> blockToItemMap, @Nonnull Item itemIn) {
        super.registerBlocks(blockToItemMap, itemIn);
        blockToItemMap.put(this.floorBlock, itemIn);
    }

    @Override
    public void removeFromBlockToItemMap(@NotNull Map<Block, Item> blockToItemMap, @NotNull Item itemIn) {
        super.removeFromBlockToItemMap(blockToItemMap, Objects.requireNonNull(itemIn));
        blockToItemMap.remove(this.floorBlock);
    }
}

