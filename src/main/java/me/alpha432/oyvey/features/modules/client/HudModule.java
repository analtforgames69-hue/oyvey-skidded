package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import java.awt.Color;

public class HudModule extends Module {
    public HudModule() {
        super("Hud", "hud", Category.CLIENT, true, false, false);
    }
@Override
public void onRender2D(Render2DEvent event) {
    MinecraftClient mc = MinecraftClient.getInstance();
    String text = "Ignite v1.0.0.0";

    int screenWidth = event.getContext().getScaledWindowWidth();
    int textWidth = mc.textRenderer.getWidth(text);
    int x = screenWidth - textWidth - 2;
    int y = 2;

    // Draw plain white text
    event.getContext().drawTextWithShadow(
            mc.textRenderer,
            text,
            x, y,
            0xFFFFFFFF   // white
    );
}
