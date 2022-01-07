package azmalent.cuneiform.mixin;

import azmalent.cuneiform.common.crafting.StrippingByproductRecipe;
import azmalent.cuneiform.lib.util.CraftingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private void spawnByproduct(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        if (level.isClientSide) return;

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        for (Recipe<RecipeWrapper> recipe : CraftingUtil.getRecipesByType(level, StrippingByproductRecipe.TYPE).values()) {
            if (recipe instanceof StrippingByproductRecipe strippingRecipe) {
                if (strippingRecipe.matches(state) && level.random.nextFloat() < strippingRecipe.getChance()) {
                    Block.popResource(level, pos, strippingRecipe.getOutput());
                }
            }
        }
    }
}
