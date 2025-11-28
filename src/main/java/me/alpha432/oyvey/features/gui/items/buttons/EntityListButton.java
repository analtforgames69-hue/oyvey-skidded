package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.features.gui.items.Button;

public class EntityListButton extends Button {

    private final Killaura module;

    public EntityListButton(Killaura module) {
        super("Entities"); // The main label for the button
        this.module = module;
    }

    @Override
    public void onClick() {
        // Example behavior: cycle through entity options or toggle selection
        // For simplicity, toggling Phantom selection when clicked
        module.targetPhantom.setValue(!module.targetPhantom.getValue());
    }

    @Override
    public String getDisplayText() {
        // Count how many entities are currently selected
        int count = 0;
        if (module.targetPlayer.getValue()) count++;
        if (module.targetPhantom.getValue()) count++;

        // Display like "Entities Selected: 1 [+]"
        return "Entities Selected: " + count + " [+]";
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        // Optional: custom rendering
        // Call super if you want default button style
        super.onRender(mouseX, mouseY);
    }
}
