package azmalent.cuneiform.util;

import azmalent.cuneiform.common.trade.RandomItemSaleListing;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import org.jetbrains.annotations.NotNull;

/**
 * Fluent builder for creating {@link VillagerTrades.ItemListing} instances.
 *
 * <p>Provides factory methods for common trade patterns:</p>
 * <ul>
 *   <li>{@link #sell} — player buys an item with emeralds</li>
 *   <li>{@link #buy} — player sells an item for emeralds</li>
 *   <li>{@link #trade} — barter between two items</li>
 * </ul>
 *
 * <p>When multiple outputs are provided, the resulting listing will randomly
 * select one output per offer via {@link RandomItemSaleListing}.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * var trade = TradeBuilder.sell(3, new ItemStack(Items.DIAMOND))
 *     .maxTrades(5)
 *     .xp(10)
 *     .build();
 * }</pre>
 */
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

    /**
     * Creates a trade where the player buys one item with one emerald.
     *
     * @param output the item the player receives
     * @return a new trade builder
     */
    public static TradeBuilder sell(@NotNull ItemStack output) {
        return sell(1, output);
    }

    /**
     * Creates a trade where the player buys one item with the specified price in emeralds.
     *
     * @param price the number of emeralds required
     * @param output the item the player receives
     * @return a new trade builder
     */
    public static TradeBuilder sell(int price, @NotNull ItemStack output) {
        return trade(new ItemStack(Items.EMERALD, price), output);
    }

    /**
     * Creates a trade where the player buys one item with emeralds and a secondary input.
     *
     * @param price the number of emeralds required
     * @param input2 the secondary input item
     * @param output the item the player receives
     * @return a new trade builder
     */
    public static TradeBuilder sell(int price, @NotNull ItemStack input2, @NotNull ItemStack output) {
        return trade(new ItemStack(Items.EMERALD, price), input2, output);
    }

    /**
     * Creates a trade where the player buys one of several random items with emeralds.
     *
     * @param price the number of emeralds required
     * @param outputs the possible items the player may receive
     * @return a new trade builder
     */
    public static TradeBuilder sellRandom(int price, @NotNull ItemStack[] outputs) {
        if (outputs.length == 1) {
            return sell(price, outputs[0]);
        }

        return new TradeBuilder(new ItemStack(Items.EMERALD, price), ItemStack.EMPTY, outputs);
    }

    /**
     * Creates a trade where the player sells an item for one emerald.
     *
     * @param input the item the player must provide
     * @return a new trade builder
     */
    public static TradeBuilder buy(@NotNull ItemStack input) {
        return buy(input, 1);
    }

    /**
     * Creates a trade where the player sells an item for the specified price in emeralds.
     *
     * @param input the item the player must provide
     * @param price the number of emeralds the player receives
     * @return a new trade builder
     */
    public static TradeBuilder buy(@NotNull ItemStack input, int price) {
        return trade(input, new ItemStack(Items.EMERALD, price));
    }

    /**
     * Creates a barter trade between two items.
     *
     * @param input the item the player must provide
     * @param output the item the player receives
     * @return a new trade builder
     */
    public static TradeBuilder trade(@NotNull ItemStack input, @NotNull ItemStack output) {
        return new TradeBuilder(input, ItemStack.EMPTY, output);
    }

    /**
     * Creates a barter trade with a primary and secondary input.
     *
     * @param input the primary input item
     * @param input2 the secondary input item
     * @param output the item the player receives
     * @return a new trade builder
     */
    public static TradeBuilder trade(@NotNull ItemStack input, @NotNull ItemStack input2, @NotNull ItemStack output) {
        return new TradeBuilder(input, input2, output);
    }

    /**
     * Sets the maximum number of times this trade can be used before it expires.
     *
     * @param maxTrades the maximum uses
     * @return this builder
     */
    public TradeBuilder maxTrades(int maxTrades) {
        this.maxUses = maxTrades;
        return this;
    }

    /**
     * Sets the experience reward the villager receives when this trade is completed.
     *
     * @param xp the experience amount
     * @return this builder
     */
    public TradeBuilder xp(int xp) {
        this.villagerXp = xp;
        return this;
    }

    /**
     * Sets the price multiplier affecting demand-based price adjustments.
     *
     * @param priceMultiplier the price multiplier
     * @return this builder
     */
    public TradeBuilder priceMultiplier(float priceMultiplier) {
        this.priceMult = priceMultiplier;
        return this;
    }

    /**
     * Builds the {@link VillagerTrades.ItemListing}.
     *
     * <p>If multiple outputs were provided, returns a {@link RandomItemSaleListing};
     * otherwise returns a {@link BasicItemListing}.</p>
     *
     * @return the constructed trade listing
     */
    public VillagerTrades.ItemListing build() {
        if (outputs.length > 1) {
            return new RandomItemSaleListing(input, input2, outputs, maxUses, villagerXp, priceMult);
        } else {
            return new BasicItemListing(input, input2, outputs[0], maxUses, villagerXp, priceMult);
        }
    }
}
