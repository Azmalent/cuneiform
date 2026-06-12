package azmalent.cuneiform.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

/**
 * Functional interface for reading a value of type {@code T} from a {@link FriendlyByteBuf}.
 *
 * <p>Extends {@link Function} so it can be used as a lambda or method reference
 * in {@link SerializationHandler#registerSerializer(Class, NetworkReader, NetworkWriter)}.</p>
 *
 * @param <T> the type to read from the buffer
 */
@FunctionalInterface
public interface NetworkReader<T> extends Function<FriendlyByteBuf, T> {
    /**
     * Reads a value from the buffer.
     */
    default T read(FriendlyByteBuf buffer) {
        return apply(buffer);
    }
}
