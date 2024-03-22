package azmalent.cuneiform.common.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    @Override
    @SuppressWarnings("ConstantConditions")
    public String toString() {
        return ForgeRegistries.RECIPE_TYPES.getKey(this).toString();
    }
}
