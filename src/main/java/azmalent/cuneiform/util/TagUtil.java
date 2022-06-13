package azmalent.cuneiform.util;

import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
public final class TagUtil {
    public static CompoundTag getNestedCompound(CompoundTag tag, String path) {
        CompoundTag currentTag = tag;
        for (String name : StringUtils.split(path, ".")) {
            currentTag = currentTag.getCompound(name);
        }

        return currentTag;
    }
}
