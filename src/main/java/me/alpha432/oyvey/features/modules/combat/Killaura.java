package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.gui.KillauraEntitySelectorGUI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class Killaura extends Module {

    private final Minecraft mc = Minecraft.getInstance();
    private final KillauraEntitySelectorGUI selectorGUI = new KillauraEntitySelectorGUI();
    private float rotationSpeed = 10.0f; // degrees per tick

    public Killaura() {
        super("Killaura", "Automatically attacks entities", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        mc.world.getEntities().forEach(entity -> {
            if (!(entity instanceof LivingEntity) || entity == mc.player) return;

            TargetType type = getEntityType(entity);
            if (type != null && selectorGUI.isEnabled(type)) {
                rotateHeadSmoothly(mc.player, entity);
                attackEntityIfReady(mc.player, entity);
            }
        });
    }

    private TargetType getEntityType(net.minecraft.entity.Entity entity) {
        if (entity instanceof PlayerEntity) return TargetType.PLAYER;
        return null; // Expand this mapping for other mobs if needed
    }

    private void rotateHeadSmoothly(ClientPlayerEntity player, net.minecraft.entity.Entity target) {
        Vec3d eyePos = player.getEyePos();
        Vec3d targetPos = target.getEyePos();
        float[] angles = calcAngle(eyePos, targetPos);

        float yawDelta = wrapDegrees(angles[0] - player.getYaw());
        float pitchDelta = wrapDegrees(angles[1] - player.getPitch());

        player.setYaw(player.getYaw() + Math.signum(yawDelta) * Math.min(rotationSpeed, Math.abs(yawDelta)));
        player.setPitch(player.getPitch() + Math.signum(pitchDelta) * Math.min(rotationSpeed, Math.abs(pitchDelta)));
    }

    private void attackEntityIfReady(ClientPlayerEntity player, net.minecraft.entity.Entity target) {
        if (player.getAttackCooldownProgress(0.5f) >= 1.0f) {
            mc.interactionManager.attackEntity(player, target);
            player.swingHand(player.getActiveHand());
        }
    }

    private float[] calcAngle(Vec3d from, Vec3d to) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, dist));
        return new float[]{yaw, pitch};
    }

    private float wrapDegrees(float value) {
        value %= 360;
        if (value >= 180) value -= 360;
        if (value < -180) value += 360;
        return value;
    }

    public KillauraEntitySelectorGUI getSelectorGUI() {
        return selectorGUI;
    }
}
