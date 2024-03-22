package azmalent.cuneiform.registry;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class BlockEntityEntry<T extends BlockEntity> implements Supplier<BlockEntityType<T>> {
    public final RegistryObject<BlockEntityType<T>> type;

    @SafeVarargs
    @SuppressWarnings("ConstantConditions")
    public BlockEntityEntry(RegistryHelper helper, String id, BlockEntitySupplier<T> constructor, @Nonnull Supplier<? extends Block>... blockSuppliers) {
        this.type = helper.getRegister(ForgeRegistries.BLOCK_ENTITY_TYPES).register(id, () -> {
            Block[] blocks = Arrays.stream(blockSuppliers).map(Supplier::get).toArray(Block[]::new);
            return BlockEntityType.Builder.of(constructor, blocks).build(null);
        });;
    }

    @SuppressWarnings("ConstantConditions")
    public BlockEntityEntry(RegistryHelper helper, String id, BlockEntitySupplier<T> constructor, List<Supplier<? extends Block>> blockSuppliers) {
        this.type = helper.getRegister(ForgeRegistries.BLOCK_ENTITY_TYPES).register(id, () -> {
            Block[] blocks = blockSuppliers.stream().map(Supplier::get).toArray(Block[]::new);
            return BlockEntityType.Builder.of(constructor, blocks).build(null);
        });;
    }

    @Override
    public BlockEntityType<T> get() {
        return type.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void registerRenderer(BlockEntityRendererProvider<T> renderer) {
        BlockEntityRenderers.register(get(), renderer);
    }
}
