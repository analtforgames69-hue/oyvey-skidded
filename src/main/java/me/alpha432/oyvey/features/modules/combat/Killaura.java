package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.gui.KillauraEntitySelectorGUI;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class Killaura extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final KillauraEntitySelectorGUI selector = new KillauraEntitySelectorGUI();

    public Killaura() {
        super(
            "Killaura",
            "Automatically attacks nearby entities",
            Category.COMBAT,
            true,   // has settings
            false,  // default disabled
            true    // visible
        );
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {

            if (!(entity instanceof LivingEntity target)) continue;
            if (target == mc.player) continue;
            if (!selector.isValidTarget(target)) continue;

            if (mc.player.squaredDistanceTo(target) > 4.5) continue;

            if (mc.player.getAttackCooldownProgress(0) < 1) continue;

            rotateTo(target);

            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);

            break;
        }
    }

    private void rotateTo(Entity e) {
        double dx = e.getX() - mc.player.getX();
        double dz = e.getZ() - mc.player.getZ();
        double dy = (e.getY() + e.getHeight() / 2.0) - mc.player.getEyeY();

        double dist = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, dist)));

        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }
}
