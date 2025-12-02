package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.mixininterface.IPlayerInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

public class WaterClutch extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int previousSlot = -1;
    private boolean clutching = false;

    public WaterClutch() {
        super("WaterClutch", "Automatically places water to prevent fall damage", Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        previousSlot = -1;
        clutching = false;
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        // Only try to clutch if falling
        if (mc.player.fallDistance > 3.0f && !mc.player.isOnGround()) {
            int waterSlot = findWaterBucket();

            if (waterSlot != -1) {
                IPlayerInventory inv = (IPlayerInventory) mc.player.getInventory();

                // Store previous slot if not already
                if (previousSlot == -1) previousSlot = inv.getSelectedSlot();

                // Switch to water bucket
                inv.setSelectedSlot(waterSlot);

                // Simulate right-click
                mc.interactionManager.interactItem(mc.player, mc.world, mc.player.getStackInHand(mc.player.getActiveHand()));

                clutching = true;
            }
        } else if (clutching) {
            // Return to previous slot
            IPlayerInventory inv = (IPlayerInventory) mc.player.getInventory();
            if (previousSlot != -1) inv.setSelectedSlot(previousSlot);
            previousSlot = -1;
            clutching = false;
        }
    }

    // Find water bucket in hotbar
    private int findWaterBucket() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.WATER_BUCKET) return i;
        }
        return -1;
    }
}
