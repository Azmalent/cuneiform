package azmalent.cuneiform.util;

import azmalent.cuneiform.mixin.accessor.RecipeManagerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Utility methods for working with crafting recipes and containers.
 */
public final class CraftingUtil {
    /**
     * Returns all recipes of the given type from the world's recipe manager.
     *
     * <p>Uses a Mixin accessor to access the recipe manager's internal type-to-recipe map.</p>
     *
     * @param world the world to get recipes from
     * @param type the recipe type to look up
     * @param <C> the container type
     * @param <T> the recipe type
     * @return a map of recipe IDs to recipes
     */
    public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getRecipesByType(Level world, RecipeType<T> type) {
        return ((RecipeManagerAccessor) world.getRecipeManager()).cuneiform_byType(type);
    }

    /**
     * Finds the first item stack in a crafting container that matches the given predicate.
     *
     * @return the first matching stack, or {@link ItemStack#EMPTY} if none found
     */
    public static ItemStack findItemInGrid(CraftingContainer container, @Nonnull Predicate<ItemStack> predicate) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && predicate.test(stack)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }
}
