package azmalent.cuneiform.common.data;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.WandererTradesEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Registers custom trades for the Wandering Trader.
 *
 * <p>Call {@link #addCommonTrade} and {@link #addRareTrade} during mod
 * initialization to add trades. The trades are applied during
 * {@link WandererTradesEvent}.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * WanderingTraderHandler.addCommonTrade(TradeBuilder.sell(1, new ItemStack(Items.EMERALD)).build());
 * WanderingTraderHandler.addRareTrade(TradeBuilder.sell(5, new ItemStack(Items.DIAMOND)).build());
 * }</pre>
 */
public final class WanderingTraderHandler {
    private static final List<VillagerTrades.ItemListing> commonTrades = Lists.newArrayList();
    private static final List<VillagerTrades.ItemListing> rareTrades = Lists.newArrayList();

    /**
     * Adds a common trade to the Wandering Trader.
     */
    public static void addCommonTrade(VillagerTrades.ItemListing trade) {
        commonTrades.add(trade);
    }

    /**
     * Adds multiple common trades to the Wandering Trader.
     */
    public static void addCommonTrades(VillagerTrades.ItemListing... trades) {
        commonTrades.addAll(Arrays.asList(trades));
    }

    /**
     * Adds a rare trade to the Wandering Trader.
     */
    public static void addRareTrade(VillagerTrades.ItemListing trade) {
        rareTrades.add(trade);
    }

    /**
     * Adds multiple rare trades to the Wandering Trader.
     */
    public static void addRareTrades(VillagerTrades.ItemListing... trades) {
        rareTrades.addAll(Arrays.asList(trades));
    }

    /**
     * Event handler for {@link WandererTradesEvent}. Adds all registered
     * common and rare trades to the event.
     */
    public static void registerTrades(WandererTradesEvent event) {
        event.getGenericTrades().addAll(commonTrades);
        event.getRareTrades().addAll(rareTrades);
    }
}
