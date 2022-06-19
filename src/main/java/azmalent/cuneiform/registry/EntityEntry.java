package azmalent.cuneiform.registry;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class EntityEntry<T extends Entity> implements Supplier<EntityType<T>> {
    private final RegistryObject<EntityType<T>> TYPE;

    protected EntityEntry(RegistryObject<EntityType<T>> type) {
        TYPE = type;
    }

    public EntityEntry(RegistryHelper helper, String id, EntityType.Builder<T> builder) {
        var registry = helper.getOrCreateRegistry(ForgeRegistries.ENTITIES);
        TYPE = registry.register(id, () -> builder.build(new ResourceLocation(helper.modid, id).toString()));
    }

    @Override
    public EntityType<T> get() {
        return TYPE.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void registerRenderer(EntityRendererProvider<T> renderer) {
        EntityRenderers.register(get(), renderer);
    }
}
