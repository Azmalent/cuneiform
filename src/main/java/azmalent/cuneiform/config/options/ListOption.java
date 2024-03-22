package azmalent.cuneiform.config.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ListOption<T> extends BasicOption<List<? extends T>> {
    private final Predicate<T> validator;

    private ListOption(List<T> defaultValue, Predicate<T> validator) {
        super(ImmutableList.copyOf(defaultValue));
        this.validator = validator;
    }

    public static <T> ListOption<T> of(List<T> defaultValue, Predicate<T> validator) {
        return new ListOption<T>(defaultValue, validator);
    }

    public static <T> ListOption<T> of(List<T> defaultValue) {
        return of(defaultValue, Objects::nonNull);
    }

    @SafeVarargs
    public static <T> ListOption<T> of(T... defaultValues) {
        return of(Lists.newArrayList(defaultValues), Objects::nonNull);
    }

    public static <T> ListOption<T> empty(Predicate<T> validator) {
        return of(Lists.newArrayList(), validator);
    }

    public static <T> ListOption<T> empty() {
        return of(Lists.newArrayList());
    }

    @SuppressWarnings("unchecked")
    public void update(Consumer<List<T>> consumer) {
        var values = get();
        consumer.accept((List<T>) values);
        set(values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(ForgeConfigSpec.Builder builder, Field field) {
        value = addComment(builder, field).defineList(getFieldName(field), defaultValue, (Predicate<Object>) validator);
    }
}
