package azmalent.cuneiform.mixin;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.CuneiformConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ArrowEntity.class)
class ArrowEntityMixin {
    @Shadow private Potion potion;

    @Shadow @Final private Set<EffectInstance> customPotionEffects;

    @SuppressWarnings("ConstantConditions")
    private void applyScaledEffect(EffectInstance effect, float scale, LivingEntity livingEntity) {
        Effect potion = effect.getPotion();
        int amplifier = effect.getAmplifier();
        boolean undead = livingEntity.isEntityUndead();

        if (potion == Effects.INSTANT_DAMAGE && !undead || potion == Effects.INSTANT_HEALTH && undead) {
            float damage = (6 << amplifier) * scale;
            livingEntity.attackEntityFrom(DamageSource.MAGIC, damage);
            Cuneiform.CONSECRATION_COMPAT.smiteWithHealingPotion(livingEntity);
        } else if (potion == Effects.INSTANT_HEALTH && !undead || potion == Effects.INSTANT_DAMAGE && undead) {
            float healing = (4 << effect.getAmplifier()) * scale;
            livingEntity.heal(healing);
        } else {
            int duration = Math.max((int) (effect.getDuration() * scale), 1);
            livingEntity.addPotionEffect(
                new EffectInstance(potion, duration, effect.getAmplifier(), effect.isAmbient(), effect.doesShowParticles())
            );
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "arrowHit(Lnet/minecraft/entity/LivingEntity;)V", cancellable = true)
    public void arrowHit(LivingEntity livingEntity, CallbackInfo ci) {
        if (CuneiformConfig.Common.Tweaks.tippedArrowPatch.get()) {
            float scaling = CuneiformConfig.Common.Tweaks.tippedArrowScaling.getAsFloat();
            for(EffectInstance effect : this.potion.getEffects()) {
                applyScaledEffect(effect, scaling, livingEntity);
            }

            for(EffectInstance effect : this.customPotionEffects) {
                applyScaledEffect(effect, scaling, livingEntity);
            }

            ci.cancel();
        }
    }
}
