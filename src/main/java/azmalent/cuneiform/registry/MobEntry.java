package azmalent.cuneiform.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements.SpawnPredicate;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MobEntry<T extends Mob> extends EntityEntry<T> {
    public final ItemEntry<ForgeSpawnEggItem> SPAWN_EGG;

    protected MobEntry(RegistryObject<EntityType<T>> type, ItemEntry<ForgeSpawnEggItem> spawnEgg) {
        super(type);
        SPAWN_EGG = spawnEgg;
    }

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

        public Builder<T> withSpawnEgg(int primaryColor, int secondaryColor) {
            this.spawnEgg = true;
            this.primaryEggColor = primaryColor;
            this.secondaryEggColor = secondaryColor;
            return this;
        }

        public Builder<T> withAttributes(Supplier<AttributeSupplier> attributeSupplier) {
            this.attributeSupplier = attributeSupplier;
            return this;
        }

        public MobEntry<T> build() {
            var registry = helper.getOrCreateRegistry(ForgeRegistries.ENTITIES);
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
