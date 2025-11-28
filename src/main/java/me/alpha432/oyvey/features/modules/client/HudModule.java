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
        
        // Calculate rainbow color
        long time = System.currentTimeMillis();
        float hue = (time % 2000) / 2000f; 
        int color = Color.HSBtoRGB(hue, 1f, 1f);   
        int screenWidth = event.getContext().getScaledWindowWidth();
        int textWidth = mc.textRenderer.getWidth(text);
        int x = screenWidth - textWidth - 2;
        int y = 2;
        
        event.getContext().drawTextWithShadow(
                mc.textRenderer,
                text,
                x, y,
                color
        );
    }
}
