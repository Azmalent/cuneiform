package azmalent.cuneiform.lib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

@SuppressWarnings("unused")
public interface IMessage {
    NetworkDirection getDirection();
    void handle(NetworkEvent.Context context);

    interface ClientToServer extends IMessage {
        @Override
        default NetworkDirection getDirection() {
            return NetworkDirection.PLAY_TO_SERVER;
        }
    }

    interface ServerToClient extends IMessage {
        @Override
        default NetworkDirection getDirection() {
            return NetworkDirection.PLAY_TO_CLIENT;
        }
    }
}
