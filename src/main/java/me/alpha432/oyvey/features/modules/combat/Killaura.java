package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Killaura extends Module {

    public Setting<Float> range = this.register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public Setting<Integer> cps = this.register(new Setting<>("CPS", 10, 1, 20));
    public Setting<TargetMode> targetMode = this.register(new Setting<>("TargetMode", TargetMode.CLOSEST));

    private enum TargetMode {
        CLOSEST,
        HEALTH,
        RANDOM
    }

    public Killaura() {
        super("Killaura", "Automatically attacks players around you", Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        List<PlayerEntity> targets = mc.world.playerEntities.stream()
                .filter(player -> player != mc.player && mc.player.getDistance(player) <= range.getValue())
                .sorted(getComparator())
                .collect(Collectors.toList());

        if (!targets.isEmpty()) {
            attack(targets.get(0));
        }
    }

    private Comparator<PlayerEntity> getComparator() {
        switch (targetMode.getValue()) {
            case HEALTH:
                return Comparator.comparingDouble(PlayerEntity::getHealth);
            case RANDOM:
                return (a, b) -> (Math.random() < 0.5 ? -1 : 1);
            default:
                return Comparator.comparingDouble(mc.player::getDistance);
        }
    }

    private void attack(PlayerEntity target) {
        if (rotate.getValue()) {
            faceTarget(target);
        }
        mc.player.swingArm(Hand.MAIN_HAND);
        mc.player.attackTargetEntityWithCurrentItem(target);
    }

    private void faceTarget(Entity target) {
        double diffX = target.getPosX() - mc.player.getPosX();
        double diffY = target.getPosY() + target.getEyeHeight(mc.player.getPose()) - (mc.player.getPosY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = target.getPosZ() - mc.player.getPosZ();

        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, dist));

        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
    }
}
