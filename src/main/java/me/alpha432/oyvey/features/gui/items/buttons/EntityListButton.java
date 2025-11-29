package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.features.gui.items.Item;
import me.alpha432.oyvey.features.modules.Module;

import net.minecraft.client.gui.DrawContext;

public class EntityListButton extends Item {
    private final Setting<Target> setting;
    private boolean extended;

    public enum Target {
        PLAYER,
        PHANTOM
    }

    public EntityListButton(Setting<Target> setting) {
        super(setting.getName());
        this.setting = setting;
        this.height = 14;
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        String display = this.setting.getName() + ": " + this.setting.getValue().name();
        drawString(display, this.x + 2.0f, this.y + 1.0f, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovering(mouseX, mouseY)) return;
        if (mouseButton == 0) {
            // Left click cycles forward
            Target[] values = Target.values();
            int next = (this.setting.getValue().ordinal() + 1) % values.length;
            this.setting.setValue(values[next]);
        } else if (mouseButton == 1) {
            // Right click cycles backward
            Target[] values = Target.values();
            int prev = (this.setting.getValue().ordinal() - 1 + values.length) % values.length;
            this.setting.setValue(values[prev]);
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth()
                && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
    }
}
