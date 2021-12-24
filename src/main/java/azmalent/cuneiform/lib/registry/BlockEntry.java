package azmalent.cuneiform.lib.registry;

import azmalent.cuneiform.common.item.CeilingOrFloorItem;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockEntry implements ItemLike {
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
                      String id, Supplier<? extends Block> constructor, CreativeModeTab creativeTab) {
        this(blockRegistry, itemRegistry, id, constructor, (block) ->
           new BlockItem(block, new Item.Properties().tab(creativeTab))
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
        return block.get().defaultBlockState();
    }

    public ItemStack makeStack() {
        return makeStack(1);
    }

    public ItemStack makeStack(int amount) {
        return new ItemStack(asItem(), amount);
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

        public Builder withBlockItem(Function<Block, ? extends BlockItem> blockItemConstructor) {
            this.blockItemConstructor = blockItemConstructor;
            return this;
        }

        public Builder withBlockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor, Item.Properties properties) {
            this.blockItemConstructor = block -> blockItemConstructor.apply(block, properties);
            return this;
        }

        public Builder withBlockItem(BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor, CreativeModeTab group) {
            return this.withBlockItem(blockItemConstructor, new Item.Properties().tab(group));
        }

        public Builder withTallBlockItem(Item.Properties properties) {
            return this.withBlockItem(DoubleHighBlockItem::new, properties);
        }

        public Builder withTallBlockItem(CreativeModeTab group) {
            return this.withBlockItem(DoubleHighBlockItem::new, group);
        }

        public Builder withWallOrFloorItem(BlockEntry wallBlock, Item.Properties properties) {
            return this.withBlockItem(block -> new StandingAndWallBlockItem(wallBlock.getBlock(), block, properties));
        }

        public Builder withWallOrFloorItem(BlockEntry wallBlock, CreativeModeTab group) {
            return this.withWallOrFloorItem(wallBlock, new Item.Properties().tab(group));
        }

        public Builder withCeilingOrFloorItem(BlockEntry floorBlock, Item.Properties properties) {
            return this.withBlockItem(block -> new CeilingOrFloorItem(floorBlock.getBlock(), block, properties));
        }

        public Builder withCeilingOrFloorItem(BlockEntry floorBlock, CreativeModeTab group) {
            return this.withCeilingOrFloorItem(floorBlock, new Item.Properties().tab(group));
        }

        public Builder withItemProperties(Item.Properties properties) {
            return this.withBlockItem(BlockItem::new, properties);
        }

        public Builder inCreativeTab(CreativeModeTab group) {
            return this.withItemProperties(new Item.Properties().tab(group));
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

