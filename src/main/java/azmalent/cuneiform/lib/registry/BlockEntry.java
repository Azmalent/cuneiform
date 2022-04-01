package azmalent.cuneiform.lib.registry;

import azmalent.cuneiform.lib.registry.RegistryHelper.BlockRenderType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.registries.ForgeRegistries.*;

@SuppressWarnings("unused")
public final class BlockEntry<T extends Block> implements Supplier<T>, ItemLike {
    public final RegistryObject<T> block;

    private BlockEntry(RegistryHelper registryHelper, String id, Supplier<T> constructor) {
        block = registryHelper.getOrCreateRegistry(BLOCKS).register(id, constructor);
    }

    private BlockEntry(RegistryHelper registryHelper,String id, Supplier<T> constructor, CreativeModeTab creativeTab) {
        this(registryHelper, id, constructor, (block) ->
           new BlockItem(block, new Item.Properties().tab(creativeTab))
        );
    }

    private BlockEntry(RegistryHelper registryHelper, String id, Supplier<T> constructor, Function<Block, ? extends BlockItem> itemConstructor) {
        block = registryHelper.getOrCreateRegistry(BLOCKS).register(id, constructor);
        registryHelper.getOrCreateRegistry(ITEMS).register(id, () -> itemConstructor.apply(block.get()));
    }

    public boolean hasItemForm() {
        return block.get().asItem() != Items.AIR;
    }

    public BlockState defaultBlockState() {
        return block.get().defaultBlockState();
    }

    public ItemStack makeStack() {
        return makeStack(1);
    }

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
            throw new NullPointerException(String.format("The block %s doesn't have an item form!", get().getRegistryName()));
        }

        return block.get().asItem();
    }

    @SuppressWarnings("ConstantConditions")
    public static class Builder<T extends Block> {
        protected RegistryHelper helper;

        protected String id;
        protected Supplier<T> constructor;
        protected Function<Block, ? extends BlockItem> blockItemConstructor;
        protected boolean noItemForm = false;

        protected BlockRenderType renderType = BlockRenderType.SOLID;

        public Builder(RegistryHelper helper, String id, Supplier<T> constructor) {
            this.helper = helper;
            this.id = id;
            this.constructor = constructor;
        }

        public Builder(RegistryHelper helper, String id, Function<Block.Properties, T> constructor, Block.Properties properties) {
            this(helper, id, () -> constructor.apply(properties));
        }

        public BlockEntry build() {
            BlockEntry entry;
            if (noItemForm) {
                entry = new BlockEntry(helper, id, constructor);
            }
            else if (blockItemConstructor != null) {
                entry = new BlockEntry(helper, id, constructor, blockItemConstructor);
            }
            else {
                entry = new BlockEntry(helper, id, constructor, helper.defaultTab);
            }

            if (renderType != BlockRenderType.SOLID) {
                helper.setBlockRenderType(entry, renderType);
            }

            return entry;
        }

        //Item form
        public Builder blockItem(Function<Block, ? extends BlockItem> blockItemConstructor) {
            this.blockItemConstructor = blockItemConstructor;
            return this;
        }

        public Builder blockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor, Item.Properties properties) {
            this.blockItemConstructor = block -> blockItemConstructor.apply(block, properties);
            return this;
        }

        public Builder blockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor, CreativeModeTab group) {
            return this.blockItem(blockItemConstructor, new Item.Properties().tab(group));
        }

        public Builder tallBlockItem(Item.Properties properties) {
            return this.blockItem(DoubleHighBlockItem::new, properties);
        }

        public Builder tallBlockItem(CreativeModeTab group) {
            return this.blockItem(DoubleHighBlockItem::new, group);
        }

        public Builder wallOrFloorItem(BlockEntry wallBlock, Item.Properties properties) {
            return this.blockItem(block -> new StandingAndWallBlockItem(wallBlock.get(), block, properties));
        }

        public Builder wallOrFloorItem(BlockEntry wallBlock, CreativeModeTab group) {
            return this.wallOrFloorItem(wallBlock, new Item.Properties().tab(group));
        }

        public Builder blockItem(Item.Properties properties) {
            return this.blockItem(BlockItem::new, properties);
        }

        public Builder blockItem(CreativeModeTab group) {
            return this.blockItem(new Item.Properties().tab(group));
        }

        public Builder noItemForm() {
            this.noItemForm = true;
            return this;
        }

        //Render types
        public Builder renderType(BlockRenderType type) {
            this.renderType = type;
            return this;
        }

        public Builder cutoutRender() {
            return this.renderType(BlockRenderType.CUTOUT);
        }

        public Builder cutoutMippedRender() {
            return this.renderType(BlockRenderType.CUTOUT_MIPPED);
        }

        public Builder transculentRender() {
            return this.renderType(BlockRenderType.TRANSCULENT);
        }
    }
}

