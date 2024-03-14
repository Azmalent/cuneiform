package azmalent.cuneiform.config.options;

import azmalent.cuneiform.util.ReflectionUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class ParseableOption<T> extends AbstractConfigOption<T, String> implements IParseableOption {
    private ForgeConfigSpec.ConfigValue<String> stringValue;
    private final String defaultValue;
    private final Function<String, T> parser;
    private T value;
    private boolean initialized = false;

    private ParseableOption(String defaultValue, Function<String, T> parser) {
        this.defaultValue = defaultValue;
        this.parser = parser;
    }

    public static <T> ParseableOption<T> of(String defaultValue, Function<String, T> parser) {
        return new ParseableOption<T>(defaultValue, parser);
    }

    public static ParseableOption<Class<?>> ofClass(String defaultValue) {
        return of(defaultValue, ReflectionUtil::getClassOrNull);
    }

    public static ParseableOption<Pattern> ofRegex(String defaultValue) {
        return of(defaultValue, Pattern::compile);
    }

    @Deprecated
    public static <T extends IForgeRegistryEntry<T>> ParseableOption<T> ofRegistryItem(IForgeRegistry<T> registry, ResourceLocation defaultId) {
        return of(defaultId.toString(), id -> {
            ResourceLocation key = new ResourceLocation(id);
            if (registry.containsKey(key)) {
                return registry.getValue(key);
            }

            return registry.containsKey(defaultId) ? registry.getValue(defaultId) : null;
        });
    }
    
    @Override
    public T get() {
        if (!initialized) {
            initValue();
        }

        return value;
    }

    private T getDefault() {
        String defaultString = stringValue.get();
        return parser.apply(defaultString);
    }

    @Override
    public void set(String newValue) {
        stringValue.set(newValue);
        invalidate();
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        stringValue = addComment(builder, field, "Default: " + defaultValue).define(getFieldName(field), defaultValue);
    }

    @Override
    public void invalidate() {
        value = null;
        initialized = false;
    }

    @Override
    public void initValue() {
        initialized = true;

        String stringValue = this.stringValue.get();
        value = parser.apply(stringValue);
        if (value == null) value = getDefault();
    }
}
