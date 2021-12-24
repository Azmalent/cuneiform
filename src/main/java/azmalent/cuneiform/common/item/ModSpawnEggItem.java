package azmalent.cuneiform.common.item;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModSpawnEggItem<T extends Entity> extends SpawnEggItem {
    private static final DefaultDispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {
        public ItemStack dispenseStack(BlockSource source, ItemStack stack) {
            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            EntityType<?> entityType = ((ModSpawnEggItem<?>) stack.getItem()).getType(stack.getTag());
            entityType.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
            stack.shrink(1);
            return stack;
        }
    };

    private final Supplier<EntityType<T>> entityType;

    public ModSpawnEggItem(Supplier<EntityType<T>> type, int primaryColor, int secondaryColor) {
        super(null, primaryColor, secondaryColor, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
        entityType = type;

        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Nonnull
    @Override
    public EntityType<?> getType(@Nullable CompoundTag compound) {
        if (compound != null && compound.contains("EntityTag", Tag.TAG_COMPOUND)) {
            CompoundTag entityTag = compound.getCompound("EntityTag");
            if (entityTag.contains("id", Tag.TAG_STRING)) {
                return EntityType.byString(entityTag.getString("id")).orElse(this.entityType.get());
            }
        }

        return this.entityType.get();
    }
}
