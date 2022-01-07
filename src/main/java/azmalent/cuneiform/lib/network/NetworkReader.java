package azmalent.cuneiform.lib.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

@FunctionalInterface
public interface NetworkReader<T> extends Function<FriendlyByteBuf, T> {
    default T read(FriendlyByteBuf buffer) {
        return apply(buffer);
    }
}
