package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;

public class Killaura extends Module {

    public enum TargetMode {
        CLOSEST,
        LOWEST_HEALTH
    }

    public Setting<Float> range = this.register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public Setting<Integer> cps = this.register(new Setting<>("CPS", 10, 1, 20));
    public Setting<TargetMode> targetMode = this.register(new Setting<>("TargetMode", TargetMode.CLOSEST));

    private long lastAttackTime = 0;

    public Killaura() {
        super("Killaura", "Automatically attacks nearby players.", Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        Entity target = getTarget();
        if (target != null && canAttack()) {
            if (rotate.getValue()) faceEntity(target);
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(mc.player.getActiveHand());
            lastAttackTime = System.currentTimeMillis();
        }
    }

    private boolean canAttack() {
        long delay = 1000 / cps.getValue();
        return System.currentTimeMillis() - lastAttackTime >= delay;
    }

    private Entity getTarget() {
        return mc.world.getPlayers().stream()
                .filter(player -> player != mc.player)
                .filter(player -> mc.player.getDistance(player) <= range.getValue())
                .min(getComparator())
                .orElse(null);
    }

    private Comparator<PlayerEntity> getComparator() {
        switch (targetMode.getValue()) {
            case LOWEST_HEALTH:
                return Comparator.comparingDouble(PlayerEntity::getHealth);
            case CLOSEST:
            default:
                return Comparator.comparingDouble(mc.player::getDistance);
        }
    }

    private void faceEntity(Entity entity) {
        double diffX = entity.getPosX() - mc.player.getPosX();
        double diffZ = entity.getPosZ() - mc.player.getPosZ();
        double diffY = entity.getPosY() + entity.getEyeHeight() - (mc.player.getPosY() + mc.player.getEyeHeight());
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (MathHelper.atan2(diffZ, diffX) * (180 / Math.PI)) - 90.0f;
        float pitch = (float) -(MathHelper.atan2(diffY, dist) * (180 / Math.PI));

        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
    }
}
