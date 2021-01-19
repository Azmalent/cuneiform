package azmalent.cuneiform.lib.registry;

import azmalent.cuneiform.lib.config.options.BooleanOption;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockEntry {
    public final RegistryObject<Block> block;
    public final RegistryObject<Item> item;

    private BlockEntry() {
        block = null;
        item = null;
    }

    public BlockEntry(DeferredRegister<Block> blockRegistry, String id, Supplier<? extends Block> constructor) {
        block = blockRegistry.register(id, constructor);
        item = null;
    }

    public BlockEntry(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry,
                      String id, Supplier<? extends Block> constructor, ItemGroup creativeTab) {
        this(blockRegistry, itemRegistry, id, constructor, (block) ->
           new BlockItem(block, new Item.Properties().group(creativeTab))
        );
    }

    public BlockEntry(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry,
                      String id, Supplier<? extends Block> constructor, Function<Block, ? extends BlockItem> blockItemConstructor) {
        block = blockRegistry.register(id, constructor);
        item = itemRegistry.register(id, () -> blockItemConstructor.apply(block.get()));
    }

    public boolean isRegistered() {
        return block != null;
    }

    public boolean hasItemForm() {
        return item != null;
    }

    public Block getBlock() {
        return block.get();
    }

    public Item getItem() {
        if (block != null && item == null) {
            throw new NullPointerException(String.format("The block %s doesn't have an item form!", getBlock().getRegistryName()));
        }

        return item.get();
    }

    public static class Builder {
        protected String id;
        protected Supplier<? extends Block> constructor;
        protected Function<Block, ? extends BlockItem> blockItemConstructor;
        protected Consumer<BlockEntry> postInitCallback;
        protected boolean noItemForm = false;

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
            return entry;
        }

        @Nullable
        public final BlockEntry buildIf(boolean condition) {
            if (condition) return build();
            return null;
        }

        @Nullable
        public final BlockEntry buildIf(BooleanOption condition) {
            if (condition.get()) return build();
            return null;
        }

        public Builder withBlockItem(Function<Block, ? extends BlockItem> blockItemConstructor) {
            this.blockItemConstructor = blockItemConstructor;
            return this;
        }

        public Builder withBlockItemProperties(Item.Properties properties) {
            blockItemConstructor = (block) -> new BlockItem(block, properties);
            return this;
        }

        public Builder withoutItemForm() {
            this.noItemForm = true;
            return this;
        }

        public Builder onInit(Consumer<BlockEntry> callback) {
            postInitCallback = callback;
            return this;
        }
    }
}

