package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.mixin.PlayerInventoryAccessor;
import me.alpha432.oyvey.mixin.EntityAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class WaterClutch extends Module {

    private int previousSlot = -1;

    public WaterClutch() {
        super("WaterClutch", "Automatically places water to prevent fall damage", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        // Trigger if falling 4+ blocks
        if (!player.isOnGround() && player.fallDistance > 4) {
            int waterSlot = findWaterBucketSlot(player);
            if (waterSlot == -1) return;

            PlayerInventoryAccessor inv = (PlayerInventoryAccessor) player.getInventory();
            EntityAccessor entity = (EntityAccessor) player;

            // Remember previous slot
            if (previousSlot == -1) previousSlot = inv.getSelectedSlot();

            // Switch to water bucket
            inv.setSelectedSlot(waterSlot);

            // Look down (pitch only, third-person)
            entity.setPitch(90f);

            // Place water
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() == Items.WATER_BUCKET) {
                mc.interactionManager.interactItem(player, Hand.MAIN_HAND);
            }

            // Restore previous slot
            inv.setSelectedSlot(previousSlot);
            previousSlot = -1;
        }
    }

    private int findWaterBucketSlot(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == Items.WATER_BUCKET) return i;
        }
        return -1;
    }
}
