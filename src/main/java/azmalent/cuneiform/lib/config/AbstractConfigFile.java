package azmalent.cuneiform.lib.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public abstract class AbstractConfigFile extends AbstractConfigObject {
    protected final String modid;
    protected final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    protected ForgeConfigSpec spec;

    protected AbstractConfigFile(String modid) {
        this.modid = modid;
    }

    protected abstract ModConfig.Type getConfigType();

    protected abstract String getConfigFilename();

    protected final ForgeConfigSpec getSpec() {
        if (spec == null) spec = builder.build();
        return spec;
    }

    protected void build() {
        initFields(builder);
        initSubCategories(builder);
    }

    public void register() {
        build();
        sync();
        ModLoadingContext.get().registerConfig(getConfigType(), getSpec(), getConfigFilename());
        postInit();
    }

    public void sync() {
        String filename = getConfigFilename();
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(filename);

        CommentedFileConfig configData = CommentedFileConfig.builder(configPath)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();

        configData.load();
        getSpec().setConfig(configData);
    }
}
