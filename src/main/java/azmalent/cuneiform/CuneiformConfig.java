package azmalent.cuneiform;

import azmalent.cuneiform.config.Comment;
import azmalent.cuneiform.config.ConfigFile;
import azmalent.cuneiform.config.Name;
import azmalent.cuneiform.config.options.BooleanOption;
import net.minecraftforge.fml.config.ModConfig;

public final class CuneiformConfig extends ConfigFile {
    public static final CuneiformConfig INSTANCE = new CuneiformConfig();
    private CuneiformConfig() {
        super(Cuneiform.MODID, ModConfig.Type.COMMON);
    }
    public static class Commands {
        @Name("/dimteleport")
        @Comment("Allows teleporting between dimensions.")
        public static BooleanOption dimteleport = BooleanOption.of(true);
    }
}
