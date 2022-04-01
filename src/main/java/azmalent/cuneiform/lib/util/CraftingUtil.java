package azmalent.cuneiform.lib.util;

import azmalent.cuneiform.common.crafting.ShapelessRecipeMatcher;
import azmalent.cuneiform.mixin.accessor.RecipeManagerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class CraftingUtil {
    public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getRecipesByType(Level world, RecipeType<T> type) {
        return ((RecipeManagerAccessor) world.getRecipeManager()).cuneiform_byType(type);
    }

    public static ItemStack findItemInGrid(CraftingContainer container, @Nonnull Predicate<ItemStack> predicate) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && predicate.test(stack)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    public static ShapelessRecipeMatcher.Builder defineShapelessRecipe() {
        return new ShapelessRecipeMatcher.Builder();
    }
}
