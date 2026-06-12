package azmalent.cuneiform.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;

/**
 * Functional interface for writing a value of type {@code T} to a {@link FriendlyByteBuf}.
 *
 * <p>Extends {@link BiConsumer} so it can be used as a lambda or method reference
 * in {@link SerializationHandler#registerSerializer(Class, NetworkReader, NetworkWriter)}.</p>
 *
 * @param <T> the type to write to the buffer
 */
@FunctionalInterface
public interface NetworkWriter<T> extends BiConsumer<FriendlyByteBuf, T> {
    /**
     * Writes a value to the buffer.
     */
    default void write(FriendlyByteBuf buffer, T value) {
        accept(buffer, value);
    }
}
