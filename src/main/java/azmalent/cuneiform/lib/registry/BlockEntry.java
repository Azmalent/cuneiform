package azmalent.cuneiform.lib.registry;

import azmalent.cuneiform.common.item.CeilingOrFloorItem;
import azmalent.cuneiform.lib.config.options.BooleanOption;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockEntry implements IItemProvider {
    public final RegistryObject<Block> block;
    public final RegistryObject<Item> item;

    private BlockEntry() {
        block = null;
        item = null;
    }

    private BlockEntry(DeferredRegister<Block> blockRegistry, String id, Supplier<? extends Block> constructor) {
        block = blockRegistry.register(id, constructor);
        item = null;
    }

    private BlockEntry(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry,
                      String id, Supplier<? extends Block> constructor, ItemGroup creativeTab) {
        this(blockRegistry, itemRegistry, id, constructor, (block) ->
           new BlockItem(block, new Item.Properties().group(creativeTab))
        );
    }

    private BlockEntry(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry,
                      String id, Supplier<? extends Block> constructor, Function<Block, ? extends BlockItem> blockItemConstructor) {
        block = blockRegistry.register(id, constructor);
        item = itemRegistry.register(id, () -> blockItemConstructor.apply(block.get()));
    }

    public boolean hasItemForm() {
        return item != null;
    }

    public Block getBlock() {
        return block.get();
    }

    public BlockState getDefaultState() {
        return block.get().getDefaultState();
    }

    public ItemStack makeStack() {
        return makeStack(1);
    }

    public ItemStack makeStack(int amount) {
        return new ItemStack(asItem(), amount);
    }

    @Deprecated
    public Item getItem() {
        return asItem();
    }

    @Override
    @Nonnull
    public Item asItem() {
        if (item == null) {
            throw new NullPointerException(String.format("The block %s doesn't have an item form!", getBlock().getRegistryName()));
        }

        return item.get();
    }

    @SuppressWarnings("ConstantConditions")
    public static class Builder {
        protected String id;
        protected Supplier<? extends Block> constructor;
        protected Function<Block, ? extends BlockItem> blockItemConstructor;
        protected Consumer<BlockEntry> postInitCallback;
        protected boolean noItemForm = false;
        protected BlockRenderType renderType = BlockRenderType.SOLID;

        protected BlockRegistryHelper helper;

        public Builder(BlockRegistryHelper helper, String id, Supplier<? extends Block> constructor) {
            this.helper = helper;
            this.id = id;
            this.constructor = constructor;
        }

        public Builder(BlockRegistryHelper helper, String id, Function<Block.Properties, ? extends Block> constructor, Block.Properties properties) {
            this(helper, id, () -> constructor.apply(properties));
        }

        public Builder(BlockRegistryHelper helper, String id, Block.Properties properties) {
            this(helper, id, () -> new Block(properties));
        }

        public BlockEntry build() {
            BlockEntry entry;
            if (noItemForm) {
                entry = new BlockEntry(helper.blocks, id, constructor);
            }
            else if (blockItemConstructor != null) {
                entry = new BlockEntry(helper.blocks, helper.items, id, constructor, blockItemConstructor);
            }
            else {
                entry = new BlockEntry(helper.blocks, helper.items, id, constructor, helper.defaultTab);
            }

            if (postInitCallback != null) postInitCallback.accept(entry);

            if (renderType != BlockRenderType.SOLID) {
                helper.setRenderType(entry, renderType);
            }

            return entry;
        }

        @Deprecated
        public final Optional<BlockEntry> buildIf(boolean condition) {
            return condition ? Optional.of(build()) : Optional.empty();
        }

        @Deprecated
        public final Optional<BlockEntry> buildIf(BooleanOption condition) {
            return buildIf(condition.get());
        }

        public Builder withBlockItem(Function<Block, ? extends BlockItem> blockItemConstructor) {
            this.blockItemConstructor = blockItemConstructor;
            return this;
        }

        public Builder withBlockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor, Item.Properties properties) {
            this.blockItemConstructor = block -> blockItemConstructor.apply(block, properties);
            return this;
        }

        public Builder withBlockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor, ItemGroup group) {
            return this.withBlockItem(blockItemConstructor, new Item.Properties().group(group));
        }

        public Builder withTallBlockItem(Item.Properties properties) {
            return this.withBlockItem(TallBlockItem::new, properties);
        }

        public Builder withTallBlockItem(ItemGroup group) {
            return this.withBlockItem(TallBlockItem::new, group);
        }

        public Builder withWallOrFloorItem(BlockEntry wallBlock, Item.Properties properties) {
            return this.withBlockItem(block -> new WallOrFloorItem(wallBlock.getBlock(), block, properties));
        }

        public Builder withWallOrFloorItem(BlockEntry wallBlock, ItemGroup group) {
            return this.withWallOrFloorItem(wallBlock, new Item.Properties().group(group));
        }

        public Builder withCeilingOrFloorItem(BlockEntry floorBlock, Item.Properties properties) {
            return this.withBlockItem(block -> new CeilingOrFloorItem(floorBlock.getBlock(), block, properties));
        }

        public Builder withCeilingOrFloorItem(BlockEntry floorBlock, ItemGroup group) {
            return this.withCeilingOrFloorItem(floorBlock, new Item.Properties().group(group));
        }

        @Deprecated
        public Builder withBlockItemProperties(Item.Properties properties) {
            return this.withItemProperties(properties);
        }

        public Builder withItemProperties(Item.Properties properties) {
            return this.withBlockItem(BlockItem::new, properties);
        }

        public Builder withItemGroup(ItemGroup group) {
            return this.withItemProperties(new Item.Properties().group(group));
        }

        public Builder withoutItemForm() {
            this.noItemForm = true;
            return this;
        }

        public Builder withRenderType(BlockRenderType type) {
            this.renderType = type;
            return this;
        }

        public Builder cutoutRender() {
            return this.withRenderType(BlockRenderType.CUTOUT);
        }

        public Builder cutoutMippedRender() {
            return this.withRenderType(BlockRenderType.CUTOUT_MIPPED);
        }

        public Builder transculentRender() {
            return this.withRenderType(BlockRenderType.TRANSCULENT);
        }

        public Builder onInit(Consumer<BlockEntry> callback) {
            postInitCallback = callback;
            return this;
        }
    }
}

