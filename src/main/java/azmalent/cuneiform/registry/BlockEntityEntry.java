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

/**
 * Represents a registered block entity type, wrapping a {@link RegistryObject}.
 *
 * @param <T> the block entity type
 */
public class BlockEntityEntry<T extends BlockEntity> implements Supplier<BlockEntityType<T>> {
    public final RegistryObject<BlockEntityType<T>> type;

    /**
     * Creates and registers a block entity entry from varargs block suppliers.
     *
     * @param helper the registry helper
     * @param id the block entity's registry ID path
     * @param constructor the block entity constructor
     * @param blockSuppliers the blocks this block entity is associated with
     */
    @SafeVarargs
    @SuppressWarnings("ConstantConditions")
    public BlockEntityEntry(RegistryHelper helper, String id, BlockEntitySupplier<T> constructor, @Nonnull Supplier<? extends Block>... blockSuppliers) {
        this.type = helper.getRegister(ForgeRegistries.BLOCK_ENTITY_TYPES).register(id, () -> {
            Block[] blocks = Arrays.stream(blockSuppliers).map(Supplier::get).toArray(Block[]::new);
            return BlockEntityType.Builder.of(constructor, blocks).build(null);
        });;
    }

    /**
     * Creates and registers a block entity entry from a list of block suppliers.
     *
     * @param helper the registry helper
     * @param id the block entity's registry ID path
     * @param constructor the block entity constructor
     * @param blockSuppliers the blocks this block entity is associated with
     */
    @SuppressWarnings("ConstantConditions")
    public BlockEntityEntry(RegistryHelper helper, String id, BlockEntitySupplier<T> constructor, List<Supplier<? extends Block>> blockSuppliers) {
        this.type = helper.getRegister(ForgeRegistries.BLOCK_ENTITY_TYPES).register(id, () -> {
            var blocks = blockSuppliers.stream().map(Supplier::get).toArray(Block[]::new);
            return BlockEntityType.Builder.of(constructor, blocks).build(null);
        });;
    }

    @Override
    public BlockEntityType<T> get() {
        return type.get();
    }

    /**
     * Registers a block entity renderer for this type (client side only).
     *
     * @param renderer the block entity renderer provider
     */
    @OnlyIn(Dist.CLIENT)
    public void registerRenderer(BlockEntityRendererProvider<T> renderer) {
        BlockEntityRenderers.register(get(), renderer);
    }
}
