package azmalent.cuneiform.mixin;

import azmalent.cuneiform.CuneiformConfig;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SpongeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpongeBlock.class)
public abstract class SpongeBlockMixin extends Block {
    public SpongeBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (CuneiformConfig.Common.Tweaks.spongeFallDamagePatch.get()) {
            entityIn.onLivingFall(fallDistance, 0.2F);
        }
    }
}
