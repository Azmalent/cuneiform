package azmalent.cuneiform.util;

import azmalent.cuneiform.common.trade.RandomItemSaleListing;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import org.jetbrains.annotations.NotNull;

public final class TradeBuilder {
    private final ItemStack input;
    private final ItemStack input2;
    private final ItemStack[] outputs;
    private int maxUses = 4;
    private int villagerXp = 1;
    private float priceMult = 0.15f;

    private TradeBuilder(@NotNull ItemStack input, @NotNull ItemStack input2, @NotNull ItemStack output) {
        this(input, input2, new ItemStack[] { output });
    }

    private TradeBuilder(@NotNull ItemStack input, @NotNull ItemStack input2, @NotNull ItemStack[] outputs) {
        this.input = input;
        this.input2 = input2;
        this.outputs = outputs;
    }

    public static TradeBuilder sell(@NotNull ItemStack output) {
        return sell(1, output);
    }

    public static TradeBuilder sell(int price, @NotNull ItemStack output) {
        return trade(new ItemStack(Items.EMERALD, price), output);
    }

    public static TradeBuilder sell(int price, @NotNull ItemStack input2, @NotNull ItemStack output) {
        return trade(new ItemStack(Items.EMERALD, price), input2, output);
    }

    public static TradeBuilder sellRandom(int price, @NotNull ItemStack[] outputs) {
        if (outputs.length == 1) {
            return sell(price, outputs[0]);
        }

        return new TradeBuilder(new ItemStack(Items.EMERALD, price), ItemStack.EMPTY, outputs);
    }

    public static TradeBuilder buy(@NotNull ItemStack input) {
        return buy(input, 1);
    }

    public static TradeBuilder buy(@NotNull ItemStack input, int price) {
        return trade(input, new ItemStack(Items.EMERALD, price));
    }

    public static TradeBuilder trade(@NotNull ItemStack input, @NotNull ItemStack output) {
        return new TradeBuilder(input, ItemStack.EMPTY, output);
    }

    public static TradeBuilder trade(@NotNull ItemStack input, @NotNull ItemStack input2, @NotNull ItemStack output) {
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
        if (outputs.length > 1) {
            return new RandomItemSaleListing(input, input2, outputs, maxUses, villagerXp, priceMult);
        } else {
            return new BasicItemListing(input, input2, outputs[0], maxUses, villagerXp, priceMult);
        }
    }
}
