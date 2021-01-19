package azmalent.cuneiform.mixin;

import azmalent.cuneiform.CuneiformConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerEntity.class)
abstract class ShulkerEntityMixin extends GolemEntity {
    public ShulkerEntityMixin(EntityType<? extends ShulkerEntity> p_i50196_1_, World p_i50196_2_) {
        super(p_i50196_1_, p_i50196_2_);
    }

    @Shadow protected abstract boolean isClosed();

    @Shadow protected abstract boolean tryTeleportToNewPosition();

    @Inject(at = @At(value = "HEAD"), method = "attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", cancellable = true)
    public void attackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!CuneiformConfig.Common.Tweaks.shulkerPiercingPatch.get()) {
            return;
        }

        if (this.isClosed()) {
            Entity entity = source.getImmediateSource();
            if (entity instanceof AbstractArrowEntity) {
                AbstractArrowEntity arrow = (AbstractArrowEntity) entity;
                if (arrow.getPierceLevel() <= 0) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }

        if (super.attackEntityFrom(source, amount)) {
            if ((double)this.getHealth() < (double)this.getMaxHealth() * 0.5D && this.rand.nextInt(4) == 0) {
                this.tryTeleportToNewPosition();
            }

            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }
}
