package azmalent.cuneiform.config.options;

import azmalent.cuneiform.util.ReflectionUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class ParseableListOption<T> extends AbstractConfigOption<List<T>, List<String>> implements IParseableOption {
    private ForgeConfigSpec.ConfigValue<List<? extends String>> stringValues;
    private final List<String> defaultValue;
    private final Function<String, T> parser;
    private List<T> value;
    private boolean initialized = false;

    private ParseableListOption(List<String> defaultValue, Function<String, T> parser) {
        this.defaultValue = ImmutableList.copyOf(defaultValue);
        this.parser = parser;
    }

    public static <T> ParseableListOption<T> of(Function<String, T> parser) {
        return of(Lists.newArrayList(), parser);
    }

    public static <T> ParseableListOption<T> of(List<String> defaultValue, Function<String, T> constructor) {
        return new ParseableListOption<T>(defaultValue, constructor);
    }

    public static ParseableListOption<Class<?>> ofClasses(String... defaultValues) {
        return of(Lists.newArrayList(defaultValues), ReflectionUtil::getClassOrNull);
    }

    public static ParseableListOption<Pattern> ofRegexes(String... defaultValues) {
        return of(Lists.newArrayList(defaultValues), Pattern::compile);
    }

    @Deprecated
    public static <T extends IForgeRegistryEntry<T>> ParseableListOption<T> ofRegistryItems(IForgeRegistry<T> registry, String... defaultValues) {
        return of(Lists.newArrayList(defaultValues), id -> {
            ResourceLocation key = new ResourceLocation(id);
            return registry.containsKey(key) ? registry.getValue(key) : null;
        });
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringValues() {
        return (List<String>) stringValues.get();
    }

    @Override
    public List<T> get() {
        if (!initialized) {
            initValue();
        }

        return value;
    }

    @Override
    public void set(List<String> newValue) {
        stringValues.set(newValue);
        invalidate();
    }

    public void update(Consumer<List<String>> consumer) {
        var values = getStringValues();
        consumer.accept(values);
        set(values);
    }

    @Override
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        stringValues = addComment(builder, field).defineList(getFieldName(field), defaultValue, Objects::nonNull);
    }

    @Override
    public void invalidate() {
        value = null;
        initialized = false;
    }

    @Override
    public void initValue() {
        initialized = true;
        value = stringValues.get().stream().map(parser).toList().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
