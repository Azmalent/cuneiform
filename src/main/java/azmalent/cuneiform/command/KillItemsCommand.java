package azmalent.cuneiform.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;

public final class KillItemsCommand extends AbstractCommand {
    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            literal("killitems")
                .requires((player) -> player.hasPermissionLevel(2))
                .executes(context -> {
                    MinecraftServer server = context.getSource().getServer();
                    return server.getCommandManager().handleCommand(context.getSource(), "kill @e[type=item]");
                })
        );
    }
}
