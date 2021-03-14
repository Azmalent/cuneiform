package azmalent.cuneiform.common.integration.consecration;

import azmalent.cuneiform.lib.compat.ModProxyDummy;
import net.minecraft.entity.LivingEntity;

@ModProxyDummy(IConsecrationCompat.MODID)
public class ConsecrationCompatDummy implements IConsecrationCompat {
    @Override
    public void smiteWithHealingPotion(LivingEntity livingEntity) {

    }
}
