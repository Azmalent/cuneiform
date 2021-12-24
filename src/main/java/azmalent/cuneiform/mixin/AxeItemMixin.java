package azmalent.cuneiform.mixin;

import net.minecraft.world.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    //TODO!

    /*    @Inject(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private void spawnByproduct(ItemUseContext context, CallbackInfoReturnable<ActionResultType> cir) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);

        RecipeUtil.getRecipes(world, StrippingByproductRecipe.TYPE).values().stream()
            .filter(recipe -> recipe instanceof StrippingByproductRecipe)
            .map(recipe -> (StrippingByproductRecipe) recipe)
            .forEach(recipe -> {
                if (recipe.matches(state) && world.rand.nextFloat() < recipe.getChance()) {
                    Block.spawnAsEntity(world, pos, recipe.getOutput());
                }
            });
    }*/
}
