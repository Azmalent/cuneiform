package azmalent.cuneiform.common.trade;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A villager trade listing that randomly selects an output from an array of
 * possible item stacks each time a {@link MerchantOffer} is generated.
 *
 * <p>Useful for trades where the villager sells one of several possible items
 * at random (e.g. a wandering trader selling random dyes).</p>
 */
public class RandomItemSaleListing implements VillagerTrades.ItemListing {
    private final ItemStack input;
    private final ItemStack input2;
    private final ItemStack[] outputs;
    private final int maxTrades;
    private final int xp;
    private final float priceMult;

    /**
     * Creates a new random item sale listing.
     *
     * @param input the primary input item stack
     * @param input2 the secondary input item stack (or {@link ItemStack#EMPTY} for none)
     * @param outputs the array of possible output item stacks (must not be empty)
     * @param maxTrades the maximum number of times this trade can be used
     * @param xp the experience reward for the villager
     * @param priceMult the price multiplier affecting demand-based price adjustments
     */
    public RandomItemSaleListing(ItemStack input, ItemStack input2, ItemStack[] outputs, int maxTrades, int xp, float priceMult) {
        if (outputs == null || outputs.length == 0) {
            throw new IllegalArgumentException("Trade outputs are not specified");
        }

        this.input = input;
        this.input2 = input2;
        this.outputs = outputs;
        this.maxTrades = maxTrades;
        this.xp = xp;
        this.priceMult = priceMult;
    }

    /**
     * Creates a new random item sale listing with a price multiplier of 1.
     *
     * @param input the primary input item stack
     * @param input2 the secondary input item stack (or {@link ItemStack#EMPTY} for none)
     * @param outputs the array of possible output item stacks (must not be empty)
     * @param maxTrades the maximum number of times this trade can be used
     * @param xp the experience reward for the villager
     */
    public RandomItemSaleListing(ItemStack input, ItemStack input2, ItemStack[] outputs, int maxTrades, int xp) {
        this(input, input2, outputs, maxTrades, xp, 1);
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
        var itemForSale = Util.getRandom(outputs, rand);
        return new MerchantOffer(input, input2, itemForSale, maxTrades, xp, priceMult);
    }
}
