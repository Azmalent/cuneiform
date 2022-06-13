package azmalent.cuneiform;

import azmalent.cuneiform.command.DimensionTeleportCommand;
import azmalent.cuneiform.command.KillAllCommand;
import azmalent.cuneiform.command.KillItemsCommand;
import azmalent.cuneiform.common.crafting.StrippingByproductRecipe;
import azmalent.cuneiform.common.data.FuelHandler;
import azmalent.cuneiform.common.data.WanderingTraderHandler;
import azmalent.cuneiform.common.data.conditions.ConfigFlagManager;
import azmalent.cuneiform.filter.FilteringHandler;
import azmalent.cuneiform.network.CuneiformNetwork;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Cuneiform.MODID)
@SuppressWarnings({"unused"})
public final class Cuneiform {
    public static final String MODID = "cuneiform";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Cuneiform() {
        CuneiformConfig.init();
        if (CuneiformConfig.Common.Filtering.enabled.get()) {
            FilteringHandler.applyLogFilter();
        }

        FMLJavaModLoadingContext.get().getModEventBus().register(ConfigFlagManager.class);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, Cuneiform::registerRecipeTypes);

        MinecraftForge.EVENT_BUS.addListener(Cuneiform::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(FuelHandler::getBurnTime);
        MinecraftForge.EVENT_BUS.addListener(WanderingTraderHandler::registerTrades);

        CuneiformNetwork.registerMessages();
    }

    private static void registerRecipeTypes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        IForgeRegistry<RecipeSerializer<?>> serializers = event.getRegistry();

        Registry.register(Registry.RECIPE_TYPE, StrippingByproductRecipe.TYPE_ID, StrippingByproductRecipe.TYPE);
        serializers.register(StrippingByproductRecipe.Serializer.INSTANCE.setRegistryName(StrippingByproductRecipe.TYPE_ID));
    }

    private static void registerCommands(final RegisterCommandsEvent event) {
        LOGGER.info("Registering commands");
        var dispatcher = event.getDispatcher();

        if (CuneiformConfig.Server.Commands.dimteleport.get()) {
            LOGGER.info("Registering /dimteleport");
            new DimensionTeleportCommand().register(dispatcher);
        }

        if (CuneiformConfig.Server.Commands.killall.get()) {
            LOGGER.info("Registering /killall");
            new KillAllCommand().register(dispatcher);
        }

        if (CuneiformConfig.Server.Commands.killitems.get()) {
            LOGGER.info("Registering /killitems");
            new KillItemsCommand().register(dispatcher);
        }
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name);
    }
}
