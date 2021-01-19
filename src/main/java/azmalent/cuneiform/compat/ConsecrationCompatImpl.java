package azmalent.cuneiform.compat;

import azmalent.cuneiform.lib.compat.ModProxyImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import top.theillusivec4.consecration.common.ConsecrationConfig;
import top.theillusivec4.consecration.common.ConsecrationUtils;
import top.theillusivec4.consecration.common.capability.UndyingCapability;

@ModProxyImpl(IConsecrationCompat.MODID)
public class ConsecrationCompatImpl implements IConsecrationCompat {
    @Override
    public void smiteWithHealingPotion(LivingEntity livingEntity) {
        if (!livingEntity.getEntityWorld().isRemote && ConsecrationUtils.isUndying(livingEntity)
                && ConsecrationUtils.isHolyEffect(Effects.INSTANT_HEALTH)) {
            UndyingCapability.getCapability(livingEntity).ifPresent(undying -> {
                undying.setSmiteDuration(ConsecrationConfig.holySmiteDuration * 20);
            });
        }
    }
}

