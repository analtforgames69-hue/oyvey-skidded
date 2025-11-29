package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.Hand;

public class Killaura extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    // Settings
    private boolean attackPlayers = true;  // default target
    private boolean attackMobs = false;    // optional
    private double range = 4.5;            // configurable reach
    private float rotationSpeed = 0.3f;    // smooth rotation factor

    public Killaura() {
        super("Killaura", "Automatically attacks targets", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (!isValidTarget(entity)) continue;

            // Rotate player model/head toward target
            rotateHead(entity);

            // Attack target
            mc.interactionManager.attackEntity(mc.player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private boolean isValidTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        if (entity == mc.player) return false;
        if (entity.isRemoved()) return false;
        if (mc.player.distanceTo(entity) > range) return false;

        if (entity instanceof PlayerEntity && attackPlayers) return true;
        if (!(entity instanceof PlayerEntity) && attackMobs) return true;

        return false;
    }

    private void rotateHead(Entity target) {
        float[] angles = MathUtil.calcAngle(mc.player.getEyePos(), target.getEyePos());
        float yaw = angles[0];
        float pitch = angles[1];

        mc.player.headYaw = MathHelper.lerp(rotationSpeed, mc.player.headYaw, yaw);
        mc.player.bodyYaw = MathHelper.lerp(rotationSpeed, mc.player.bodyYaw, yaw);
        mc.player.pitch = MathHelper.lerp(rotationSpeed, mc.player.pitch, pitch);
    }

    // Optional setters for GUI
    public void setAttackPlayers(boolean val) { attackPlayers = val; }
    public void setAttackMobs(boolean val) { attackMobs = val; }
    public void setRange(double val) { range = val; }
    public void setRotationSpeed(float val) { rotationSpeed = val; }
}
