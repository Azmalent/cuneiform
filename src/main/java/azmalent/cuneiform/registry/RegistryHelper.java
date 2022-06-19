package azmalent.cuneiform.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class RegistryHelper {
    public final String modid;
    public final CreativeModeTab defaultTab;
    private final Map<ResourceKey<?>, DeferredRegister<?>> registries = Maps.newHashMap();

    private Queue<Pair<BlockEntry<?>, BlockRenderType>> renderTypes = Lists.newLinkedList();
    private Queue<Pair<EntityEntry<? extends LivingEntity>, Supplier<AttributeSupplier>>> attributeSuppliers = Lists.newLinkedList();

    public RegistryHelper(String modid) {
        this(modid, CreativeModeTab.TAB_MISC);
    }

    public RegistryHelper(String modid, CreativeModeTab defaultTab) {
        this.modid = modid;
        this.defaultTab = defaultTab;

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onAttributeCreation);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener((FMLClientSetupEvent event) -> {
                event.enqueueWork(this::initRenderTypes);
            });
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends IForgeRegistryEntry<T>> DeferredRegister<T> getOrCreateRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        if (!registries.containsKey(registryKey)) {
            registries.put(registryKey, DeferredRegister.create(registryKey, modid));
        }

        return (DeferredRegister<T>) registries.get(registryKey);
    }

    public <T extends IForgeRegistryEntry<T>> DeferredRegister<T> getOrCreateRegistry(IForgeRegistry<T> registry) {
        return getOrCreateRegistry(registry.getRegistryKey());
    }

    //Blocks
    public <T extends Block> BlockEntry.Builder<T> createBlock(String id, Supplier<T> constructor) {
        return new BlockEntry.Builder<T>(this, id, constructor);
    }

    public <T extends Block> BlockEntry.Builder<T> createBlock(String id, Function<Block.Properties, T> constructor, Block.Properties properties) {
        return new BlockEntry.Builder<T>(this, id, constructor, properties);
    }

    public BlockEntry.Builder<Block> createBlock(String id, Block.Properties properties) {
        return new BlockEntry.Builder<Block>(this, id, () -> new Block(properties));
    }

    //Items
    public <T extends Item> ItemEntry<T> createItem(String id, Supplier<T> constructor) {
        return new ItemEntry<T>(this, id, constructor);
    }

    public <T extends Item> ItemEntry<T> createItem(String id, Function<Item.Properties, T> constructor, Item.Properties props) {
        return new ItemEntry<T>(this, id, () -> constructor.apply(props));
    }

    public ItemEntry<Item> createItem(String id, Item.Properties props) {
        return new ItemEntry<Item>(this, id, () -> new Item(props));
    }

    public ItemEntry<Item> createItem(String id, CreativeModeTab tab) {
        return createItem(id, new Item.Properties().tab(tab));
    }

    public ItemEntry<Item> createItem(String id) {
        return createItem(id, defaultTab);
    }

    public ItemEntry<Item> createFood(String id, FoodProperties props) {
        return createItem(id, new Item.Properties().food(props).tab(CreativeModeTab.TAB_FOOD));
    }

    public <T extends Mob> ItemEntry<ForgeSpawnEggItem> createSpawnEgg(String entityId, Supplier<EntityType<T>> entityType, int primaryColor, int secondaryColor) {
        return createItem(entityId + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    }

    //Block Entities
    @SafeVarargs
    public final <T extends BlockEntity> BlockEntityEntry<T> createBlockEntity(String id, BlockEntitySupplier<T> constructor, Supplier<? extends Block>... blockSuppliers) {
        return new BlockEntityEntry<T>(this, id, constructor, blockSuppliers);
    }

    public final <T extends BlockEntity> BlockEntityEntry<T> createBlockEntity(String id, BlockEntitySupplier<T> constructor, List<Supplier<? extends Block>> blockSuppliers) {
        return new BlockEntityEntry<T>(this, id, constructor, blockSuppliers);
    }

    //Entities
    public final <T extends Entity> EntityEntry<T> createEntity(String id, EntityType.Builder<T> builder) {
        return new EntityEntry<T>(this, id, builder);
    }

    public final <T extends Mob> MobEntry.Builder<T> createMob(String id, EntityType.Builder<T> builder) {
        return new MobEntry.Builder<T>(this, id, builder);
    }

    public final <T extends Mob> void setEntityAttributes(EntityEntry<? extends LivingEntity> entity, Supplier<AttributeSupplier> supplier) {
        if (attributeSuppliers == null) {
            throw new IllegalStateException("Entity attribures already initialized");
        }

        attributeSuppliers.add(Pair.of(entity, supplier));
    }

    public final void onAttributeCreation(EntityAttributeCreationEvent event) {
        while(!attributeSuppliers.isEmpty()) {
            var pair = attributeSuppliers.poll();
            event.put(pair.getLeft().get(), pair.getRight().get());
        }

        attributeSuppliers = null;
    }

    //Render types
    public final void setBlockRenderType(BlockEntry<?> blockEntry, BlockRenderType renderType) {
        if (renderTypes == null) {
            throw new IllegalStateException("Render types already initialized");
        }

        renderTypes.add(Pair.of(blockEntry, renderType));
    }

    @OnlyIn(Dist.CLIENT)
    public final void initRenderTypes() {
        while(!renderTypes.isEmpty()) {
            var pair = renderTypes.poll();
            ItemBlockRenderTypes.setRenderLayer(pair.getLeft().get(), pair.getRight().get());
        }

        renderTypes = null;
    }

    public enum BlockRenderType {
        SOLID, CUTOUT, CUTOUT_MIPPED, TRANSCULENT;

        @OnlyIn(Dist.CLIENT)
        public RenderType get() {
            return switch (this) {
                case SOLID -> RenderType.solid();
                case CUTOUT -> RenderType.cutout();
                case CUTOUT_MIPPED -> RenderType.cutoutMipped();
                case TRANSCULENT -> RenderType.translucent();
            };
        }
    }
}
