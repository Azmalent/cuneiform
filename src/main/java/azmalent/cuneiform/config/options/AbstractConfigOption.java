package azmalent.cuneiform.config.options;

import azmalent.cuneiform.config.Comment;
import azmalent.cuneiform.config.Name;
import azmalent.cuneiform.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.function.Supplier;

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

    protected final String getFieldName(Field field) {
        Name name = field.getAnnotation(Name.class);
        if (name != null) return name.value();
        return StringUtil.splitCamelCase(field.getName());
    }
}
