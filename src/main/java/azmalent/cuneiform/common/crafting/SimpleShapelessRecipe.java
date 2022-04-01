package azmalent.cuneiform.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class SimpleShapelessRecipe extends CustomRecipe {
    public SimpleShapelessRecipe(ResourceLocation id) {
        super(id);
    }

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
