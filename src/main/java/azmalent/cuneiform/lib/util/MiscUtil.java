package azmalent.cuneiform.lib.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
public final class MiscUtil {
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

    public static String splitCamelCase(String string) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(string);
        if (words.length > 0) {
            words[0] = StringUtils.capitalize(words[0]);
        }

        return StringUtils.join(words, ' ');
    }
}
