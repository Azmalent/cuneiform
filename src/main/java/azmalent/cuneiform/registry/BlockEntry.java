package azmalent.cuneiform.registry;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

/**
 * Represents a registered block, wrapping a {@link RegistryObject} and optionally
 * its associated {@link BlockItem}.
 *
 * <p>Use the inner {@link Builder} to configure the block's item form (or lack
 * thereof) before calling {@link Builder#build()}.</p>
 *
 * @param <T> the block type
 */
public class BlockEntry<T extends Block> implements Supplier<T>, ItemLike {
    public final RegistryObject<T> block;

    private BlockEntry(RegistryHelper registryHelper, String id, Supplier<T> constructor) {
        block = registryHelper.getRegister(BLOCKS).register(id, constructor);
    }

    private BlockEntry(RegistryHelper registryHelper,String id, Supplier<T> constructor, CreativeModeTab creativeTab) {
        this(registryHelper, id, constructor, (block) ->
            new BlockItem(block, new Item.Properties())
        );
    }

    private BlockEntry(RegistryHelper registryHelper, String id, Supplier<T> constructor, Function<Block, ? extends BlockItem> itemConstructor) {
        block = registryHelper.getRegister(BLOCKS).register(id, constructor);
        registryHelper.getRegister(ITEMS).register(id, () -> itemConstructor.apply(block.get()));
    }

    /**
     * Whether this block has an associated item form.
     *
     * @return {@code true} if the block has a corresponding item
     */
    public boolean hasItemForm() {
        return block.get().asItem() != Items.AIR;
    }

    /**
     * Returns the default block state of this block.
     *
     * @return the default block state
     */
    public BlockState defaultBlockState() {
        return block.get().defaultBlockState();
    }

    /**
     * Creates an item stack of this block with the given amount.
     *
     * @param amount the stack size
     * @return a new item stack
     */
    public ItemStack makeStack() {
        return makeStack(1);
    }

    /**
     * Creates an item stack of this block with the given amount.
     *
     * @param amount the stack size
     * @return a new item stack
     */
    public ItemStack makeStack(int amount) {
        return new ItemStack(asItem(), amount);
    }

    @Override
    public T get() {
        return block.get();
    }

    @Override
    @Nonnull
    public Item asItem() {
        if (!hasItemForm()) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(this.get());
            throw new NullPointerException(String.format("The block %s doesn't have an item form!", id));
        }

        return block.get().asItem();
    }

    /**
     * Builder for configuring a {@link BlockEntry} before registration.
     *
     * <p>Call {@link #build()} to finalize and register the block.</p>
     *
     * @param <T> the block type
     */
    public static class Builder<T extends Block> {
        protected RegistryHelper helper;

        protected String id;
        protected Supplier<T> constructor;
        protected Function<Block, ? extends BlockItem> blockItemConstructor;
        protected boolean noItemForm = false;
        public Builder(RegistryHelper helper, String id, Supplier<T> constructor) {
            this.helper = helper;
            this.id = id;
            this.constructor = constructor;
        }

        public Builder(RegistryHelper helper, String id, Function<Block.Properties, T> constructor, Block.Properties properties) {
            this(helper, id, () -> constructor.apply(properties));
        }

        /**
         * Builds and registers the block entry.
         *
         * @return the registered block entry
         */
        public BlockEntry<T> build() {
            BlockEntry<T> entry;
            if (noItemForm) {
                entry = new BlockEntry<T>(helper, id, constructor);
            }
            else if (blockItemConstructor != null) {
                entry = new BlockEntry<T>(helper, id, constructor, blockItemConstructor);
            }
            else {
                entry = new BlockEntry<T>(helper, id, constructor, helper.defaultTab);
            }

            return entry;
        }

        //Item form

        /**
         * Sets a custom block item constructor.
         *
         * @param blockItemConstructor the constructor taking the block instance
         * @return this builder
         */
        public Builder<T> blockItem(Function<Block, ? extends BlockItem> blockItemConstructor) {
            this.blockItemConstructor = blockItemConstructor;
            return this;
        }

        /**
         * Sets a custom block item constructor with properties.
         *
         * @param blockItemConstructor the constructor taking block and properties
         * @param properties the item properties
         * @return this builder
         */
        public Builder<T> blockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor, Item.Properties properties) {
            this.blockItemConstructor = block -> blockItemConstructor.apply(block, properties);
            return this;
        }

        /**
         * Sets a custom block item constructor with default properties.
         *
         * @param blockItemConstructor the constructor taking block and properties
         * @return this builder
         */
        public Builder<T> blockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor) {
            return this.blockItem(blockItemConstructor, new Item.Properties());
        }

        /**
         * Sets the block item as a tall (double-high) block item.
         *
         * @param properties the item properties
         * @return this builder
         */
        public Builder<T> tallBlockItem(Item.Properties properties) {
            return this.blockItem(DoubleHighBlockItem::new, properties);
        }

        /**
         * Sets the block item as a tall (double-high) block item with default properties.
         *
         * @return this builder
         */
        public Builder<T> tallBlockItem() {
            return this.blockItem(DoubleHighBlockItem::new);
        }

        /**
         * Sets the block item as a standing and wall block item (for blocks with wall variants).
         *
         * @param wallBlock the wall block entry
         * @param properties the item properties
         * @return this builder
         */
        public Builder<T> wallOrFloorItem(BlockEntry<?> wallBlock, Item.Properties properties) {
            return this.blockItem(block -> new StandingAndWallBlockItem(block, wallBlock.get(), properties, Direction.DOWN));
        }

        /**
         * Sets the block item as a standing and wall block item with default properties.
         *
         * @param wallBlock the wall block entry
         * @return this builder
         */
        public Builder<T> wallOrFloorItem(BlockEntry<?> wallBlock) {
            return this.wallOrFloorItem(wallBlock, new Item.Properties());
        }

        /**
         * Sets a custom block item with the given properties.
         *
         * @param properties the item properties
         * @return this builder
         */
        public Builder<T> blockItem(Item.Properties properties) {
            return this.blockItem(BlockItem::new, properties);
        }

        /**
         * Sets a default block item with default properties.
         *
         * @return this builder
         */
        public Builder<T> blockItem() {
            return this.blockItem(new Item.Properties());
        }

        /**
         * Marks this block as having no item form.
         *
         * @return this builder
         */
        public Builder<T> noItemForm() {
            this.noItemForm = true;
            return this;
        }
    }
}
