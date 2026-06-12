package azmalent.cuneiform.network;

import net.minecraft.network.FriendlyByteBuf;

/**
 * Custom serializer for network messages that require manual encoding/decoding.
 *
 * <p>Register with {@link CuneiformChannel#registerMessage(Class, INetworkSerializer)}
 * or {@link SerializationHandler#registerSerializer(Class, INetworkSerializer)}.</p>
 *
 * @param <T> the message type this serializer handles
 * @see SerializationHandler
 */
public interface INetworkSerializer<T> {
    /**
     * Reads a message of type {@code T} from the network buffer.
     */
    T read(FriendlyByteBuf buffer);

    /**
     * Writes a message to the network buffer.
     */
    void write(FriendlyByteBuf buffer, Object value);
}
