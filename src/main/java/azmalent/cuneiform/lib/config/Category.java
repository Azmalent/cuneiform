package azmalent.cuneiform.lib.config;

import azmalent.cuneiform.lib.config.annotations.Comment;
import azmalent.cuneiform.lib.config.annotations.Name;
import azmalent.cuneiform.lib.util.StringUtil;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class Category extends AbstractConfigObject {
    public final void init(ForgeConfigSpec.Builder builder) {
        Class clazz = this.getClass();
        Name name = (Name) clazz.getAnnotation(Name.class);
        String categoryName = (name != null) ? name.value() : StringUtil.splitCamelCase(clazz.getSimpleName());

        Comment comment = (Comment) clazz.getAnnotation(Comment.class);
        if (comment != null) {
            builder.comment(comment.value());
        }

        builder.push(categoryName);
        initFields(builder);
        initSubCategories(builder);
        builder.pop();
    }
}