package azmalent.cuneiform.lib.registry;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class RegistryHelper {
    public final DeferredRegister<Block> blocks;
    public final DeferredRegister<Item> items;
    public final CreativeModeTab defaultTab;

    private final Map<BlockEntry, BlockRenderType> renderTypes = Maps.newHashMap();

    public RegistryHelper(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry, CreativeModeTab defaultCreativeTab) {
        blocks = blockRegistry;
        items = itemRegistry;
        defaultTab = defaultCreativeTab;

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            bus.<FMLClientSetupEvent>addListener(event -> event.enqueueWork(this::initRenderTypes));
        });
    }

    public BlockEntry.Builder createBlock(String id, Supplier<? extends Block> constructor) {
        return new BlockEntry.Builder(this, id, constructor);
    }

    public BlockEntry.Builder createBlock(String id, Function<Block.Properties, ? extends Block> constructor, Block.Properties properties) {
        return new BlockEntry.Builder(this, id, constructor, properties);
    }

    public BlockEntry.Builder createBlock(String id, Block.Properties properties) {
        return new BlockEntry.Builder(this, id, properties);
    }

    public void setBlockRenderType(BlockEntry blockEntry, BlockRenderType renderType) {
        renderTypes.put(blockEntry, renderType);
    }

    @OnlyIn(Dist.CLIENT)
    public void initRenderTypes() {
        for (Map.Entry<BlockEntry, BlockRenderType> entry : renderTypes.entrySet()) {
            BlockEntry blockEntry = entry.getKey();
            BlockRenderType renderType = entry.getValue();
            ItemBlockRenderTypes.setRenderLayer(blockEntry.getBlock(), renderType.get());
        }
    }
}
