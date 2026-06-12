package azmalent.cuneiform.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * Utility methods for item stack manipulation, block/item conversion, and
 * player inventory management.
 */
public final class ItemUtil {
    /**
     * Extracts the {@link Block} from a {@link BlockItem} stack.
     *
     * @return the block the item represents
     * @throws IllegalArgumentException if the stack's item is not a {@link BlockItem}
     */
    public static Block getBlockFromItem(ItemStack stack) {
        var item = stack.getItem();
        if (item instanceof BlockItem blockItem) {
            return blockItem.getBlock();
        }

        throw new IllegalArgumentException(item + " is not a block item!");
    }

    /**
     * Damages the item in the player's hand by 1 point.
     */
    public static void damageHeldItem(Player player, InteractionHand hand) {
        damageHeldItem(player, hand, 1);
    }

    /**
     * Damages the item in the player's hand by the specified amount.
     */
    public static void damageHeldItem(Player player, InteractionHand hand, int amount) {
        if (!player.level().isClientSide) {
            var stack = player.getItemInHand(hand);

            stack.hurtAndBreak(amount, player, (p) -> {
                p.broadcastBreakEvent(hand);
            });
        }
    }

    /**
     * Damages the item in the player's equipment slot by 1 point.
     */
    public static void damageEquippedItem(Player player, EquipmentSlot slot) {
        damageEquippedItem(player, slot, 1);
    }

    /**
     * Damages the item in the player's equipment slot by the specified amount.
     */
    public static void damageEquippedItem(Player player, EquipmentSlot slot, int amount) {
        if (!player.level().isClientSide) {
            var stack = player.getItemBySlot(slot);

            stack.hurtAndBreak(amount, player, (p) -> {
                p.broadcastBreakEvent(slot);
            });
        }
    }

    /**
     * Gives an item stack to a player, adding it to their inventory or dropping
     * it on the ground if the inventory is full.
     */
    public static void giveStackToPlayer(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    /**
     * Gives an item stack to a player, attempting to place it in the specified
     * hand first. If the hand is not empty, falls back to the general inventory.
     * If the inventory is full, drops the item on the ground.
     */
    public static void giveStackToPlayer(Player player, ItemStack stack, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            player.setItemInHand(hand, stack);
        } else {
            giveStackToPlayer(player, stack);
        }
    }
}
