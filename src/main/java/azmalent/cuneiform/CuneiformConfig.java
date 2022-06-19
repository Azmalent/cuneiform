package azmalent.cuneiform;

import azmalent.cuneiform.config.Comment;
import azmalent.cuneiform.config.ConfigFile;
import azmalent.cuneiform.config.Name;
import azmalent.cuneiform.config.options.BooleanOption;
import azmalent.cuneiform.config.options.ListOption;
import azmalent.cuneiform.config.options.ParseableListOption;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.fml.config.ModConfig;

import java.util.regex.Pattern;

public final class CuneiformConfig extends ConfigFile {
    public static final CuneiformConfig INSTANCE = new CuneiformConfig();

    private CuneiformConfig() {
        super(Cuneiform.MODID, ModConfig.Type.COMMON);
    }

    @Comment("Features related to log filtering.")
    public static class Filtering {
        public static BooleanOption enabled = BooleanOption.of(true);

        @Comment("The following loggers will NOT be filtered. Exceptions will still be handled normally.")
        public static ListOption<String> loggerWhitelist = ListOption.of(Cuneiform.MODID);

        @Comment("The following loggers will be completely ignored. Exceptions will still be handled normally.")
        public static ListOption<String> loggerBlacklist = ListOption.of();

        @Comment("Any lines containing the following substrings will be removed from the logs.")
        public static ListOption<String> substringsToRemove = ListOption.of(
            "[net.minecraft.util.Util]: No data fixer registered for"
        );

        @Comment({"Any lines matching the following regular expressions will be removed from the logs.",
            "Note that the whole line doesn't have to match the regex, it just needs to contain a match in it.",
            "If you want to match the whole string, use ^ and $ symbols (see regex syntax for more info)"})
        public static ParseableListOption<Pattern> patternsToRemove = ParseableListOption.ofRegexes();

        @Comment({"Stack traces from the following exceptions will be truncated, leaving only the message.",
            "The class must be exactly the same, child classes will not be filtered!"})
        public static ParseableListOption<Class<?>> exceptionsToTruncate = ParseableListOption.ofClasses(
            JsonSyntaxException.class.getCanonicalName()
        );

        @Comment({"Exceptions inheriting the following classes will be completely removed from the logs.",
            "The class must be exactly the same, child classes will not be filtered!"})
        public static ParseableListOption<Class<?>> exceptionsToIgnore = ParseableListOption.ofClasses();
    }

    public static class Commands {
        @Name("/dimteleport")
        @Comment("Allows teleporting between dimensions.")
        public static BooleanOption dimteleport = BooleanOption.of(true);
    }
}
