package azmalent.cuneiform.lib.util;

import azmalent.cuneiform.lib.registry.ModRecipeType;
import azmalent.cuneiform.mixin.accessor.RecipeManagerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Map;

public final class RecipeUtil {
    public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getRecipesByType(Level world, RecipeType<T> type) {
        return ((RecipeManagerAccessor) world.getRecipeManager()).cuneiform_byType(type);
    }
}
