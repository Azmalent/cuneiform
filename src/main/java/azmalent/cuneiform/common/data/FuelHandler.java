package azmalent.cuneiform.common.data;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;

import java.util.Map;

/**
 * Registers custom furnace fuels and handles burn time events.
 *
 * <p>Call {@link #registerFuel(float, ItemLike...)} during mod initialization
 * to register items as fuels. The burn time is calculated as
 * {@code itemsPerFuel * 200} ticks.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * FuelHandler.registerFuel(3, ModBlocks.COAL_BLOCK);
 * // Registers coal block as fuel with burn time of 600 ticks (3 items * 200)
 * }</pre>
 */
public final class FuelHandler {
    private static final Map<Item, Integer> FUELS = Maps.newHashMap();

    /**
     * Registers one or more items as furnace fuels.
     *
     * @param itemsPerFuel the number of items smelted per fuel unit; the burn time is calculated as {@code itemsPerFuel * 200} ticks
     * @param fuels the items to register as fuel
     */
    public static void registerFuel(float itemsPerFuel, ItemLike... fuels) {
        for (var item : fuels) {
            FUELS.put(item.asItem(), (int) (itemsPerFuel * 200));
        }
    }

    /**
     * Event handler for {@link FurnaceFuelBurnTimeEvent}. Checks if the item
     * is a registered fuel and sets the burn time accordingly.
     */
    public static void getBurnTime(FurnaceFuelBurnTimeEvent event) {
        var item = event.getItemStack().getItem();
        if (FUELS.containsKey(item)) {
            event.setBurnTime(FUELS.get(item));
        }
    }
}
