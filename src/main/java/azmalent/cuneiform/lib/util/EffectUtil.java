package azmalent.cuneiform.lib.util;

import azmalent.cuneiform.Cuneiform;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

public final class EffectUtil {
    public final void applyScaledEffect(EffectInstance effect, float scale, LivingEntity livingEntity) {
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
}
