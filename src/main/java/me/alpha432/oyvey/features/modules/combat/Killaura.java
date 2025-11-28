package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

import static me.alpha432.oyvey.OyVey.mc;

public class Killaura extends Module {

    // Settings
    public Setting<Float> range = new Setting<>("Range", 4.0f, 1.0f, 6.0f);
    public Setting<Boolean> rotate = new Setting<>("Rotate", true);

    // Entity selection (use booleans for UI-friendly version)
    public Setting<Boolean> targetPlayer = new Setting<>("Player", true);
    public Setting<Boolean> targetPhantom = new Setting<>("Phantom", false);

    public Killaura() {
        super("Killaura", "Automatically attacks entities", Category.COMBAT, true, false, false);
        addSettings(range, rotate, targetPlayer, targetPhantom);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) { // getEntities() exists in 1.21.5 mappings as World.getEntities()
            if (!(entity instanceof LivingEntity)) continue;
            if (entity == mc.player) continue;
            LivingEntity living = (LivingEntity) entity;
            if (living.getHealth() <= 0) continue;

            // Check type
            if (entity instanceof PlayerEntity && targetPlayer.getValue()) {
                attackEntity(living);
            } else if (entity instanceof PhantomEntity && targetPhantom.getValue()) {
                attackEntity(living);
            }
        }
    }

    private void attackEntity(LivingEntity entity) {
        if (mc.player.getAttackCooldownProgress(0f) < 1.0f) return; // attack cooldown ready
        mc.player.attack(entity);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
