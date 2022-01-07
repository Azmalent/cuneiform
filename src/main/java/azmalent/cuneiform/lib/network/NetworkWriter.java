package azmalent.cuneiform.lib.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface NetworkWriter<T> extends BiConsumer<FriendlyByteBuf, T> {
    default void write(FriendlyByteBuf buffer, T value) {
        accept(buffer, value);
    }
}
