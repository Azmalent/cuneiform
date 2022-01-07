package azmalent.cuneiform.lib.util;

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

    public ItemStack findItemInGrid(CraftingContainer container, @Nonnull Predicate<ItemStack> predicate) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && predicate.test(stack)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    @SafeVarargs
    public final ShapelessMatcher createStackMatcher(@Nonnull Predicate<ItemStack>... predicates) {
        return new ShapelessMatcher(predicates);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public final ShapelessMatcher createItemMatcher(@Nonnull Predicate<Item>... predicates) {
        Predicate<ItemStack>[] stackPredicates = new Predicate[predicates.length];
        for (int i = 0; i < predicates.length; i++) {
            Predicate<Item> predicate = predicates[i];
            stackPredicates[i] = stack -> predicate.test(stack.getItem());
        }

        return new ShapelessMatcher(stackPredicates);
    }

    public static final class ShapelessMatcher {
        private final Predicate<ItemStack>[] predicates;
        private final boolean[] matches;

        @SafeVarargs
        private ShapelessMatcher(@Nonnull Predicate<ItemStack>... predicates) {
            this.predicates = predicates;
            this.matches = new boolean[predicates.length];
            resetMatches();
        }

        private void resetMatches() {
            Arrays.fill(matches, false);
        }

        public boolean matches(CraftingContainer container, @Nonnull Level level) {
            resetMatches();

            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.isEmpty()) {
                    boolean matchesAnything = false;

                    for (int j = 0; j < predicates.length; j++) {
                        if (predicates[j].test(stack) && !matches[j]) {
                            matches[j] = true;
                            matchesAnything = true;
                            break;
                        }
                    }

                    if (!matchesAnything) return false;
                }
            }

            return true;
        }
    }
}
