package azmalent.cuneiform.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@SuppressWarnings("unused")
public class CuneiformChannel {
    private final SimpleChannel channel;
    private int index = 0;

    public CuneiformChannel(ResourceLocation channelName, int version) {
        String protocol = Integer.toString(version);
        channel = NetworkRegistry.newSimpleChannel(channelName, () -> protocol, protocol::equals, protocol::equals);
    }

    public <T extends Record & IMessage> void registerMessage(Class<T> clazz) {
        registerMessage(clazz, buffer -> SerializationHandler.decodeMessage(clazz, buffer), SerializationHandler::encodeMessage);
    }

	public <T extends IMessage> void registerMessage(Class<T> clazz, INetworkSerializer<T> serializer) {
		registerMessage(clazz, serializer::read, serializer::write);
	}

    public <T extends IMessage> void registerMessage(Class<T> clazz, NetworkReader<T> decoder, NetworkWriter<T> encoder) {
        channel.<T>registerMessage(index++, clazz, (value, buf) -> encoder.write(buf, value), decoder, (msg, sup) -> {
            NetworkEvent.Context context = sup.get();
            if (context.getDirection() == msg.getDirection()) {
                context.enqueueWork(() -> msg.onReceive(context));
            }

            context.setPacketHandled(true);
        });
    }

    public void sendToServer(IMessage message) {
        channel.sendToServer(message);
    }

    public void sendToPlayer(ServerPlayer player, IMessage message) {
        if (!(player instanceof FakePlayer)) {
            channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public void sendToAllPlayers(IMessage message) {
        channel.send(PacketDistributor.ALL.noArg(), message);
    }
}
