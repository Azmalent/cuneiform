package azmalent.cuneiform;

import azmalent.cuneiform.command.DimensionTeleportCommand;
import azmalent.cuneiform.command.KillAllCommand;
import azmalent.cuneiform.command.KillItemsCommand;
import azmalent.cuneiform.compat.IConsecrationCompat;
import azmalent.cuneiform.filter.FilteringUtil;
import azmalent.cuneiform.lib.compat.ModCompatUtil;
import azmalent.cuneiform.lib.compat.ModProxy;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Cuneiform.MODID)
@Mod.EventBusSubscriber
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
        CommandDispatcher dispatcher = event.getDispatcher();

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
