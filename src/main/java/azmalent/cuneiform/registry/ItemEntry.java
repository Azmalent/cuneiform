package azmalent.cuneiform.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ItemEntry<T extends Item> implements Supplier<T>, ItemLike {
    private final RegistryObject<T> item;

    public ItemEntry(RegistryHelper registryHelper, String id, Supplier<T> constructor) {
        item = registryHelper.getOrCreateRegistry(ForgeRegistries.ITEMS).register(id, constructor);
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

    public ItemStack makeStack() {
        return makeStack(1);
    }

    public ItemStack makeStack(int amount) {
        return new ItemStack(asItem(), amount);
    }
}
