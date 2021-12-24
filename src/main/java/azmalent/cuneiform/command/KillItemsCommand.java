package azmalent.cuneiform.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

public final class KillItemsCommand extends AbstractCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            literal("killitems")
                .requires((player) -> player.hasPermission(2))
                .executes(context -> {
                    MinecraftServer server = context.getSource().getServer();
                    return server.getCommands().performCommand(context.getSource(), "kill @e[type=item]");
                })
        );
    }
}
