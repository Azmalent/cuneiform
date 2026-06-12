package azmalent.cuneiform.config.options;

import azmalent.cuneiform.config.Comment;
import azmalent.cuneiform.config.Name;
import azmalent.cuneiform.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * Abstract base class for all config option types in the annotation-driven config system.
 *
 * <p>Each option wraps a {@link ForgeConfigSpec.ConfigValue} and implements
 * {@link Supplier} to provide convenient access to the current value.</p>
 *
 * <p>Subclasses must implement {@link #init(ForgeConfigSpec.Builder, Field)} to
 * define the option in the config spec, using the field's {@link Name} and
 * {@link Comment} annotations for metadata.</p>
 *
 * @param <TGet> the type returned by {@link #get()}
 * @param <TSet> the type accepted by {@link #set(Object)}
 * @see BasicOption
 * @see BooleanOption
 */
public abstract class AbstractConfigOption<TGet, TSet> implements Supplier<TGet> {
    @Override
    public abstract TGet get();

    /**
     * Sets the config option to a new value.
     */
    public abstract void set(TSet newValue);

    /**
     * Initializes this option in the given config spec builder, reading metadata
     * from the field's {@link Name} and {@link Comment} annotations.
     */
    public abstract void init(ForgeConfigSpec.Builder builder, Field field);

    /**
     * Adds a comment to the builder from the field's {@link Comment} annotation,
     * optionally appending additional comment lines.
     */
    protected final ForgeConfigSpec.Builder addComment(ForgeConfigSpec.Builder builder, Field field, String... additionalComments) {
        Comment comment = field.getAnnotation(Comment.class);
        if (comment != null) {
            if (additionalComments.length > 0) {
                String[] comments = ArrayUtils.addAll(comment.value(), additionalComments);
                return builder.comment(comments);
            }

            return builder.comment(comment.value());
        }

        return additionalComments.length > 0 ? builder.comment(additionalComments) : builder;
    }

    /**
     * Gets the display name for a field from its {@link Name} annotation,
     * or derives it from the field name by splitting camelCase.
     */
    protected final String getFieldName(Field field) {
        Name name = field.getAnnotation(Name.class);
        if (name != null) return name.value();
        return StringUtil.splitCamelCase(field.getName());
    }
}
