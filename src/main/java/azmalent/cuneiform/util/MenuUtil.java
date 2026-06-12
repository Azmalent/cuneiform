package azmalent.cuneiform.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Utility methods for adding standard slot layouts to container menus.
 */
public final class MenuUtil {
    public static final int PLAYER_INVENTORY_X = 8;
    public static final int PLAYER_INVENTORY_Y = 84;
    public static final int HOTBAR_Y = 142;
    public static final int SLOT_OFFSET = 18;

    /**
     * Adds the standard 3×9 inventory and 1×9 hotbar slots to a container menu.
     *
     * @param menu the container menu to add slots to
     * @param inventory the player inventory
     */
    public static void addHotbarAndInventorySlots(AbstractContainerMenu menu, Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            menu.addSlot(new Slot(inventory, i, PLAYER_INVENTORY_X + SLOT_OFFSET * i, HOTBAR_Y));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = 9 + row * 9 + col;
                int x = PLAYER_INVENTORY_X + col * SLOT_OFFSET;
                int y = PLAYER_INVENTORY_Y + row * SLOT_OFFSET;
                menu.addSlot(new Slot(inventory, slotIndex, x, y));
            }
        }
    }

    /**
     * Adds a grid of {@link SlotItemHandler} slots for an {@link ItemStackHandler}
     * to a container menu.
     *
     * @param menu the container menu to add slots to
     * @param stackHandler the item stack handler backing the slots
     * @param xPos the X position of the top-left slot
     * @param yPos the Y position of the top-left slot
     * @param containerWidth the number of slots per row
     * @param containerHeight the number of rows
     */
    public static void addContainerSlots(AbstractContainerMenu menu, ItemStackHandler stackHandler, int xPos, int yPos, int containerWidth, int containerHeight) {
        for (int row = 0; row < containerHeight; row++) {
            for (int col = 0; col < containerWidth; col++) {
                int x = xPos + col * SLOT_OFFSET;
                int y = yPos + row * SLOT_OFFSET;
                menu.addSlot(new SlotItemHandler(stackHandler, row * containerHeight + col, x, y));
            }
        }
    }
}
