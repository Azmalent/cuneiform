package azmalent.cuneiform.common.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Base class for custom shapeless recipes that use a {@link ShapelessRecipeMatcher}
 * for ingredient matching.
 *
 * <p>Subclasses must implement {@link #getMatcher()} to provide the matcher, and
 * should override {@link #assemble} to produce the crafting result.</p>
 */
public abstract class SimpleShapelessRecipe extends CustomRecipe {
    /**
     * Creates a new simple shapeless recipe.
     *
     * @param id the recipe ID
     * @param category the crafting book category
     */
    public SimpleShapelessRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    /**
     * Returns the {@link ShapelessRecipeMatcher} used to match this recipe.
     */
    abstract protected ShapelessRecipeMatcher getMatcher();

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return getMatcher().canCraftInDimensions(width, height);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(CraftingContainer container, Level level) {
        return getMatcher().matches(container, level);
    }
}
