package azmalent.cuneiform;

import azmalent.cuneiform.lib.config.CommonConfigFile;
import azmalent.cuneiform.lib.config.ServerConfigFile;
import azmalent.cuneiform.lib.config.Category;
import azmalent.cuneiform.lib.config.options.*;
import azmalent.cuneiform.lib.config.annotations.Comment;
import azmalent.cuneiform.lib.config.annotations.Name;
import azmalent.cuneiform.lib.config.options.lazy.ClassListOption;
import azmalent.cuneiform.lib.config.options.ListOption;
import azmalent.cuneiform.lib.config.options.lazy.RegexListOption;
import com.google.common.collect.Lists;

public final class CuneiformConfig {
    public static void init() {
        new Common().register();
        new Server().register();
    }

    public static class Common extends CommonConfigFile {
        public Common() {
            super(Cuneiform.MODID);
        }

        @Comment("Features related to log filtering.")
        public static class Filtering extends Category {
            public static BooleanOption enabled = new BooleanOption(true);

            @Comment("Any lines containing the following text will be removed from the logs.")
            public static ListOption<String> stringsToRemove = new ListOption(Lists.newArrayList(
                "[net.minecraft.util.Util]: No data fixer registered for",
                "[net.minecraft.command.Commands]: Ambiguity between arguments"
            ));

            @Comment("Any lines matching the following regular expressions will be removed from the logs.")
            public static RegexListOption patternsToRemove = new RegexListOption(Lists.newArrayList(
                "\\[net\\.minecraftforge\\.common\\.ForgeConfigSpec\\/CORE\\]: Configuration file .* is not correct. Correcting",
                "\\[net\\.minecraftforge\\.common\\.ForgeConfigSpec\\/CORE\\]: Incorrect key .* was corrected from .* to .*"
            ));

            @Comment("Stack traces from the following exceptions will be truncated, leaving only the message.")
            public static ClassListOption exceptionsToTruncate = new ClassListOption(Lists.newArrayList(
                "com.google.gson.JsonSyntaxException"
            ));
        }

        public static class Tweaks extends Category {
            @Name("Arrow Instant Effect Nerf")
            @Comment("Scales healing and instant damage inflicted by tipped arrows, like in Combat test snapshots.")
            public static BooleanOption tippedArrowPatch = new BooleanOption(true);

            @Name("Arrow Instant Effect Scaling")
            public static DoubleOption tippedArrowScaling = new DoubleOption(1/4d, 1/8d, 1);

            @Name("Shulker Shell Piercing")
            @Comment("Allows arrows fired from crossbows enchanted with Piercing to damage closed shulkers.")
            public static BooleanOption shulkerPiercingPatch = new BooleanOption(true);

            @Name("Sponge Reduces Fall Damage")
            @Comment("Allows sponge blocks to reduce fall damage by 80%, like hay bales.")
            public static BooleanOption spongeFallDamagePatch = new BooleanOption(true);
        }
    }

    public static class Server extends ServerConfigFile {
        public Server() {
            super(Cuneiform.MODID);
        }

        public static class Commands extends Category {
            @Name("/dimteleport")
            @Comment("Allows teleporting between dimensions.")
            public static BooleanOption dimteleport = new BooleanOption(true);

            @Name("/killitems")
            @Comment("Deletes all dropped items.")
            public static BooleanOption killitems = new BooleanOption(true);

            @Name("/killall")
            @Comment("Kills all non-player entities.")
            public static BooleanOption killall = new BooleanOption(true);
        }
    }
}
