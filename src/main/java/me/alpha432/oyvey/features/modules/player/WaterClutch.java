package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.mixin.PlayerInventoryAccessor;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class WaterClutch extends Module {

    private int previousSlot = -1;

    public WaterClutch() {
        super("WaterClutch", "Automatically places water when falling", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        // Find water bucket in hotbar
        int waterSlot = -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.WATER_BUCKET) {
                waterSlot = i;
                break;
            }
        }

        if (waterSlot == -1) return; // No water bucket, do nothing

        // Rotate head down in third person
        mc.player.pitch = 90f;

        // Save current slot
        if (previousSlot == -1) {
            previousSlot = ((PlayerInventoryAccessor) mc.player.getInventory()).getSelectedSlot();
        }

        // Switch to water bucket
        ((PlayerInventoryAccessor) mc.player.getInventory()).setSelectedSlot(waterSlot);

        // Place water down
        BlockPos pos = mc.player.getBlockPos().down();
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(mc.player.getPos(), mc.player.getHorizontalFacing(), pos, false));

        // Switch back
        ((PlayerInventoryAccessor) mc.player.getInventory()).setSelectedSlot(previousSlot);
        previousSlot = -1;
    }
}
