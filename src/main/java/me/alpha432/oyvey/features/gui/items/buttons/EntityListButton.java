package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.items.Item;
import me.alpha432.oyvey.features.modules.combat.Killaura;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class EntityListButton extends Item {
    private final Killaura killaura;
    private final List<Button> entityButtons = new ArrayList<>();

    public EntityListButton(Killaura killaura) {
        super("Target Entity");
        this.killaura = killaura;

        // Create buttons for each entity type
        entityButtons.add(new Button("Player") {
            @Override
            public void onMouseClick() {
                super.onMouseClick();
                killaura.setTargetEntity("Player");
                updateButtonStates("Player");
            }
        });
        entityButtons.add(new Button("Phantom") {
            @Override
            public void onMouseClick() {
                super.onMouseClick();
                killaura.setTargetEntity("Phantom");
                updateButtonStates("Phantom");
            }
        });

        // Initialize buttons positions
        float offsetY = this.y + this.height;
        for (Button btn : entityButtons) {
            btn.setWidth(this.width); // match parent width
            btn.setHeight(14);
            btn.setX(this.x);
            btn.setY(offsetY);
            offsetY += btn.getHeight();
        }
    }

    private void updateButtonStates(String selected) {
        for (Button btn : entityButtons) {
            btn.state = btn.getName().equals(selected);
        }
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(context, mouseX, mouseY, partialTicks);
        for (Button btn : entityButtons) {
            btn.drawScreen(context, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Button btn : entityButtons) {
            btn.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void setLocation(float x, float y) {
        super.setLocation(x, y);
        float offsetY = y + this.height;
        for (Button btn : entityButtons) {
            btn.setX(x);
            btn.setY(offsetY);
            offsetY += btn.getHeight();
        }
    }
}
