package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.gui.items.buttons.BooleanButton;
import me.alpha432.oyvey.features.gui.items.buttons.EntityListButton;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class Killaura extends Module {
    public Setting<Float> range = register(new Setting<>("Range", 5.0f, 0.1f, 10.0f));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));

    // Entity toggles
    public Setting<Boolean> targetPlayers = register(new Setting<>("Players", true));
    public Setting<Boolean> targetPhantoms = register(new Setting<>("Phantoms", false));

    // GUI buttons list
    private final List<BooleanButton> entityButtons = new ArrayList<>();

    public Killaura() {
        super("Killaura", "Automatically attacks entities", Category.COMBAT, true, false, false);

        // Initialize entity buttons
        entityButtons.add(new BooleanButton("Players", targetPlayers));
        entityButtons.add(new BooleanButton("Phantoms", targetPhantoms));
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player) continue;
            if (entity.distanceTo(mc.player) > range.getValue()) continue;

            // Entity type filtering
            if (entity instanceof PlayerEntity && !targetPlayers.getValue()) continue;
            if (entity.getType().getName().getString().equalsIgnoreCase("phantom") && !targetPhantoms.getValue())
                continue;

            // Attack logic
            if (mc.player.getAttackCooldownProgress(0f) < 1.0f) return;

            mc.player.attack(entity);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    // This lets your GUI system access the buttons
    public List<BooleanButton> getEntityButtons() {
        return entityButtons;
    }
}
