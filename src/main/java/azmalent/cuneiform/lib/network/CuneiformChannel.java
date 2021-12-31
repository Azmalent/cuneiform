package azmalent.cuneiform.lib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CuneiformChannel {
    private final SimpleChannel channel;
    private int index = 0;

    public CuneiformChannel(ResourceLocation channelName, int version) {
        String protocol = Integer.toString(version);
        channel = NetworkRegistry.newSimpleChannel(channelName, () -> protocol, protocol::equals, protocol::equals);
    }

    public <T extends Record & IMessage> void registerMessage(Class<T> clazz) {
        registerMessage(clazz, SerializationHandler::encodeMessage, buffer -> SerializationHandler.decodeMessage(clazz, buffer));
    }

    public <T extends Record & IMessage> void registerMessage(Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder) {
        channel.<T>registerMessage(index++, clazz, encoder, decoder, (message, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection() == message.getDirection()) {
                context.enqueueWork(() -> message.handle(context));
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
