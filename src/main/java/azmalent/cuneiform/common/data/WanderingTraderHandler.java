package azmalent.cuneiform.common.data;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.WandererTradesEvent;

import java.util.Arrays;
import java.util.List;

public final class WanderingTraderHandler {
    private static final List<VillagerTrades.ItemListing> commonTrades = Lists.newArrayList();
    private static final List<VillagerTrades.ItemListing> rareTrades = Lists.newArrayList();

    public static void addCommonTrade(VillagerTrades.ItemListing trade) {
        commonTrades.add(trade);
    }

    public static void addCommonTrades(VillagerTrades.ItemListing... trades) {
        commonTrades.addAll(Arrays.asList(trades));
    }

    public static void addRareTrade(VillagerTrades.ItemListing trade) {
        rareTrades.add(trade);
    }

    public static void addRareTrades(VillagerTrades.ItemListing... trades) {
        rareTrades.addAll(Arrays.asList(trades));
    }

    public static void registerTrades(WandererTradesEvent event) {
        event.getGenericTrades().addAll(commonTrades);
        event.getRareTrades().addAll(rareTrades);
    }
}
