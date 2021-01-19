package azmalent.cuneiform.lib.config;

import net.minecraftforge.fml.config.ModConfig;

public abstract class ServerConfigFile extends AbstractConfigFile {
    protected ServerConfigFile(String modid) {
        super(modid);
    }

    @Override
    protected ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }

    @Override
    protected String getConfigFilename() {
        return modid + "-server.toml";
    }
}
