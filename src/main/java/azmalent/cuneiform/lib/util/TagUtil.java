package azmalent.cuneiform.lib.util;

import net.minecraft.nbt.CompoundTag;

@SuppressWarnings("unused")
public final class TagUtil {
    public static CompoundTag getNestedCompound(CompoundTag tag, String path) {
        CompoundTag currentTag = tag;
        String[] tokens = path.split("\\.");

        for (int i = 0; i < tokens.length; i++) {
            String name = tokens[i];
            tag = currentTag.getCompound(name);
        }

        return tag;
    }
}
