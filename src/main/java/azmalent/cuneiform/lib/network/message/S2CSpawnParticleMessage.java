package azmalent.cuneiform.lib.network.message;

import azmalent.cuneiform.lib.network.IMessage;
import com.mojang.math.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public record S2CSpawnParticleMessage(
        ResourceLocation name,
        double xPos, double yPos, double zPos,
        double xSpeed, double ySpeed, double zSpeed) implements IMessage.ServerToClient {

    public S2CSpawnParticleMessage(SimpleParticleType type, Vector3d pos, Vector3d speed) {
        this(type.getRegistryName(), pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
    }

    public S2CSpawnParticleMessage(SimpleParticleType type, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
        this(type.getRegistryName(), xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void handle(NetworkEvent.Context context) {
        Level level = Minecraft.getInstance().level;
        SimpleParticleType type = (SimpleParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(name);
        if (type != null) {
            level.addParticle(type, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }
    }
}
