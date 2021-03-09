package azmalent.cuneiform.lib.registry;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockRegistryHelper {
    public final DeferredRegister<Block> blocks;
    public final DeferredRegister<Item> items;
    public final ItemGroup defaultTab;

    private final Map<BlockEntry, BlockRenderType> renderTypes = Maps.newHashMap();

    public BlockRegistryHelper(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry, ItemGroup defaultCreativeTab) {
        blocks = blockRegistry;
        items = itemRegistry;
        defaultTab = defaultCreativeTab;
    }

    public BlockEntry.Builder newBuilder(String id, Supplier<? extends Block> constructor) {
        return new BlockEntry.Builder(this, id, constructor);
    }

    public BlockEntry.Builder newBuilder(String id, Function<Block.Properties, ? extends Block> constructor, Block.Properties properties) {
        return new BlockEntry.Builder(this, id, constructor, properties);
    }

    public BlockEntry.Builder newBuilder(String id, Block.Properties properties) {
        return new BlockEntry.Builder(this, id, properties);
    }

    public void setRenderType(BlockEntry blockEntry, BlockRenderType renderType) {
        renderTypes.put(blockEntry, renderType);
    }

    @OnlyIn(Dist.CLIENT)
    public void initRenderTypes() {
        for (Map.Entry<BlockEntry, BlockRenderType> entry : renderTypes.entrySet()) {
            BlockEntry blockEntry = entry.getKey();
            BlockRenderType renderType = entry.getValue();
            RenderTypeLookup.setRenderLayer(blockEntry.getBlock(), renderType.get());
        }
    }
}
