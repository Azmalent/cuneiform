package azmalent.cuneiform.network;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

/**
 * Base interface for network messages sent through a {@link CuneiformChannel}.
 *
 * <p>Messages must implement either {@link ClientToServer} or {@link ServerToClient}
 * to declare their direction. The channel uses this to verify that messages are
 * processed on the correct side.</p>
 *
 * <p>For simple auto-serializing messages, use a Java {@code record} that implements
 * one of the direction sub-interfaces. The {@link SerializationHandler} will handle
 * serialization of all record components via reflection.</p>
 *
 * @see CuneiformChannel
 * @see SerializationHandler
 */
public interface IMessage {
    /**
     * Returns the network direction this message travels in.
     */
    NetworkDirection getDirection();

    /**
     * Called when the message is received on the target side.
     */
    void onReceive(NetworkEvent.Context context);

    /**
     * Sub-interface for messages sent from the client to the server.
     */
    interface ClientToServer extends IMessage {
        @Override
        default NetworkDirection getDirection() {
            return NetworkDirection.PLAY_TO_SERVER;
        }
    }

    /**
     * Sub-interface for messages sent from the server to the client.
     */
    interface ServerToClient extends IMessage {
        @Override
        default NetworkDirection getDirection() {
            return NetworkDirection.PLAY_TO_CLIENT;
        }
    }
}
