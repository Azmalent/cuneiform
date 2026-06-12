package azmalent.cuneiform.network;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.network.message.S2CSpawnParticleMessage;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

/**
 * Central network helper for the Cuneiform mod.
 *
 * <p>Provides a single {@link #CHANNEL} instance and convenience methods for
 * spawning particles on all connected clients.</p>
 */
public final class CuneiformNetwork {
    public static final CuneiformChannel CHANNEL = new CuneiformChannel(Cuneiform.prefix("channel"), 1);

    /**
     * Registers all network messages with the channel.
     */
    public static void registerMessages() {
        CHANNEL.registerMessage(S2CSpawnParticleMessage.class);
    }

    /**
     * Spawns a particle on all clients at the given position with the given speed.
     *
     * @param type the particle type
     * @param pos the position
     * @param speed the speed/direction vector
     */
    public static void spawnParticle(SimpleParticleType type, Vec3 pos, Vec3 speed) {
        var message = new S2CSpawnParticleMessage(type, pos, speed);
        CHANNEL.sendToAllPlayers(message);
    }

    /**
     * Spawns a particle on all clients at the given coordinates with the given speed components.
     *
     * @param type the particle type
     * @param xPos the X position
     * @param yPos the Y position
     * @param zPos the Z position
     * @param xSpeed the X speed component
     * @param ySpeed the Y speed component
     * @param zSpeed the Z speed component
     */
    public static void spawnParticle(SimpleParticleType type, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
        var message = new S2CSpawnParticleMessage(type, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        CHANNEL.sendToAllPlayers(message);
    }
}
