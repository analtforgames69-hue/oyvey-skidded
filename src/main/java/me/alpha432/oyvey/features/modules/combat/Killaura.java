package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.gui.items.buttons.BindButton;
import me.alpha432.oyvey.features.gui.items.buttons.EntityListButton;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.models.Timer;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Killaura extends Module {
    private final Timer attackTimer = new Timer();

    public Setting<Float> range = this.register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public Setting<EntityListButton.Target> targetMode = this.register(new Setting<>("Target", EntityListButton.Target.PLAYER));

    private BindButton bindButton;

    public Killaura() {
        super("Killaura", "Automatically attacks entities around you", Category.COMBAT, true, false, false);
        this.bindButton = new BindButton(this.getBindSetting());
        this.addSettings(range, rotate, targetMode);
    }

    @Override
    public void onUpdate() {
        if (!this.isEnabled() || mc.player == null || mc.world == null) return;

        LivingEntity target = getTarget();
        if (target != null) {
            if (rotate.getValue()) {
                lookAtEntity(target);
            }

            if (mc.player.getAttackCooldownProgress(0f) >= 1.0f) {
                attackEntity(target);
            }
        }
    }

    private LivingEntity getTarget() {
        List<LivingEntity> entities = mc.world.getEntitiesByClass(LivingEntity.class, mc.player.getBoundingBox().expand(range.getValue()), e -> e != mc.player);

        for (LivingEntity entity : entities) {
            if (targetMode.getValue() == EntityListButton.Target.PLAYER && !(entity instanceof PlayerEntity)) continue;
            if (targetMode.getValue() == EntityListButton.Target.PHANTOM && !entity.getType().getName().getString().equalsIgnoreCase("phantom"))
                continue;
            return entity;
        }
        return null;
    }

    private void attackEntity(LivingEntity target) {
        mc.player.attack(target);
        mc.player.swingHand(mc.player.getActiveHand());
        attackTimer.reset();
    }

    private void lookAtEntity(LivingEntity entity) {
        ClientPlayerEntity player = mc.player;
        double diffX = entity.getX() - player.getX();
        double diffY = entity.getY() + entity.getEyeHeight(entity.getPose()) - (player.getY() + player.getEyeHeight(player.getPose()));
        double diffZ = entity.getZ() - player.getZ();

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)));

        player.yaw = MathUtil.smoothRotation(player.yaw, yaw, 10f);
        player.pitch = MathUtil.smoothRotation(player.pitch, pitch, 10f);
    }

    @Override
    public void onDisable() {
        // Reset rotations if necessary
    }

    @Override
    public void onEnable() {
        attackTimer.reset();
    }
}
