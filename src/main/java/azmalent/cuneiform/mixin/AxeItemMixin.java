package azmalent.cuneiform.mixin;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.common.crafting.StrippingByproductRecipe;
import azmalent.cuneiform.lib.network.message.S2CSpawnParticleMessage;
import azmalent.cuneiform.lib.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

import java.util.Random;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private void spawnByproduct(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        if (level.isClientSide) return;

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        for (Recipe<RecipeWrapper> recipe : RecipeUtil.getRecipesByType(level, StrippingByproductRecipe.TYPE).values()) {
            if (recipe instanceof StrippingByproductRecipe strippingRecipe) {
                if (strippingRecipe.matches(state) && level.random.nextFloat() < strippingRecipe.getChance()) {
                    Block.popResource(level, pos, strippingRecipe.getOutput());

                    for(int i = 0; i < 8; i++) {
                        Random random = context.getLevel().random;

                        double x = pos.getX() + random.nextDouble();
                        double y = pos.getY() + random.nextDouble();
                        double z = pos.getZ() + random.nextDouble();
                        double xSpeed = random.nextGaussian() * 0.02D;
                        double ySpeed = random.nextGaussian() * 0.02D;
                        double zSpeed = random.nextGaussian() * 0.02D;

                        S2CSpawnParticleMessage message = new S2CSpawnParticleMessage(ParticleTypes.HAPPY_VILLAGER, x, y, z, xSpeed, ySpeed, zSpeed);
                        Cuneiform.CHANNEL.sendToAllPlayers(message);
                    }
                }
            }
        }
    }
}
