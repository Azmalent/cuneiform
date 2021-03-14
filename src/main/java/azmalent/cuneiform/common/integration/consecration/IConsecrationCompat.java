package azmalent.cuneiform.common.integration.consecration;

import net.minecraft.entity.LivingEntity;

public interface IConsecrationCompat {
    String MODID = "consecration";

    void smiteWithHealingPotion(LivingEntity livingEntity);
}

