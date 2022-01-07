package azmalent.cuneiform.lib.config;

import azmalent.cuneiform.common.data.conditions.ConfigFlagManager;
import azmalent.cuneiform.lib.config.options.AbstractConfigOption;
import azmalent.cuneiform.lib.config.options.BooleanOption;
import azmalent.cuneiform.lib.util.ReflectionUtil;
import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

public abstract class AbstractConfigObject {
    protected final void initFields(ForgeConfigSpec.Builder builder) {
        Class<?> clazz = this.getClass();
        AbstractConfigObject instance = ReflectionUtil.getSingletonInstanceOrNull(this.getClass());

        for (Field field : clazz.getFields()) {
            try {
                if (AbstractConfigOption.class.isAssignableFrom(field.getType())) {
                    AbstractConfigOption<?, ?> option = (AbstractConfigOption<?, ?>) field.get(instance);
                    option.init(builder, field);
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void initSubCategories(ForgeConfigSpec.Builder builder) {
        for (Class<?> innerClass : this.getClass().getDeclaredClasses()) {
            if (Category.class.isAssignableFrom(innerClass)) {
                try {
                    Category category = innerClass.asSubclass(Category.class).newInstance();
                    category.init(builder);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void postInit() {

    }

    protected void initFlags(String modid) {
        Class<?> clazz = this.getClass();
        AbstractConfigObject instance = ReflectionUtil.getSingletonInstanceOrNull(this.getClass());

        for (Field field : clazz.getFields()) {
            try {
                if (BooleanOption.class.isAssignableFrom(field.getType())) {
                    BooleanOption option = (BooleanOption) field.get(instance);
                    if (option.hasFlag()) {
                        ConfigFlagManager.putFlag(modid, option.getFlag(), option.get());
                    };
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            if (Category.class.isAssignableFrom(innerClass)) {
                try {
                    Category category = innerClass.asSubclass(Category.class).newInstance();
                    category.initFlags(modid);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
