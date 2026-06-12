package azmalent.cuneiform.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Central helper for registering game objects (blocks, items, entities, block entities,
 * mobs) via Forge's {@link DeferredRegister} system.
 *
 * <p>Provides fluent factory methods that create {@link BlockEntry}, {@link ItemEntry},
 * {@link EntityEntry}, {@link MobEntry}, and {@link BlockEntityEntry} wrappers around
 * {@link net.minecraftforge.registries.RegistryObject} instances.</p>
 *
 * <p>Entity attributes are queued during registration and applied during
 * {@link EntityAttributeCreationEvent} to ensure proper ordering.</p>
 */
public class RegistryHelper {
    public final String modid;
    public final CreativeModeTab defaultTab;
    private final Map<ResourceKey<?>, DeferredRegister<?>> deferredRegisters = Maps.newHashMap();

    private Queue<Pair<EntityEntry<? extends LivingEntity>, Supplier<AttributeSupplier>>> attributeSuppliers = Lists.newLinkedList();

    /**
     * Creates a new registry helper and registers the attribute creation listener.
     *
     * @param context the FML mod loading context
     * @param modid the mod ID to register objects under
     * @param defaultTab the default creative tab for items
     */
    public RegistryHelper(FMLJavaModLoadingContext context, String modid, CreativeModeTab defaultTab) {
        this.modid = modid;
        this.defaultTab = defaultTab;

        IEventBus bus = context.getModEventBus();
        bus.addListener(this::onAttributeCreation);
    }

    @SuppressWarnings("unchecked")
    public <T> DeferredRegister<T> getRegister(ResourceKey<? extends Registry<T>> registryKey) {
        if (!deferredRegisters.containsKey(registryKey)) {
            deferredRegisters.put(registryKey, DeferredRegister.create(registryKey, modid));
        }

        return (DeferredRegister<T>) deferredRegisters.get(registryKey);
    }

    /**
     * Gets or creates a {@link DeferredRegister} for the given Forge registry.
     *
     * @param registry the Forge registry
     * @param <T> the registry type
     * @return the deferred register for this mod
     */
    public <T> DeferredRegister<T> getRegister(IForgeRegistry<T> registry) {
        return getRegister(registry.getRegistryKey());
    }

    //Blocks

    /**
     * Creates a block entry builder for a block constructed via supplier.
     *
     * @param id the block's registry ID path
     * @param constructor the block constructor
     * @param <T> the block type
     * @return a block entry builder
     */
    public <T extends Block> BlockEntry.Builder<T> createBlock(String id, Supplier<T> constructor) {
        return new BlockEntry.Builder<T>(this, id, constructor);
    }

    /**
     * Creates a block entry builder for a block constructed from {@link Block.Properties}.
     *
     * @param id the block's registry ID path
     * @param constructor the block constructor taking properties
     * @param properties the block properties
     * @param <T> the block type
     * @return a block entry builder
     */
    public <T extends Block> BlockEntry.Builder<T> createBlock(String id, Function<Block.Properties, T> constructor, Block.Properties properties) {
        return new BlockEntry.Builder<T>(this, id, constructor, properties);
    }

    /**
     * Creates a block entry builder for a basic {@link Block}.
     *
     * @param id the block's registry ID path
     * @param properties the block properties
     * @return a block entry builder
     */
    public BlockEntry.Builder<Block> createBlock(String id, Block.Properties properties) {
        return new BlockEntry.Builder<Block>(this, id, () -> new Block(properties));
    }

    //Items

    /**
     * Creates an item entry for an item constructed via supplier.
     *
     * @param id the item's registry ID path
     * @param constructor the item constructor
     * @param <T> the item type
     * @return the item entry
     */
    public <T extends Item> ItemEntry<T> createItem(String id, Supplier<T> constructor) {
        return new ItemEntry<T>(this, id, constructor);
    }

    /**
     * Creates an item entry for an item constructed from {@link Item.Properties}.
     *
     * @param id the item's registry ID path
     * @param constructor the item constructor taking properties
     * @param props the item properties
     * @param <T> the item type
     * @return the item entry
     */
    public <T extends Item> ItemEntry<T> createItem(String id, Function<Item.Properties, T> constructor, Item.Properties props) {
        return new ItemEntry<T>(this, id, () -> constructor.apply(props));
    }

    /**
     * Creates an item entry for a basic {@link Item}.
     *
     * @param id the item's registry ID path
     * @param props the item properties
     * @return the item entry
     */
    public ItemEntry<Item> createItem(String id, Item.Properties props) {
        return new ItemEntry<Item>(this, id, () -> new Item(props));
    }

    /**
     * Creates an item entry for a basic item in the given creative tab.
     *
     * @param id the item's registry ID path
     * @param tab the creative tab
     * @return the item entry
     */
    public ItemEntry<Item> createItem(String id, CreativeModeTab tab) {
        return createItem(id, new Item.Properties());
    }

    /**
     * Creates an item entry for a basic item in the default creative tab.
     *
     * @param id the item's registry ID path
     * @return the item entry
     */
    public ItemEntry<Item> createItem(String id) {
        return createItem(id, defaultTab);
    }

    /**
     * Creates a food item entry with the given food properties.
     *
     * @param id the item's registry ID path
     * @param props the food properties
     * @return the food item entry
     */
    public ItemEntry<Item> createFood(String id, FoodProperties props) {
        return createItem(id, new Item.Properties().food(props));
    }

    /**
     * Creates a spawn egg item entry for the given entity type.
     *
     * @param entityId the entity's registry ID path (without the "_spawn_egg" suffix)
     * @param entityType the entity type supplier
     * @param primaryColor the primary egg color (RGB)
     * @param secondaryColor the secondary egg color (RGB)
     * @param <T> the mob type
     * @return the spawn egg item entry
     */
    public <T extends Mob> ItemEntry<ForgeSpawnEggItem> createSpawnEgg(String entityId, Supplier<EntityType<T>> entityType, int primaryColor, int secondaryColor) {
        return createItem(entityId + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Properties()));
    }

    //Block Entities

    /**
     * Creates a block entity entry.
     *
     * @param id the block entity's registry ID path
     * @param constructor the block entity constructor
     * @param blockSuppliers the blocks this block entity is associated with
     * @param <T> the block entity type
     * @return the block entity entry
     */
    @SafeVarargs
    public final <T extends BlockEntity> BlockEntityEntry<T> createBlockEntity(String id, BlockEntitySupplier<T> constructor, Supplier<? extends Block>... blockSuppliers) {
        return new BlockEntityEntry<T>(this, id, constructor, blockSuppliers);
    }

    /**
     * Creates a block entity entry with a list of associated blocks.
     *
     * @param id the block entity's registry ID path
     * @param constructor the block entity constructor
     * @param blockSuppliers the blocks this block entity is associated with
     * @param <T> the block entity type
     * @return the block entity entry
     */
    public final <T extends BlockEntity> BlockEntityEntry<T> createBlockEntity(String id, BlockEntitySupplier<T> constructor, List<Supplier<? extends Block>> blockSuppliers) {
        return new BlockEntityEntry<T>(this, id, constructor, blockSuppliers);
    }

    //Entities

    /**
     * Creates an entity entry.
     *
     * @param id the entity's registry ID path
     * @param builder the entity type builder
     * @param <T> the entity type
     * @return the entity entry
     */
    public final <T extends Entity> EntityEntry<T> createEntity(String id, EntityType.Builder<T> builder) {
        return new EntityEntry<T>(this, id, builder);
    }

    /**
     * Creates a mob entry builder.
     *
     * @param id the mob's registry ID path
     * @param builder the entity type builder
     * @param <T> the mob type
     * @return a mob entry builder
     */
    public final <T extends Mob> MobEntry.Builder<T> createMob(String id, EntityType.Builder<T> builder) {
        return new MobEntry.Builder<T>(this, id, builder);
    }

    /**
     * Queues attribute registration for the given entity. The attributes will
     * be applied during {@link EntityAttributeCreationEvent}.
     *
     * @param entity the entity entry
     * @param supplier the attribute supplier
     * @throws IllegalStateException if attributes have already been initialized
     */
    public final <T extends Mob> void setEntityAttributes(EntityEntry<? extends LivingEntity> entity, Supplier<AttributeSupplier> supplier) {
        if (attributeSuppliers == null) {
            throw new IllegalStateException("Entity attribures already initialized");
        }

        attributeSuppliers.add(Pair.of(entity, supplier));
    }

    /**
     * Applies all queued entity attributes during {@link EntityAttributeCreationEvent}.
     *
     * @param event the attribute creation event
     */
    public final void onAttributeCreation(EntityAttributeCreationEvent event) {
        while(!attributeSuppliers.isEmpty()) {
            var pair = attributeSuppliers.poll();
            event.put(pair.getLeft().get(), pair.getRight().get());
        }

        attributeSuppliers = null;
    }
}
