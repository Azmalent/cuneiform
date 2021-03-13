package azmalent.cuneiform.lib.config.options;

import azmalent.cuneiform.lib.config.annotations.Comment;
import azmalent.cuneiform.lib.config.annotations.Name;
import azmalent.cuneiform.lib.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class AbstractConfigOption<TGet, TSet> implements Supplier<TGet> {
    @Override
    public abstract TGet get();

    public abstract void set(TSet newValue);

    public abstract void init(ForgeConfigSpec.Builder builder, Field field);

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

    protected final String getName(Field field) {
        Name name = field.getAnnotation(Name.class);
        if (name != null) return name.value();
        return StringUtil.splitCamelCase(field.getName());
    }
}
