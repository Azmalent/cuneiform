package azmalent.cuneiform.common.crafting;

import azmalent.cuneiform.Cuneiform;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
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

/**
 * A custom recipe type that defines a byproduct drop when stripping a block
 * with an axe (via the {@link azmalent.cuneiform.mixin.AxeItemMixin}).
 *
 * <p>JSON format:</p>
 * <pre>{@code
 * {
 *   "type": "cuneiform:stripping_byproduct",
 *   "block": "minecraft:oak_log",
 *   "output": { "item": "minecraft:stick" },
 *   "chance": 0.5
 * }
 * }</pre>
 *
 * <p>The {@code chance} field is optional and defaults to 1.0 (100%).</p>
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StrippingByproductRecipe implements Recipe<Container> {
    public static final ResourceLocation TYPE_ID = Cuneiform.prefix("stripping_byproduct");
    public static final ModRecipeType<StrippingByproductRecipe> TYPE = new ModRecipeType<>();

    private final ResourceLocation id;
    private final Block block;
    private final ItemStack output;
    private final float chance;

    /**
     * Creates a new stripping byproduct recipe.
     *
     * @param id the recipe ID
     * @param block the registry ID of the block that triggers the byproduct
     * @param output the item stack to drop
     * @param chance the probability (0–1) of dropping the byproduct
     */
    public StrippingByproductRecipe(ResourceLocation id, ResourceLocation block, ItemStack output, float chance) {
        Preconditions.checkArgument(0 <= chance && chance <= 1, "Chance must be between 0 and 1");

        this.id = id;
        this.block = ForgeRegistries.BLOCKS.getValue(block);
        this.output = output;
        this.chance = chance;
    }

    /**
     * Returns the probability of dropping the byproduct.
     *
     * @return the chance, between 0 and 1
     */
    public float getChance() {
        return chance;
    }

    /**
     * Checks whether this recipe matches the given block state.
     *
     * @param state the block state to test
     * @return {@code true} if the block matches
     */
    public boolean matches(BlockState state) {
        return block == state.getBlock();
    }

    /**
     * Returns a copy of the byproduct output stack.
     *
     * @return the output item stack
     */
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
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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

    /**
     * Serializer for {@link StrippingByproductRecipe}.
     */
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
