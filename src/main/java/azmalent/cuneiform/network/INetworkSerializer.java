package azmalent.cuneiform.network;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkSerializer<T> {
    T read(FriendlyByteBuf buffer);
    void write(FriendlyByteBuf buffer, Object value);
}
