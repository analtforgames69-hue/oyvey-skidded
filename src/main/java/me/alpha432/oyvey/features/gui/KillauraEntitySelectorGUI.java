package me.alpha432.oyvey.features.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public class KillauraEntitySelectorGUI {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    private final Set<Class<? extends LivingEntity>> selected =
            new HashSet<>(Set.of(PlayerEntity.class));

    public boolean isValidTarget(LivingEntity e) {
        for (Class<?> clazz : selected) {
            if (clazz.isInstance(e)) return true;
        }
        return false;
    }

    public void render(DrawContext ctx) {
        int x = 10, y = 10;

        ctx.drawText(mc.textRenderer, Text.literal("Killaura Target Selector"), x, y, 0xFFFFFF, false);
        y += 15;

        // Player toggle
        boolean players = selected.contains(PlayerEntity.class);
        ctx.drawText(mc.textRenderer, Text.literal("[ " + (players ? "X" : " ") + " ] Players"), x, y, 0xFFFFFF, false);
    }
}
