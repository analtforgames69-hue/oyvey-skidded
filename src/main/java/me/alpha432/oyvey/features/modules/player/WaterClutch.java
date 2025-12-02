package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class WaterClutch extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private boolean placedWater = false;
    private int previousSlot = -1;

    public WaterClutch() {
        super("WaterClutch", "Automatically water-clutches from fall damage", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        double fallDistance = mc.player.fallDistance;
        BlockPos below = mc.player.getBlockPos().down();

        if (placedWater) {
            if (mc.world.getBlockState(below).getBlock() == Blocks.WATER) {
                int emptySlot = findEmptyBucketSlot();
                if (emptySlot != -1) swapHotbarSlot(emptySlot);
            }
            placedWater = false;
            return;
        }

        if (fallDistance >= 4f) {
            int waterSlot = findWaterBucketSlot();
            if (waterSlot == -1) return;
            previousSlot = mc.player.getInventory().selectedSlot;
            swapHotbarSlot(waterSlot);

            mc.player.setPitch(90f);

            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(
                    mc.player.getPos(),
                    Direction.UP,
                    below,
                    false
            ));

            placedWater = true;
        }
    }

    private void swapHotbarSlot(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    private int findWaterBucketSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.WATER_BUCKET) return i;
        }
        return -1;
    }

    private int findEmptyBucketSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.BUCKET) return i;
        }
        return -1;
    }
}
