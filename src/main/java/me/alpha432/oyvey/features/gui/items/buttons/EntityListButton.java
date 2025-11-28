package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class EntityListButton extends Button {
    private final Killaura module;
    private boolean extended = false;

    public EntityListButton(String name, Killaura module) {
        super(name);
        this.module = module;
        this.height = 15;
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        // Draw the main button
        RenderUtil.rect(context.getMatrices(), this.x, this.y, this.x + (float) this.width,
                this.y + (float) this.height - 0.5f,
                this.isHovering(mouseX, mouseY) ? 0x77111111 : 0x55111111);
        drawString(this.getName(), this.x + 2.3f, this.y - 2.0f, -1);

        // Draw extended entity toggles
        if (extended) {
            List<BooleanButton> entityButtons = module.getEntityButtons();
            float offsetY = this.y + this.height;
            for (BooleanButton btn : entityButtons) {
                btn.x = this.x;
                btn.y = offsetY;
                btn.width = this.width;
                btn.drawScreen(context, mouseX, mouseY, partialTicks);
                offsetY += btn.getHeight();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.extended = !this.extended;
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
        }

        if (extended) {
            for (BooleanButton btn : module.getEntityButtons()) {
                btn.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public int getHeight() {
        int baseHeight = 14;
        if (extended) {
            for (BooleanButton btn : module.getEntityButtons()) {
                baseHeight += btn.getHeight();
            }
        }
        return baseHeight;
    }
}
