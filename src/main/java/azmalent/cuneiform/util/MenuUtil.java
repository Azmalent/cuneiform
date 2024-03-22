package azmalent.cuneiform.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public final class MenuUtil {
    public static final int PLAYER_INVENTORY_X = 8;
    public static final int PLAYER_INVENTORY_Y = 84;
    public static final int HOTBAR_Y = 142;
    public static final int SLOT_OFFSET = 18;

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
