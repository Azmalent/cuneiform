package azmalent.cuneiform.common.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A simple {@link RecipeType} implementation that returns its registry key
 * from {@link #toString()}.
 *
 * <p>Used as a base for custom recipe types registered via
 * {@link net.minecraftforge.registries.RegisterEvent}.</p>
 *
 * @param <T> the recipe type this recipe type handles
 */
public class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    @Override
    @SuppressWarnings("ConstantConditions")
    public String toString() {
        return ForgeRegistries.RECIPE_TYPES.getKey(this).toString();
    }
}
