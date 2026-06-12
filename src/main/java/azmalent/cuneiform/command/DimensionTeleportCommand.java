package azmalent.cuneiform.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;


public final class DimensionTeleportCommand extends AbstractCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(
            literal("dimteleport")
                .requires(player -> player.hasPermission(2))
                .then(
                    argument("dimension", DimensionArgument.dimension())
                    .executes(context -> {
                        ServerPlayer player = context.getSource().getPlayerOrException();
                        ServerLevel dimension = DimensionArgument.getDimension(context, "dimension");
                        return teleportPlayer(player, dimension);
                    })
                    .then(
                        argument("position", Vec3Argument.vec3())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ServerLevel dimension = DimensionArgument.getDimension(context, "dimension");
                            Vec3 pos = Vec3Argument.getVec3(context, "position");
                            return teleportPlayer(player, dimension, pos);
                        })
                    )
                )
        );

        dispatcher.register(literal("dimtp").redirect(node));
    }

    private static int teleportPlayer(ServerPlayer player, ServerLevel dimension) {
        return teleportPlayer(player, dimension, player.position());
    }

    private static int teleportPlayer(ServerPlayer player, ServerLevel dimension, Vec3 pos) {
        player.teleportTo(dimension, pos.x, pos.y, pos.z, player.getYRot(), player.getXRot());
        return 1;
    }
}
