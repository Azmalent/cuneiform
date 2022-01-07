package azmalent.cuneiform.lib.util;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;

@SuppressWarnings("unused")
public final class TradeBuilder {
    private final ItemStack input;
    private final ItemStack input2;
    private final ItemStack output;
    private int maxUses = 4;
    private int villagerXp = 1;
    private float priceMult = 0.15f;

    private TradeBuilder(ItemStack input, ItemStack output) {
        this(input, ItemStack.EMPTY, output);
    }

    private TradeBuilder(ItemStack input, ItemStack input2, ItemStack output) {
        this.input = input;
        this.input2 = input2;
        this.output = output;
    }

    public static TradeBuilder sell(ItemStack output) {
        return sell(1, output);
    }

    public static TradeBuilder sell(int price, ItemStack output) {
        return trade(new ItemStack(Items.EMERALD, price), output);
    }

    public static TradeBuilder sell(int price, ItemStack input2, ItemStack output) {
        return trade(new ItemStack(Items.EMERALD, price), input2, output);
    }

    public static TradeBuilder buy(ItemStack input) {
        return buy(input, 1);
    }

    public static TradeBuilder buy(ItemStack input, int price) {
        return trade(input, new ItemStack(Items.EMERALD, price));
    }

    public static TradeBuilder trade(ItemStack input, ItemStack output) {
        return new TradeBuilder(input, output);
    }

    public static TradeBuilder trade(ItemStack input, ItemStack input2, ItemStack output) {
        return new TradeBuilder(input, input2, output);
    }

    public TradeBuilder maxTrades(int maxTrades) {
        this.maxUses = maxTrades;
        return this;
    }

    public TradeBuilder xp(int xp) {
        this.villagerXp = xp;
        return this;
    }

    public TradeBuilder priceMultiplier(float priceMultiplier) {
        this.priceMult = priceMultiplier;
        return this;
    }

    public VillagerTrades.ItemListing build() {
        return new BasicItemListing(input, input2, output, maxUses, villagerXp, priceMult);
    }
}
