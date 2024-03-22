package azmalent.cuneiform.common.crafting;

import azmalent.cuneiform.Cuneiform;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StrippingByproductRecipe implements Recipe<Container> {
    public static final ResourceLocation TYPE_ID = Cuneiform.prefix("stripping_byproduct");
    public static final ModRecipeType<StrippingByproductRecipe> TYPE = new ModRecipeType<>();

    private final ResourceLocation id;
    private final Block block;
    private final ItemStack output;
    private final float chance;

    public StrippingByproductRecipe(ResourceLocation id, ResourceLocation block, ItemStack output, float chance) {
        Preconditions.checkArgument(0 <= chance && chance <= 1, "Chance must be between 0 and 1");

        this.id = id;
        this.block = ForgeRegistries.BLOCKS.getValue(block);
        this.output = output;
        this.chance = chance;
    }

    public float getChance() {
        return chance;
    }

    public boolean matches(BlockState state) {
        return block == state.getBlock();
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }


    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<StrippingByproductRecipe> {
        public static final Serializer INSTANCE = new Serializer();

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
            buffer.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(recipe.block));
            buffer.writeItemStack(recipe.output, true);
            buffer.writeFloat(recipe.chance);
        }
    }
}
