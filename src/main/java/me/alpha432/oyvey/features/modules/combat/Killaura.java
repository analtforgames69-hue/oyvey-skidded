package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import me.alpha432.oyvey.OyVey;
import java.util.ArrayList;
import java.util.List;

public class Killaura extends Module {

    // Combat settings
    public Setting<Float> range = register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));

    // Entity selection (GUI-friendly)
    public Setting<Boolean> targetPlayer = register(new Setting<>("Player", true));
    public Setting<Boolean> targetPhantom = register(new Setting<>("Phantom", false));

    public Killaura() {
        super("Killaura", "Automatically attacks entities", Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (OyVey.mc.player == null || OyVey.mc.world == null) return;

        for (Entity entity : OyVey.mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity)) continue;
            if (entity == OyVey.mc.player) continue;

            LivingEntity living = (LivingEntity) entity;
            if (living.getHealth() <= 0) continue;

            // Check entity type against selected targets
            if ((entity instanceof PlayerEntity && targetPlayer.getValue()) ||
                (entity instanceof PhantomEntity && targetPhantom.getValue())) {
                attackEntity(living);
            }
        }
    }

    private void attackEntity(LivingEntity entity) {
        if (OyVey.mc.player.getAttackCooldownProgress(0f) < 1.0f) return;
        OyVey.mc.player.attack(entity);
        OyVey.mc.player.swingHand(Hand.MAIN_HAND);
    }

    /**
     * Returns a list of selected entity types for GUI display.
     * Example: ["Player"]
     */
    public List<String> getSelectedEntities() {
        List<String> selected = new ArrayList<>();
        if (targetPlayer.getValue()) selected.add("Player");
        if (targetPhantom.getValue()) selected.add("Phantom");
        return selected;
    }

    /**
     * GUI label helper
     * Example output: "Entities Selected: Player, Phantom"
     */
    public String getEntitiesLabel() {
        List<String> selected = getSelectedEntities();
        if (selected.isEmpty()) return "Entities Selected: None";
        return "Entities Selected: " + String.join(", ", selected);
    }
}
