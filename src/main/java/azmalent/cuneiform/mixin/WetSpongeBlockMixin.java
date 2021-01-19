package azmalent.cuneiform.mixin;

import azmalent.cuneiform.CuneiformConfig;
import net.minecraft.block.Block;
import net.minecraft.block.SpongeBlock;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WetSpongeBlock.class)
public abstract class WetSpongeBlockMixin extends Block {
    public WetSpongeBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (CuneiformConfig.Common.Tweaks.spongeFallDamagePatch.get()) {
            entityIn.onLivingFall(fallDistance, 0.2F);
        }
    }
}
