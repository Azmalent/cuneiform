package azmalent.cuneiform.config;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.CuneiformConfig;
import azmalent.cuneiform.common.data.conditions.ConfigFlagManager;
import azmalent.cuneiform.config.options.AbstractConfigOption;
import azmalent.cuneiform.config.options.BooleanOption;
import azmalent.cuneiform.config.options.IParseableOption;
import azmalent.cuneiform.util.ReflectionUtil;
import azmalent.cuneiform.util.StringUtil;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("unused")
public abstract class ConfigFile {
    private final List<IParseableOption> parseableCache = Lists.newArrayList();

    protected final String modid;
    protected final ModConfig.Type configType;
    protected ForgeConfigSpec spec;

    protected ConfigFile(String modid, ModConfig.Type configType) {
        this.modid = modid;
        this.configType = configType;
    }

    protected String getConfigFilename() {
        return "%s-%s.toml".formatted(modid, configType.toString().toLowerCase());
    }

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
                    } else if (option instanceof IParseableOption p) {
                        parseableCache.add(p);
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

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

    public final void buildSpec() {
        var builder = new ForgeConfigSpec.Builder();

        var clazz = this.getClass();
        initOptions(clazz, builder);
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            initCategory(innerClass, builder);
        }

        spec = builder.build();
    }

    public final void register() {
        ModLoadingContext.get().registerConfig(configType, spec, getConfigFilename());

        FMLJavaModLoadingContext.get().getModEventBus().addListener((ModConfigEvent.Loading event) -> {
            if (event.getConfig().getSpec() == this.spec) {
                this.onLoad();
            }
        });

        FMLJavaModLoadingContext.get().getModEventBus().addListener((ModConfigEvent.Reloading event) -> {
            if (event.getConfig().getSpec() == this.spec) {
                this.onFileChange();
            }
        });
    }

    protected void onLoad() {
        Cuneiform.LOGGER.info("Loading config file " + getConfigFilename());
    }

    protected void onFileChange() {
        Cuneiform.LOGGER.info("Reloading config file " + getConfigFilename());
        parseableCache.forEach(IParseableOption::invalidate);
    }

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
