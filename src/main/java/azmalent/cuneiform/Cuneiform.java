package azmalent.cuneiform;

import azmalent.cuneiform.command.DimensionTeleportCommand;
import azmalent.cuneiform.command.KillAllCommand;
import azmalent.cuneiform.command.KillItemsCommand;
import azmalent.cuneiform.compat.IConsecrationCompat;
import azmalent.cuneiform.filter.FilteringUtil;
import azmalent.cuneiform.lib.compat.ModCompatUtil;
import azmalent.cuneiform.lib.compat.ModProxy;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Cuneiform.MODID)
@SuppressWarnings({"unused", "unchecked"})
public final class Cuneiform {
    public static final String MODID = "cuneiform";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @ModProxy("consecration")
    public static IConsecrationCompat CONSECRATION_COMPAT;

    public Cuneiform() {
        CuneiformConfig.init();
        if (CuneiformConfig.Common.Filtering.enabled.get()) {
            FilteringUtil.applyLogFilter();
        }

        ModCompatUtil.initModProxies(Cuneiform.class, MODID);

        MinecraftForge.EVENT_BUS.addListener(Cuneiform::registerCommands);
    }

    private static void registerCommands(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        if (CuneiformConfig.Server.Commands.dimteleport.get()) {
            new DimensionTeleportCommand().register(dispatcher);
        }

        if (CuneiformConfig.Server.Commands.killall.get()) {
            new KillAllCommand().register(dispatcher);
        }

        if (CuneiformConfig.Server.Commands.killitems.get()) {
            new KillItemsCommand().register(dispatcher);
        }
    }
}
