package azmalent.cuneiform.lib.config;

public abstract class CommonConfigFile extends AbstractConfigFile {
    protected CommonConfigFile(String modid) {
        super(modid);
    }

    @Override
    protected net.minecraftforge.fml.config.ModConfig.Type getConfigType() {
        return net.minecraftforge.fml.config.ModConfig.Type.COMMON;
    }

    @Override
    protected String getConfigFilename() {
        return modid + "-common.toml";
    }

    @Override
    protected void postInit() {
        initFlags(modid);
    }
}
