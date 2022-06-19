package azmalent.cuneiform.network.message;

import azmalent.cuneiform.network.IMessage;
import azmalent.cuneiform.util.ClientUtil;
import com.mojang.math.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public record S2CSpawnParticleMessage(
        ResourceLocation particleId,
        double xPos, double yPos, double zPos,
        double xSpeed, double ySpeed, double zSpeed) implements IMessage.ServerToClient {

    public S2CSpawnParticleMessage(SimpleParticleType type, Vector3d pos, Vector3d speed) {
        this(type.getRegistryName(), pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
    }

    public S2CSpawnParticleMessage(SimpleParticleType type, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
        this(type.getRegistryName(), xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void onReceive(NetworkEvent.Context context) {
        Level level = ClientUtil.getClientLevel();
        SimpleParticleType type = (SimpleParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(particleId);
        if (level != null && type != null) {
            level.addParticle(type, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }
}
