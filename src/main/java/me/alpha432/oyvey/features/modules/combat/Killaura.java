package me.alpha432.oyvey.features.modules.combat;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.TickEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class Killaura extends Module {
    public Setting<Float> range = register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public Setting<TargetMode> targetMode = register(new Setting<>("TargetMode", TargetMode.CLOSEST));

    public enum TargetMode {
        CLOSEST,
        HEALTH
    }

    public Killaura() {
        super("Killaura", "Automatically attacks entities", Category.COMBAT, true, false, false);
    }

    @Subscribe
    public void onTick(TickEvent.Pre event) {
        Entity target = getTarget();
        if (target != null) {
            if (rotate.getValue()) {
                faceEntity(target);
            }

            if (mc.player.getAttackCooldownProgress(0) >= 1.0f) { // attack indicator fully recharged
                mc.player.attack(target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private Entity getTarget() {
        Entity bestTarget = null;
        double shortestDistance = range.getValue();

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity) || entity == mc.player || entity.isDead()) continue;

            double distance = mc.player.squaredDistanceTo(entity);
            if (distance > range.getValue() * range.getValue()) continue;

            if (targetMode.getValue() == TargetMode.CLOSEST) {
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestTarget = entity;
                }
            } else if (targetMode.getValue() == TargetMode.HEALTH) {
                if (bestTarget == null || ((LivingEntity) entity).getHealth() < ((LivingEntity) bestTarget).getHealth()) {
                    bestTarget = entity;
                }
            }
        }
        return bestTarget;
    }

    private void faceEntity(Entity entity) {
        Vec3d diff = entity.getPos().subtract(mc.player.getPos());
        double yaw = Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90;
        double pitch = -Math.toDegrees(Math.atan2(diff.y, Math.sqrt(diff.x * diff.x + diff.z * diff.z)));

        mc.player.setYaw((float) yaw);
        mc.player.setPitch((float) pitch);
    }

    @Override
    public String getDisplayInfo() {
        return targetMode.getValue().name();
    }
}
