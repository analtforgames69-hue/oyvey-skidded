package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Killaura extends Module {

    public enum TargetMode {
        CLOSEST,
        HEALTH,
        SMART
    }

    private final Minecraft mc = Minecraft.getInstance();

    public Setting<Float> range = register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public Setting<Integer> cps = register(new Setting<>("CPS", 10, 1, 20));
    public Setting<TargetMode> targetMode = register(new Setting<>("TargetMode", TargetMode.CLOSEST));

    public Killaura() {
        super("Killaura", "Automatically attacks entities around you.", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;

        Player target = getTarget();
        if (target != null) attack(target);
    }

    private Player getTarget() {
        List<Player> players = mc.level.players();
        Player closest = null;
        double closestDist = range.getValue() * range.getValue();

        for (Player player : players) {
            if (player == mc.player || player.isDeadOrDying()) continue;

            double distance = mc.player.distanceToSqr(player);
            if (distance <= closestDist) {
                switch (targetMode.getValue()) {
                    case CLOSEST:
                        closest = player;
                        closestDist = distance;
                        break;
                    case HEALTH:
                        if (closest == null || player.getHealth() < closest.getHealth()) closest = player;
                        break;
                    case SMART:
                        // Simplified smart targeting
                        closest = player;
                        break;
                }
            }
        }
        return closest;
    }

    private void attack(Player target) {
        if (rotate.getValue()) faceEntity(target);

        mc.player.swing(InteractionHand.MAIN_HAND);
        mc.player.attack(target);
    }

    private void faceEntity(Entity entity) {
        Vec3 eyesPos = mc.player.getEyePosition(1.0f);
        Vec3 targetPos = entity.position().add(0, entity.getBbHeight() / 2.0, 0);
        double dx = targetPos.x - eyesPos.x;
        double dy = targetPos.y - eyesPos.y;
        double dz = targetPos.z - eyesPos.z;

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distanceXZ));

        mc.player.setYRot(yaw);
        mc.player.setXRot(pitch);
    }
}
