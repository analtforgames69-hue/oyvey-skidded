package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.gui.items.buttons.BooleanButton;
import me.alpha432.oyvey.features.gui.items.buttons.BindButton;
import me.alpha432.oyvey.features.gui.items.buttons.EntityListButton;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

public class Killaura extends Module {

    // Settings
    public Setting<Boolean> targetPlayers;
    public Setting<Boolean> targetPhantoms;

    // GUI buttons
    private final List<BooleanButton> entityButtons = new ArrayList<>();
    public EntityListButton entityListButton;
    public BindButton bindButton;

    public Killaura() {
        super("Killaura", "Automatically attacks selected entities around you", Module.Category.COMBAT, true, false, false);

        // Settings
        targetPlayers = register(new Setting<>("Players", true));
        targetPhantoms = register(new Setting<>("Phantoms", false));

        // Buttons for GUI
        entityButtons.add(new BooleanButton(targetPlayers));
        entityButtons.add(new BooleanButton(targetPhantoms));

        // Entity list GUI button
        entityListButton = new EntityListButton("Target Entities", this);

        // Bind button
        bindButton = new BindButton(this);
    }

    public List<BooleanButton> getEntityButtons() {
        return entityButtons;
    }

    @Override
    public void onTick() {
        if (!isOn() || fullNullCheck()) return;

        ClientPlayerEntity player = mc.player;

        // Only attack when attack indicator is full (cooldown ready)
        if (!player.getAttackCooldownProgress(0f).equals(1.0f)) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == player) continue;

            // Only attack selected entity types
            if ((entity instanceof PlayerEntity && !targetPlayers.getValue()) ||
                (entity.getType().getName().getString().equalsIgnoreCase("phantom") && !targetPhantoms.getValue()))
                continue;

            // Distance check (3 blocks)
            if (player.squaredDistanceTo(entity) <= 9.0) {
                // Attack
                player.attack(entity);
                player.swingHand(player.getActiveHand());
            }
        }
    }

    public static boolean fullNullCheck() {
        return mc.player == null || mc.world == null;
    }
}
