package azmalent.cuneiform.network.message;

import azmalent.cuneiform.network.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Server-to-client message that spawns a particle on the client.
 *
 * <p>The particle type is transmitted as a {@link ResourceLocation} and resolved
 * on the client via {@link ForgeRegistries#PARTICLE_TYPES}.</p>
 *
 * @param particleId the registry ID of the particle type
 * @param xPos the X position
 * @param yPos the Y position
 * @param zPos the Z position
 * @param xSpeed the X speed component
 * @param ySpeed the Y speed component
 * @param zSpeed the Z speed component
 */
public record S2CSpawnParticleMessage(
        ResourceLocation particleId,
        double xPos, double yPos, double zPos,
        double xSpeed, double ySpeed, double zSpeed) implements IMessage.ServerToClient {

    /**
     * Creates a particle message from a {@link SimpleParticleType} and {@link Vec3} values.
     *
     * @param type the particle type
     * @param pos the position
     * @param speed the speed vector
     */
    public S2CSpawnParticleMessage(SimpleParticleType type, Vec3 pos, Vec3 speed) {
        this(ForgeRegistries.PARTICLE_TYPES.getKey(type), pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
    }

    /**
     * Creates a particle message from a {@link SimpleParticleType} and raw coordinates.
     *
     * @param type the particle type
     * @param xPos the X position
     * @param yPos the Y position
     * @param zPos the Z position
     * @param xSpeed the X speed component
     * @param ySpeed the Y speed component
     * @param zSpeed the Z speed component
     */
    public S2CSpawnParticleMessage(SimpleParticleType type, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
        this(ForgeRegistries.PARTICLE_TYPES.getKey(type), xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void onReceive(NetworkEvent.Context context) {
        Level level = Minecraft.getInstance().level;
        SimpleParticleType type = (SimpleParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(particleId);
        if (level != null && type != null) {
            level.addParticle(type, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }
}
