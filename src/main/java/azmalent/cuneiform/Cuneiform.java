package azmalent.cuneiform;

import azmalent.cuneiform.command.DimensionTeleportCommand;
import azmalent.cuneiform.common.crafting.StrippingByproductRecipe;
import azmalent.cuneiform.common.data.FuelHandler;
import azmalent.cuneiform.common.data.WanderingTraderHandler;
import azmalent.cuneiform.common.data.conditions.ConfigFlagManager;
import azmalent.cuneiform.network.CuneiformNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Cuneiform.MODID)
public final class Cuneiform {
    public static final String MODID = "cuneiform";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    //TODO: list & map support for auto package serializer
    //TODO: config support for arbitrary objects/records
    //TODO: javadocs
    public Cuneiform() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(Cuneiform::registerRecipeTypes);
        modBus.addListener(ConfigFlagManager::setup);

        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.addListener(Cuneiform::registerCommands);
        eventBus.addListener(FuelHandler::getBurnTime);
        eventBus.addListener(WanderingTraderHandler::registerTrades);

        CuneiformNetwork.registerMessages();
    }

    private void initConfig() {
        CuneiformConfig.INSTANCE.buildSpec();
        CuneiformConfig.INSTANCE.register();
        CuneiformConfig.INSTANCE.sync();
    }

    private static void registerRecipeTypes(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper -> {
            helper.register(StrippingByproductRecipe.TYPE_ID,StrippingByproductRecipe.TYPE);
        });

        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> {
            helper.register(StrippingByproductRecipe.TYPE_ID, StrippingByproductRecipe.Serializer.INSTANCE);
        });
    }

    private static void registerCommands(final RegisterCommandsEvent event) {
        LOGGER.info("Registering commands");
        var dispatcher = event.getDispatcher();

        if (CuneiformConfig.Commands.dimteleport.get()) {
            LOGGER.info("Registering /dimteleport");
            new DimensionTeleportCommand().register(dispatcher);
        }
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name);
    }
}
