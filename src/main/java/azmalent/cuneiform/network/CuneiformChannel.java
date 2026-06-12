package azmalent.cuneiform.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Wrapper around Forge's {@link SimpleChannel} providing auto-serialization
 * for record-based messages and convenience send methods.
 *
 * <p>Messages registered via {@link #registerMessage(Class)} must be Java
 * {@code record}s implementing {@link IMessage}. Serialization is handled
 * automatically by {@link SerializationHandler}.</p>
 *
 * <p>For messages requiring custom serialization, use
 * {@link #registerMessage(Class, NetworkReader, NetworkWriter)} or
 * {@link #registerMessage(Class, INetworkSerializer)}.</p>
 */
public class CuneiformChannel {
    private final SimpleChannel channel;
    private int index = 0;

    /**
     * Creates a new channel with the given name and protocol version.
     *
     * @param channelName the channel name (e.g. {@code "cuneiform:channel"})
     * @param version the protocol version; clients and servers must match
     */
    public CuneiformChannel(ResourceLocation channelName, int version) {
        String protocol = Integer.toString(version);
        channel = NetworkRegistry.newSimpleChannel(channelName, () -> protocol, protocol::equals, protocol::equals);
    }

    /**
     * Registers a record-based message with auto-serialization.
     */
    public <T extends Record & IMessage> void registerMessage(Class<T> clazz) {
        registerMessage(clazz, buffer -> SerializationHandler.decodeMessage(clazz, buffer), SerializationHandler::encodeMessage);
    }

    /**
     * Registers a message with a custom serializer.
     */
    public <T extends IMessage> void registerMessage(Class<T> clazz, INetworkSerializer<T> serializer) {
        registerMessage(clazz, serializer::read, serializer::write);
    }

    /**
     * Registers a message with custom reader and writer functions.
     */
    public <T extends IMessage> void registerMessage(Class<T> clazz, NetworkReader<T> decoder, NetworkWriter<T> encoder) {
        channel.<T>registerMessage(index++, clazz, (value, buf) -> encoder.write(buf, value), decoder, (msg, sup) -> {
            NetworkEvent.Context context = sup.get();
            if (context.getDirection() == msg.getDirection()) {
                context.enqueueWork(() -> msg.onReceive(context));
            }

            context.setPacketHandled(true);
        });
    }

    /**
     * Sends a message to the server.
     */
    public void sendToServer(IMessage message) {
        channel.sendToServer(message);
    }

    /**
     * Sends a message to a specific player.
     *
     * <p>{@link FakePlayer} instances are silently ignored.</p>
     */
    public void sendToPlayer(ServerPlayer player, IMessage message) {
        if (!(player instanceof FakePlayer)) {
            channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    /**
     * Sends a message to all connected players.
     */
    public void sendToAllPlayers(IMessage message) {
        channel.send(PacketDistributor.ALL.noArg(), message);
    }
}
