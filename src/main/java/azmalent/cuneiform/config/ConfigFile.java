package azmalent.cuneiform.config;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.common.data.conditions.ConfigFlagManager;
import azmalent.cuneiform.config.options.AbstractConfigOption;
import azmalent.cuneiform.config.options.BooleanOption;
import azmalent.cuneiform.util.ReflectionUtil;
import azmalent.cuneiform.util.StringUtil;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.lang.reflect.Field;
import java.nio.file.Path;

//TODO: rewrite config system
/**
 * Base class for annotation-driven configuration files built on Forge's
 * {@link ForgeConfigSpec} and NightConfig.
 *
 * <p>Subclasses should:</p>
 * <ol>
 *   <li>Extend this class and call the constructor with their mod ID and config type</li>
 *   <li>Define public static fields of {@link AbstractConfigOption} subtypes</li>
 *   <li>Optionally group fields in inner static classes to create config categories</li>
 *   <li>Annotate fields with {@link Name} and {@link Comment} for display names and comments</li>
 *   <li>Call {@link #buildSpec()}, {@link #register()}, and optionally {@link #sync()}</li>
 * </ol>
 *
 * <p>Config flags for use in JSON conditions (recipes, loot tables) are registered
 * automatically from {@link BooleanOption} instances that have a non-null config flag name.</p>
 *
 * <p>Subclasses typically use an {@code INSTANCE} singleton pattern, discovered via
 * {@link ReflectionUtil#getSingletonInstanceOrNull}.</p>
 *
 * @see AbstractConfigOption
 * @see Name
 * @see Comment
 */
public abstract class ConfigFile {
    protected final String modid;
    protected final ModConfig.Type configType;
    protected ForgeConfigSpec spec;

    protected ConfigFile(String modid, ModConfig.Type configType) {
        this.modid = modid;
        this.configType = configType;
    }

    /**
     * Returns the config filename in the format {@code "modid-type.toml"}.
     *
     * @return the config filename
     */
    protected String getConfigFilename() {
        return "%s-%s.toml".formatted(modid, configType.toString().toLowerCase());
    }

    /**
     * Initializes all config option fields on the given class by scanning its
     * public fields for {@link AbstractConfigOption} instances.
     *
     * <p>For {@link BooleanOption} fields with a config flag, the flag is
     * automatically registered with {@link ConfigFlagManager} (COMMON configs only).</p>
     *
     * @param clazz the class whose fields to scan
     * @param builder the config spec builder
     */
    protected final void initOptions(Class<?> clazz, ForgeConfigSpec.Builder builder) {
        var instance = ReflectionUtil.getSingletonInstanceOrNull(clazz);

        for (Field field : clazz.getFields()) {
            try {
                if (ReflectionUtil.isSubclass(field.getType(), AbstractConfigOption.class)) {
                    var option = (AbstractConfigOption<?, ?>) field.get(instance);
                    option.init(builder, field);

                    if (option instanceof BooleanOption b && b.hasFlag()) {
                        if (configType == ModConfig.Type.COMMON) {
                            ConfigFlagManager.putFlag(modid, b.getConfigFlag(), b);
                        } else {
                            Cuneiform.LOGGER.warn(("Found flag '%s' in %s. " +
                                "Ignoring as flags are only usable in common config files")
                                .formatted(b.getConfigFlag(), getConfigFilename()));
                        }
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes a config category by scanning the given class and its inner classes.
     *
     * <p>The category name is derived from the {@link Name} annotation or the
     * class's simple name (split from camelCase).</p>
     *
     * @param clazz the category class
     * @param builder the config spec builder
     */
    protected final void initCategory(Class<?> clazz, ForgeConfigSpec.Builder builder) {
        Name name = clazz.getAnnotation(Name.class);
        String categoryName = (name != null) ? name.value() : StringUtil.splitCamelCase(clazz.getSimpleName());

        Comment comment = clazz.getAnnotation(Comment.class);
        if (comment != null) {
            builder.comment(comment.value());
        }

        builder.push(categoryName);

        initOptions(clazz, builder);
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            initCategory(innerClass, builder);
        }

        builder.pop();
    }

    /**
     * Builds the {@link ForgeConfigSpec} by scanning this class and its inner
     * classes for config option fields.
     */
    public final void buildSpec() {
        var builder = new ForgeConfigSpec.Builder();

        var clazz = this.getClass();
        initOptions(clazz, builder);
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            initCategory(innerClass, builder);
        }

        spec = builder.build();
    }

    /**
     * Registers the config with Forge and attaches load/reload listeners.
     */
    public final void register() {
        ModLoadingContext.get().registerConfig(configType, spec, getConfigFilename());

        FMLJavaModLoadingContext.get().getModEventBus().addListener((ModConfigEvent.Loading event) -> {
            if (event.getConfig().getSpec() == this.spec) {
                this.onLoad();
            }
        });

        FMLJavaModLoadingContext.get().getModEventBus().addListener((ModConfigEvent.Reloading event) -> {
            if (event.getConfig().getSpec() == this.spec) {
                this.onReload();
            }
        });
    }

    /**
     * Called when the config is loaded for the first time.
     * Override to perform custom initialization logic.
     */
    protected void onLoad() {
        Cuneiform.LOGGER.info("Loading config file " + getConfigFilename());
    }

    /**
     * Called when the config file is reloaded (e.g. via the {@code /forge config} command).
     * Override to perform custom reload logic.
     */
    protected void onReload() {
        Cuneiform.LOGGER.info("Reloading config file " + getConfigFilename());
    }

    /**
     * Synchronizes the config from disk. Only applicable to clientside configs;
     * logs a warning if called on a server config.
     */
    public void sync() {
        if (configType == ModConfig.Type.SERVER) {
            Cuneiform.LOGGER.warn("sync() called on a server config");
            return;
        }

        String filename = getConfigFilename();
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(filename);

        var configData = CommentedFileConfig.builder(configPath)
            .sync()
            .autosave()
            .preserveInsertionOrder()
            .writingMode(WritingMode.REPLACE)
            .build();

        configData.load();
        configData.save();

        spec.setConfig(configData);
    }
}
