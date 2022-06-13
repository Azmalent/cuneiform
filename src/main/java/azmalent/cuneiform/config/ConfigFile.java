package azmalent.cuneiform.config;

import azmalent.cuneiform.common.data.conditions.ConfigFlagManager;
import azmalent.cuneiform.config.options.BooleanOption;
import azmalent.cuneiform.config.options.AbstractConfigOption;
import azmalent.cuneiform.util.ReflectionUtil;
import azmalent.cuneiform.util.TextUtil;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.lang.reflect.Field;
import java.nio.file.Path;

public abstract class ConfigFile {
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

    protected static void initOptions(Class<?> clazz, ForgeConfigSpec.Builder builder) {
        var instance = ReflectionUtil.getSingletonInstanceOrNull(clazz);

        for (Field field : clazz.getFields()) {
            try {
                if (ReflectionUtil.isSubclass(field.getType(), AbstractConfigOption.class)) {
                    var option = (AbstractConfigOption<?, ?>) field.get(instance);
                    option.init(builder, field);
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void initCategory(Class<?> clazz, ForgeConfigSpec.Builder builder) {
        Name name = clazz.getAnnotation(Name.class);
        String categoryName = (name != null) ? name.value() : TextUtil.splitCamelCase(clazz.getSimpleName());

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

    protected static void initConfigFlags(Class<?> clazz, String modid) {
        var instance = ReflectionUtil.getSingletonInstanceOrNull(clazz);

        for (Field field : clazz.getFields()) {
            try {
                if (ReflectionUtil.isSubclass(field.getType(), BooleanOption.class)) {
                    BooleanOption option = (BooleanOption) field.get(instance);
                    if (option.hasFlag()) {
                        ConfigFlagManager.putFlag(modid, option.getConfigFlag(), option.get());
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            initConfigFlags(innerClass, modid);
        }
    }

    public final void buildSpec() {
        var clazz = this.getClass();

        var builder = new ForgeConfigSpec.Builder();

        initOptions(clazz, builder);
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            initCategory(innerClass, builder);
        }

        spec = builder.build();
    }

    public void register() {
        buildSpec();
        ModLoadingContext.get().registerConfig(configType, spec, getConfigFilename());

        if (configType != ModConfig.Type.CLIENT) {
            initConfigFlags(this.getClass(), modid);
        }
    }

    @SuppressWarnings("unused")
    public void sync() {
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
