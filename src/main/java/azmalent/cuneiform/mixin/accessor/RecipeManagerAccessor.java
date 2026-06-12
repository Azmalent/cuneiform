package azmalent.cuneiform.mixin.accessor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/**
 * Mixin accessor for {@link RecipeManager} that provides access to the
 * internal {@code byType} method.
 *
 * <p>Used by {@link azmalent.cuneiform.util.CraftingUtil#getRecipesByType} to
 * look up all recipes of a given type without iterating the entire recipe map.</p>
 */
@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {
    /**
     * Invokes the private {@code byType} method to get all recipes of the
     * given type.
     *
     * @param type the recipe type to look up
     * @param <C> the container type
     * @param <T> the recipe type
     * @return a map of recipe IDs to recipes
     */
    @Invoker("byType")
    <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> cuneiform_byType(RecipeType<T> type);
}
