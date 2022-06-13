package azmalent.cuneiform;

import azmalent.cuneiform.config.Comment;
import azmalent.cuneiform.config.ConfigFile;
import azmalent.cuneiform.config.Name;
import azmalent.cuneiform.config.options.*;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraftforge.fml.config.ModConfig;

import java.util.regex.Pattern;

public final class CuneiformConfig {
    public static void init() {
        new Common().register();
        new Server().register();
    }

    public static class Common extends ConfigFile {
        public Common() {
            super(Cuneiform.MODID, ModConfig.Type.COMMON);
        }

        @Comment("Features related to log filtering.")
        public static class Filtering {
            public static BooleanOption enabled = BooleanOption.of(true);

            @Comment("Any lines containing the following substrings will be removed from the logs.")
            public static ListOption<String> substringsToRemove = ListOption.of(
                "[net.minecraft.util.Util]: No data fixer registered for",
                "[net.minecraft.command.Commands]: Ambiguity between arguments"
            );

            @Comment({"Any lines matching the following regular expressions will be removed from the logs.",
                "Note that the whole line doesn't have to match the regex, it just needs to contain a match in it.",
                "If you want to match the whole string, use ^ and $ symbols (see regex syntax for more info)"})
            public static ParseableListOption<Pattern> patternsToRemove = ParseableListOption.ofRegexes(
                "\\[net\\.minecraftforge\\.common\\.ForgeConfigSpec\\/CORE\\]: Configuration file .* is not correct. Correcting",
                "\\[net\\.minecraftforge\\.common\\.ForgeConfigSpec\\/CORE\\]: Incorrect key .* was corrected from .* to .*"
            );

            @Comment({"Stack traces from the following exceptions will be truncated, leaving only the message.",
                "The class must be exactly the same, child classes will not be filtered!"})
            public static ParseableListOption<Class<?>> exceptionsToTruncate = ParseableListOption.ofClasses(
                "com.google.gson.JsonSyntaxException"
            );

            @Comment({"Exceptions inheriting the following classes will be completely removed from the logs.",
                "The class must be exactly the same, child classes will not be filtered!"})
            public static ParseableListOption<Class<?>> exceptionsToIgnore = ParseableListOption.ofClasses();
        }
    }

    public static class Server extends ConfigFile {
        public Server() {
            super(Cuneiform.MODID, ModConfig.Type.SERVER);
        }

        public static class Commands {
            @Name("/dimteleport")
            @Comment("Allows teleporting between dimensions.")
            public static BooleanOption dimteleport = BooleanOption.of(true);
        }
    }
}
