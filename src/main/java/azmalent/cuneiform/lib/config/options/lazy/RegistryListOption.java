package azmalent.cuneiform.lib.config.options.lazy;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public final class RegistryListOption<T extends IForgeRegistryEntry<T>> extends LazyListOption<T> {
    public RegistryListOption(IForgeRegistry<T> registry) {
        this(registry, Lists.newArrayList());
    }

    public RegistryListOption(IForgeRegistry<T> registry, List<String> defaultValue) {
        super(defaultValue, id -> {
            ResourceLocation key = new ResourceLocation(id);
            return registry.containsKey(key) ? registry.getValue(key) : null;
        });
    }
}
