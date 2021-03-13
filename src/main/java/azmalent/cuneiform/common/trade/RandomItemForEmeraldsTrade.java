package azmalent.cuneiform.common.trade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class RandomItemForEmeraldsTrade implements VillagerTrades.ITrade {
    private final ItemStack price;
    private final IItemProvider[] forSale;
    private final int maxTrades;
    private final int xp;
    private final float priceMult;

    public RandomItemForEmeraldsTrade(int price, IItemProvider[] forSale, int maxTrades, int xp, float priceMult) {
        this.price = new ItemStack(Items.EMERALD, price);
        this.forSale = forSale;
        this.maxTrades = maxTrades;
        this.xp = xp;
        this.priceMult = priceMult;
    }

    public RandomItemForEmeraldsTrade(int price, IItemProvider[] forSale, int maxTrades, int xp) {
        this(price, forSale, maxTrades, xp, 1);
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
        IItemProvider itemForSale = forSale[rand.nextInt(forSale.length)];
        return new MerchantOffer(price, new ItemStack(itemForSale), maxTrades, xp, priceMult);
    }
}
