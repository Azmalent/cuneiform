package azmalent.cuneiform.network;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.network.message.S2CSpawnParticleMessage;
import com.mojang.math.Vector3d;
import net.minecraft.core.particles.SimpleParticleType;

public final class CuneiformNetwork {
    public static final CuneiformChannel CHANNEL = new CuneiformChannel(Cuneiform.prefix("channel"), 1);

    public static void registerMessages() {
        CHANNEL.registerMessage(S2CSpawnParticleMessage.class);
    }

    public static void spawnParticle(SimpleParticleType type, Vector3d pos, Vector3d speed) {
        var message = new S2CSpawnParticleMessage(type, pos, speed);
        CHANNEL.sendToAllPlayers(message);
    }

    public static void spawnParticle(SimpleParticleType type, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
        var message = new S2CSpawnParticleMessage(type, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        CHANNEL.sendToAllPlayers(message);
    }
}
