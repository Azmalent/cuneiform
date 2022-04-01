package azmalent.cuneiform.lib.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public final class ItemUtil {
    public static Block getBlockFromItem(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BlockItem blockItem) {
            return blockItem.getBlock();
        }

        throw new IllegalArgumentException(item.toString() + " is not a block item!");
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
