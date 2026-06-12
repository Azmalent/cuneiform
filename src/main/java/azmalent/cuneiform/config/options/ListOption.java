package azmalent.cuneiform.config.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A config option that holds a list of values of type {@code T}.
 *
 * <p>Each element in the list must pass the provided {@link Predicate} validator.
 * The default list is immutable; use {@link #update(Consumer)} to modify the
 * current value.</p>
 *
 * @param <T> the element type
 */
public final class ListOption<T> extends BasicOption<List<? extends T>> {
    private final Predicate<T> validator;

    private ListOption(List<T> defaultValue, Predicate<T> validator) {
        super(ImmutableList.copyOf(defaultValue));
        this.validator = validator;
    }

    /**
     * Creates a list option with the given default values and element validator.
     */
    public static <T> ListOption<T> of(List<T> defaultValue, Predicate<T> validator) {
        return new ListOption<T>(defaultValue, validator);
    }

    /**
     * Creates a list option with the given default values and a non-null validator.
     */
    public static <T> ListOption<T> of(List<T> defaultValue) {
        return of(defaultValue, Objects::nonNull);
    }

    /**
     * Creates a list option from a variable number of default values.
     */
    @SafeVarargs
    public static <T> ListOption<T> of(T... defaultValues) {
        return of(Lists.newArrayList(defaultValues), Objects::nonNull);
    }

    /**
     * Creates an empty list option with the given element validator.
     */
    public static <T> ListOption<T> empty(Predicate<T> validator) {
        return of(Lists.newArrayList(), validator);
    }

    /**
     * Creates an empty list option with a non-null validator.
     */
    public static <T> ListOption<T> empty() {
        return of(Lists.newArrayList());
    }

    /**
     * Updates the current list value by applying a consumer to it, then sets
     * the modified list back.
     */
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
