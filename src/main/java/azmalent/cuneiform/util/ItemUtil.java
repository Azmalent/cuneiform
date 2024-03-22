package azmalent.cuneiform.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public final class ItemUtil {
    public static Block getBlockFromItem(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BlockItem blockItem) {
            return blockItem.getBlock();
        }

        throw new IllegalArgumentException(item + " is not a block item!");
    }

    public static void damageHeldItem(Player player, InteractionHand hand) {
        damageHeldItem(player, hand, 1);
    }

    public static void damageHeldItem(Player player, InteractionHand hand, int amount) {
        if (!player.level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);

            stack.hurtAndBreak(amount, player, (p) -> {
                p.broadcastBreakEvent(hand);
            });
        }
    }

    public static void damageEquippedItem(Player player, EquipmentSlot slot) {
        damageEquippedItem(player, slot, 1);
    }

    public static void damageEquippedItem(Player player, EquipmentSlot slot, int amount) {
        if (!player.level.isClientSide) {
            ItemStack stack = player.getItemBySlot(slot);

            stack.hurtAndBreak(amount, player, (p) -> {
                p.broadcastBreakEvent(slot);
            });
        }
    }

    public static void giveStackToPlayer(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    public static void giveStackToPlayer(Player player, ItemStack stack, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            player.setItemInHand(hand, stack);
        } else {
            giveStackToPlayer(player, stack);
        }
    }
}
