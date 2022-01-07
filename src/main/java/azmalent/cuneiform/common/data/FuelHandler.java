package azmalent.cuneiform.common.data;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;

import java.util.Map;

@SuppressWarnings("unused")
public final class FuelHandler {
    private static final Map<Item, Integer> FUELS = Maps.newHashMap();

    public static void registerFuel(float itemsPerFuel, ItemLike... fuels) {
        for (ItemLike item : fuels) {
            FUELS.put(item.asItem(), (int) (itemsPerFuel * 200));
        }
    }

    public static void getBurnTime(FurnaceFuelBurnTimeEvent event) {
        Item item = event.getItemStack().getItem();
        if (FUELS.containsKey(item)) {
            event.setBurnTime(FUELS.get(item));
        }
    }
}
