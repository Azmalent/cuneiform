package azmalent.cuneiform.common.event;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;

import java.util.Map;

public class FuelHandler {
    private static final Map<Item, Integer> FUELS = Maps.newHashMap();

    public static void registerFuel(ItemLike itemProvider, int duration) {
        FUELS.put(itemProvider.asItem(), duration);
    }

    public static void getBurnTime(FurnaceFuelBurnTimeEvent event) {
        Item item = event.getItemStack().getItem();
        if (FUELS.containsKey(item)) {
            event.setBurnTime(FUELS.get(item));
        }
    }
}
