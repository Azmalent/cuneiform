package azmalent.cuneiform.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;


public final class DimensionTeleportCommand extends AbstractCommand {
    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> node = dispatcher.register(
            literal("dimteleport")
                .requires(player -> player.hasPermissionLevel(2))
                .then(
                    argument("dimension", DimensionArgument.getDimension())
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().asPlayer();
                        ServerWorld dimension = DimensionArgument.getDimensionArgument(context, "dimension");
                        return teleportPlayer(player, dimension);
                    })
                    .then(
                        argument("position", Vec3Argument.vec3())
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().asPlayer();
                            ServerWorld dimension = DimensionArgument.getDimensionArgument(context, "dimension");
                            Vector3d pos = Vec3Argument.getVec3(context, "position");
                            return teleportPlayer(player, dimension, pos);
                        })
                    )
                )
        );

        dispatcher.register(literal("dimtp").redirect(node));
    }

    private static int teleportPlayer(ServerPlayerEntity player, ServerWorld dimension) {
        return teleportPlayer(player, dimension, player.getPositionVec());
    }

    private static int teleportPlayer(ServerPlayerEntity player, ServerWorld dimension, Vector3d pos) {
        player.teleport(dimension, pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
        return 1;
    }
}
