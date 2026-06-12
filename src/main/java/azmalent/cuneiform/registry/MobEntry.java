package azmalent.cuneiform.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Represents a registered mob entity, extending {@link EntityEntry} with
 * optional spawn egg support.
 *
 * @param <T> the mob type
 */
public class MobEntry<T extends Mob> extends EntityEntry<T> {
    public final ItemEntry<ForgeSpawnEggItem> SPAWN_EGG;

    protected MobEntry(RegistryObject<EntityType<T>> type, ItemEntry<ForgeSpawnEggItem> spawnEgg) {
        super(type);
        SPAWN_EGG = spawnEgg;
    }

    /**
     * Builder for configuring a {@link MobEntry} before registration.
     *
     * @param <T> the mob type
     */
    public static class Builder<T extends Mob> {
        private final RegistryHelper helper;
        private final String id;
        private final EntityType.Builder<T> typeBuilder;

        boolean spawnEgg = false;
        int primaryEggColor;
        int secondaryEggColor;

        protected Supplier<AttributeSupplier> attributeSupplier = null;

        public Builder(RegistryHelper helper, String id, EntityType.Builder<T> typeBuilder) {
            this.helper = helper;
            this.id = id;
            this.typeBuilder = typeBuilder;
        }

        /**
         * Enables spawn egg registration with the given colors.
         *
         * @param primaryColor the primary egg color (RGB)
         * @param secondaryColor the secondary egg color (RGB)
         * @return this builder
         */
        public Builder<T> withSpawnEgg(int primaryColor, int secondaryColor) {
            this.spawnEgg = true;
            this.primaryEggColor = primaryColor;
            this.secondaryEggColor = secondaryColor;
            return this;
        }

        /**
         * Sets the attribute supplier for this mob.
         *
         * @param attributeSupplier the attribute supplier
         * @return this builder
         */
        public Builder<T> withAttributes(Supplier<AttributeSupplier> attributeSupplier) {
            this.attributeSupplier = attributeSupplier;
            return this;
        }

        /**
         * Builds and registers the mob entry.
         *
         * @return the registered mob entry
         */
        public MobEntry<T> build() {
            var registry = helper.getRegister(ForgeRegistries.ENTITY_TYPES);
            var type = registry.register(id, () -> typeBuilder.build(new ResourceLocation(helper.modid, id).toString()));
            var egg = spawnEgg ? helper.createSpawnEgg(id, type, primaryEggColor, secondaryEggColor) : null;

            var mob = new MobEntry<T>(type, egg);

            if (attributeSupplier != null) {
                helper.setEntityAttributes(mob, attributeSupplier);
            }

            return mob;
        }
    }
}
