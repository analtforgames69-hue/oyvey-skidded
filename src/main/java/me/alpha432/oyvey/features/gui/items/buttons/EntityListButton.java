package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.features.gui.items.buttons.Button;
import me.alpha432.oyvey.features.modules.combat.Killaura;
import net.minecraft.client.gui.DrawContext;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.OyVey;

public class EntityListButton extends Button {

    private final Killaura killaura;

    public EntityListButton(Killaura ka) {
        super("Target");
        this.killaura = ka;
        this.height = 14; // match your other buttons
        this.width = 50;  // reasonable default width
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        int color = ColorUtil.toARGB(255, 255, 255, 150); // simple hover effect
        if (this.isHovering(mouseX, mouseY)) {
            color = ColorUtil.toARGB(255, 255, 255, 255);
        }

        // Draw button background
        RenderUtil.rect(context.getMatrices(), this.x, this.y, this.x + this.width, this.y + this.height, color);

        // Draw the text: current target
        drawString("Target: " + killaura.targetEntity, this.x + 2.3f, this.y + 2.0f, -1);
    }

    @Override
    public void onMouseClick() {
        if (killaura.targetEntity.equals("Player")) {
            killaura.targetEntity = "Phantom";
        } else {
            killaura.targetEntity = "Player";
        }
    }
}
