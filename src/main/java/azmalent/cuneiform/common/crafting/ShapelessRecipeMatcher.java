package azmalent.cuneiform.common.crafting;

import com.google.common.collect.Lists;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Matches shapeless crafting recipes by testing each slot in a
 * {@link CraftingContainer} against a list of {@link StackPredicate predicates}.
 *
 * <p>Each predicate must match exactly one non-empty slot, and every slot must
 * match some predicate. Use the inner {@link Builder} to construct matchers
 * fluently.</p>
 *
 * <p>Designed for use with {@link SimpleShapelessRecipe}.</p>
 */
public final class ShapelessRecipeMatcher {
    /**
     * A predicate that tests an {@link ItemStack}.
     */
    @FunctionalInterface
    public interface StackPredicate extends Predicate<ItemStack> {

    }

    private final StackPredicate[] predicates;
    private final boolean[] matches;

    /**
     * Creates a matcher with the given predicates.
     */
    public ShapelessRecipeMatcher(@Nonnull StackPredicate... predicates) {
        this.predicates = predicates;
        this.matches = new boolean[predicates.length];
        resetMatches();
    }

    private void resetMatches() {
        Arrays.fill(matches, false);
    }

    /**
     * Checks whether the recipe can fit in a grid of the given dimensions.
     */
    public boolean canCraftInDimensions(int width, int height) {
        return predicates.length <= width * height;
    }

    /**
     * Tests whether the contents of the crafting container match all predicates.
     *
     * @param container the crafting container
     * @param level the world level (unused, but required by the recipe matching contract)
     * @return {@code true} if every predicate matches exactly one slot and every non-empty slot is matched
     */
    public boolean matches(CraftingContainer container, @Nonnull Level level) {
        resetMatches();

        for (int i = 0; i < container.getContainerSize(); i++) {
            var stack = container.getItem(i);
            if (!stack.isEmpty()) {
                var matchesAnything = false;

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

        for (boolean match : matches) {
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Builder for constructing {@link ShapelessRecipeMatcher} instances.
     */
    public static class Builder {
        private final List<StackPredicate> predicates = Lists.newArrayList();

        /**
         * Adds an ingredient that matches a specific item.
         *
         * @param item the required item
         * @return this builder
         */
        public Builder addIngredient(ItemLike item) {
            predicates.add(stack -> stack.is( item.asItem() ));
            return this;
        }

        /**
         * Adds an ingredient that matches any item in a tag.
         *
         * @param tag the item tag
         * @return this builder
         */
        public Builder addIngredient(TagKey<Item> tag) {
            predicates.add(stack -> stack.is(tag));
            return this;
        }

        /**
         * Adds a custom ingredient predicate.
         *
         * @param predicate the predicate to test stacks against
         * @return this builder
         */
        public Builder addIngredient(StackPredicate predicate) {
            predicates.add(predicate);
            return this;
        }

        /**
         * Builds the matcher from the accumulated predicates.
         *
         * @return a new {@link ShapelessRecipeMatcher}
         */
        public ShapelessRecipeMatcher build() {
            StackPredicate[] predicateArray = predicates.toArray(new StackPredicate[0]);
            return new ShapelessRecipeMatcher(predicateArray);
        }
    }
}
