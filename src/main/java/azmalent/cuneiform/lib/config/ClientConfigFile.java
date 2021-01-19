package azmalent.cuneiform.lib.config;

import net.minecraftforge.fml.config.ModConfig;

public abstract class ClientConfigFile extends AbstractConfigFile {
    protected ClientConfigFile(String modid) {
        super(modid);
    }

    @Override
    protected ModConfig.Type getConfigType() {
        return ModConfig.Type.CLIENT;
    }

    @Override
    protected String getConfigFilename() {
        return modid + "-client.toml";
    }
}
