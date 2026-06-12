package azmalent.cuneiform.mixin.accessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Mixin accessor for {@link FireBlock} that provides access to the
 * {@code setFlammable} method.
 *
 * <p>Used by {@link azmalent.cuneiform.util.DataUtil#registerFlammable} to
 * register custom flammable blocks.</p>
 */
@Mixin(FireBlock.class)
public interface FireBlockAccessor {
    /**
     * Invokes {@code FireBlock.setFlammable} to register a block as flammable.
     *
     * @param block the block to register
     * @param flameOdds the probability of catching fire
     * @param burnOdds the probability of being consumed by fire
     */
    @Invoker("setFlammable")
    void cuneiform_setFlammable(Block block, int flameOdds, int burnOdds);
}
