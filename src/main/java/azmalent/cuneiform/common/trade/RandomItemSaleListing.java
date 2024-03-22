package azmalent.cuneiform.common.trade;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RandomItemSaleListing implements VillagerTrades.ItemListing {
    private final ItemStack input;
    private final ItemStack input2;
    private final ItemStack[] outputs;
    private final int maxTrades;
    private final int xp;
    private final float priceMult;

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

    public RandomItemSaleListing(ItemStack input, ItemStack input2, ItemStack[] outputs, int maxTrades, int xp) {
        this(input, input2, outputs, maxTrades, xp, 1);
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
        ItemStack itemForSale = Util.getRandom(outputs, rand);
        return new MerchantOffer(input, input2, itemForSale, maxTrades, xp, priceMult);
    }
}
