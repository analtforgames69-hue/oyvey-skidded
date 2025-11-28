package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.features.gui.items.buttons.EntityListButton;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import java.util.List;
import java.util.stream.Collectors;

public class Killaura extends Module {
    
    // Settings
    public Setting<Float> range = register(new Setting<>("Range", 5.0f, 0.1f, 10.0f));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public Setting<Boolean> targetPlayers = register(new Setting<>("Players", true));
    public Setting<Boolean> targetPhantoms = register(new Setting<>("Phantoms", false));

    // Entity selection button
    private EntityListButton entityListButton;

    public Killaura() {
        super("Killaura", "Automatically attacks entities", Category.COMBAT, true, false, false);

        // Initialize the EntityListButton with available entity types
        List<String> entities = List.of("Player", "Phantom"); // Add more entity types if needed
        this.entityListButton = new EntityListButton("Target Entities", entities, this);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player) continue;
            
            String entityName = entity.getType().getName().getString();

            // Skip entities based on settings
            if (entityName.equalsIgnoreCase("player") && !targetPlayers.getValue()) continue;
            if (entityName.equalsIgnoreCase("phantom") && !targetPhantoms.getValue()) continue;

            if (mc.player.squaredDistanceTo(entity) > range.getValue() * range.getValue()) continue;

            // Attack entity
            mc.player.attack(entity);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    // Getter for the EntityListButton (so the GUI can access it)
    public EntityListButton getEntityListButton() {
        return entityListButton;
    }
}
