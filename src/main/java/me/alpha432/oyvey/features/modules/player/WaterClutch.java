package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class WaterClutch extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int previousSlot = -1;
    private boolean clutching = false;

    public WaterClutch() {
        super("WaterClutch", "Automatically places water to avoid fall damage", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        // Only trigger if falling from height
        if (mc.player.fallDistance > 3.0f && !mc.player.isOnGround()) {
            int waterSlot = findWaterBucket();
            if (waterSlot != -1) {
                // Save previous slot
                if (previousSlot == -1) previousSlot = mc.player.getInventory().selectedSlot;

                // Switch to water bucket
                mc.player.getInventory().selectedSlot = waterSlot;

                // Use water bucket
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);

                clutching = true;
            }
        } else if (clutching) {
            // Restore previous slot after clutch
            if (previousSlot != -1) mc.player.getInventory().selectedSlot = previousSlot;
            previousSlot = -1;
            clutching = false;
        }
    }

    /**
     * Finds the first water bucket in the hotbar (slots 0-8)
     */
    private int findWaterBucket() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.WATER_BUCKET) {
                return i;
            }
        }
        return -1; // Not found
    }
}
