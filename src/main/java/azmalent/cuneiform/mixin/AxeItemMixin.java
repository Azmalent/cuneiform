package azmalent.cuneiform.mixin;

import azmalent.cuneiform.common.crafting.StrippingByproductRecipe;
import azmalent.cuneiform.util.CraftingUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin into {@link AxeItem} that injects stripping byproduct drops.
 */
@Mixin(AxeItem.class)
public class AxeItemMixin {
    /**
     * Injects after the block is set during stripping to spawn byproduct drops.
     *
     * @param context the use-on context
     * @param cir the callback info
     */
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private void spawnByproduct(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        var level = context.getLevel();
        if (level.isClientSide) return;

        var pos = context.getClickedPos();
        var state = level.getBlockState(pos);

        for (Recipe<?> recipe : CraftingUtil.getRecipesByType(level, StrippingByproductRecipe.TYPE).values()) {
            if (recipe instanceof StrippingByproductRecipe strippingRecipe) {
                float chance = strippingRecipe.getChance();

                if (strippingRecipe.matches(state) && level.random.nextFloat() < chance) {
                    Block.popResource(level, pos, strippingRecipe.getOutput());
                }
            }
        }
    }
}
