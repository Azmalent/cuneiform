package azmalent.cuneiform.lib.util;

import net.minecraft.nbt.CompoundNBT;

@SuppressWarnings("unused")
public final class NbtUtil {
    public static CompoundNBT getNestedCompound(CompoundNBT tag, String path) {
        CompoundNBT currentTag = tag;
        String[] tokens = path.split(".");

        for (int i = 0; i < tokens.length; i++) {
            String name = tokens[i];
            tag = currentTag.getCompound(name);
        }

        return tag;
    }
}
