package azmalent.cuneiform.common.crafting;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.lib.registry.AbstractRecipe;
import azmalent.cuneiform.lib.registry.ModRecipeType;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StrippingByproductRecipe extends AbstractRecipe<RecipeWrapper> {
    public static final ResourceLocation TYPE_ID = Cuneiform.prefix("stripping_byproduct");
    public static final RecipeType<StrippingByproductRecipe> TYPE = new ModRecipeType<>();

    private final Block block;
    private final ItemStack output;
    private final float chance;

    public StrippingByproductRecipe(ResourceLocation id, ResourceLocation block, ItemStack output, float chance) {
        super(id);

        Preconditions.checkArgument(0 <= chance && chance <= 1, "Chance must be between 0 and 1");
        this.block = ForgeRegistries.BLOCKS.getValue(block);
        this.output = output;
        this.chance = chance;
    }

    public boolean matches(BlockState state) {
        return state.is(block);
    }

    public ItemStack getOutput() {
        return output;
    }

    public float getChance() {
        return chance;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public static class Serializer extends AbstractRecipe.Serializer<StrippingByproductRecipe> {
        public static Serializer INSTANCE = new Serializer();

        @Override
        public StrippingByproductRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation block = new ResourceLocation(json.get("block").getAsString());
            ItemStack output = ShapedRecipe.itemStackFromJson(json.get("output").getAsJsonObject());
            float chance = 1;

            if (json.has("chance")) {
                chance = json.get("chance").getAsFloat();
            }

            return new StrippingByproductRecipe(id, block, output, chance);
        }

        @Nullable
        @Override
        public StrippingByproductRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new StrippingByproductRecipe(id, buffer.readResourceLocation(), buffer.readItem(), buffer.readFloat());
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void toNetwork(FriendlyByteBuf buffer, StrippingByproductRecipe recipe) {
            buffer.writeResourceLocation(recipe.block.getRegistryName());
            buffer.writeItemStack(recipe.output, true);
            buffer.writeFloat(recipe.chance);
        }
    }
}
