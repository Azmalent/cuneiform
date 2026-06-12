package azmalent.cuneiform.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Represents a registered item, wrapping a {@link RegistryObject}.
 *
 * @param <T> the item type
 */
public class ItemEntry<T extends Item> implements Supplier<T>, ItemLike {
    private final RegistryObject<T> item;

    /**
     * Creates and registers an item entry.
     *
     * @param registryHelper the registry helper
     * @param id the item's registry ID path
     * @param constructor the item constructor
     */
    public ItemEntry(RegistryHelper registryHelper, String id, Supplier<T> constructor) {
        item = registryHelper.getRegister(ForgeRegistries.ITEMS).register(id, constructor);
    }

    @Override
    public T get() {
        return item.get();
    }

    @Override
    @Nonnull
    public Item asItem() {
        return item.get();
    }

    /**
     * Creates an item stack of this item.
     */
    public ItemStack makeStack() {
        return makeStack(1);
    }

    /**
     * Creates an item stack of this item with the given amount.
     */
    public ItemStack makeStack(int amount) {
        return new ItemStack(asItem(), amount);
    }
}
