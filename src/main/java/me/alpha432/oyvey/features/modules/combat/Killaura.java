package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.events.TickEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
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
    public Setting<Float> range = register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));

    // Entity selection list
    public Setting<List<String>> targetEntities = register(new Setting<>("TargetEntities", new ArrayList<>(List.of("Player"))));

    public Killaura() {
        super("Killaura", "Automatically attacks entities", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        List<Entity> targets = getTargets();

        for (Entity entity : targets) {
            attackEntity(entity);
        }
    }

    private List<Entity> getTargets() {
        List<Entity> targets = new ArrayList<>();
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity)) continue;
            if (entity == mc.player) continue;
            if (entity.getHealth() <= 0) continue; // 1.21.5 uses getHealth()

            // Filter by selected entity types
            if (entity instanceof PlayerEntity && targetEntities.getValue().contains("Player")) {
                targets.add(entity);
            } else if (entity instanceof PhantomEntity && targetEntities.getValue().contains("Phantom")) {
                targets.add(entity);
            }
        }
        return targets;
    }

    private void attackEntity(Entity entity) {
        if (mc.player.getAttackCooldownProgress(0f) < 1.0f) return; // only attack when ready
        mc.player.attack(entity);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
