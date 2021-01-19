package azmalent.cuneiform.lib.config.options;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.util.Collection;

public final class IntOption extends AbstractNumericOption<Integer> {
    public IntOption(int defaultValue) {
        super(defaultValue);
    }

    public IntOption(int defaultValue, int min, int max) {
        super(defaultValue, min, max);
    }

    public IntOption(int defaultValue, Collection<Integer> allowedValues) {
        super(defaultValue, allowedValues);
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        if (rangeRestricted) {
            value = addComment(builder, field, "Default: " + defaultValue)
                    .defineInRange(getName(field), defaultValue, min, max);
        }
        else {
            super.init(builder, field);
        }
    }
}
