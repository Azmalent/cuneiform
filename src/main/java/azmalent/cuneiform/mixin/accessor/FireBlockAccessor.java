package azmalent.cuneiform.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockAccessor {
    @Invoker("setFireInfo")
    void cuneiform_setFireInfo(Block block, int encouragement, int flammability);
}
