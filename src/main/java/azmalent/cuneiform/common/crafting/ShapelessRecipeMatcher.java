package azmalent.cuneiform.common.crafting;

import com.google.common.collect.Lists;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
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

public final class ShapelessRecipeMatcher {
    @FunctionalInterface
    public interface StackPredicate extends Predicate<ItemStack> {

    }

    private final StackPredicate[] predicates;
    private final boolean[] matches;

    public ShapelessRecipeMatcher(@Nonnull StackPredicate... predicates) {
        this.predicates = predicates;
        this.matches = new boolean[predicates.length];
        resetMatches();
    }

    private void resetMatches() {
        Arrays.fill(matches, false);
    }

    public boolean canCraftInDimensions(int width, int height) {
        return predicates.length <= width * height;
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

    public static class Builder {
        private final List<StackPredicate> predicates = Lists.newArrayList();

        public Builder addIngredient(ItemLike item) {
            predicates.add(stack -> stack.is( item.asItem() ));
            return this;
        }

        public Builder addIngredient(TagKey<Item> tag) {
            predicates.add(stack -> stack.is(tag));
            return this;
        }

        public Builder addIngredient(StackPredicate predicate) {
            predicates.add(predicate);
            return this;
        }

        public ShapelessRecipeMatcher build() {
            StackPredicate[] predicateArray = predicates.toArray(new StackPredicate[0]);
            return new ShapelessRecipeMatcher(predicateArray);
        }
    }
}
