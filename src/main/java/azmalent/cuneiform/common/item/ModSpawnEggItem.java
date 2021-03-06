package azmalent.cuneiform.common.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModSpawnEggItem<T extends Entity> extends SpawnEggItem {
    private static final DefaultDispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            Direction direction = source.getBlockState().get(DispenserBlock.FACING);
            EntityType<?> entityType = ((ModSpawnEggItem<?>) stack.getItem()).getType(stack.getTag());
            entityType.spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
            stack.shrink(1);
            return stack;
        }
    };

    private final Supplier<EntityType<T>> entityType;

    public ModSpawnEggItem(Supplier<EntityType<T>> type, int primaryColor, int secondaryColor) {
        super(null, primaryColor, secondaryColor, new Item.Properties().group(ItemGroup.MISC));
        entityType = type;

        DispenserBlock.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Nonnull
    @Override
    public EntityType<?> getType(@Nullable CompoundNBT compound) {
        if (compound != null && compound.contains("EntityTag", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT entityTag = compound.getCompound("EntityTag");
            if (entityTag.contains("id", Constants.NBT.TAG_STRING)) {
                return EntityType.byKey(entityTag.getString("id")).orElse(this.entityType.get());
            }
        }

        return this.entityType.get();
    }
}
