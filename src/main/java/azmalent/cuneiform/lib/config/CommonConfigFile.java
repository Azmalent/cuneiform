package azmalent.cuneiform.lib.config;

import net.minecraftforge.fml.config.ModConfig.Type;

public abstract class CommonConfigFile extends AbstractConfigFile {
    protected CommonConfigFile(String modid) {
        super(modid);
    }

    @Override
    protected Type getConfigType() {
        return Type.COMMON;
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
