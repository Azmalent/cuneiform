package azmalent.cuneiform.lib.config.options.lazy;

import azmalent.cuneiform.lib.config.options.AbstractConfigOption;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;

public final class RegistryOption<T extends IForgeRegistryEntry<T>> extends LazyOption<T> {
    public RegistryOption(IForgeRegistry<T> registry, String defaultNamespace, String defaultId) {
        this(registry, defaultNamespace + ":" + defaultId);
    }

    public RegistryOption(IForgeRegistry<T> registry, String defaultValue) {
        super(defaultValue, id -> {
            ResourceLocation key = new ResourceLocation(id);
            if (registry.containsKey(key)) {
                return registry.getValue(key);
            }

            ResourceLocation defaultKey = new ResourceLocation(defaultValue);
            return registry.containsKey(defaultKey) ? registry.getValue(defaultKey) : null;
        });
    }
}
