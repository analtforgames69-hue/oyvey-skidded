package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.features.modules.combat.TargetType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.TextLayerType;

import java.util.*;
import java.util.stream.Collectors;

@net.fabricmc.api.EnvType(net.fabricmc.api.EnvType.CLIENT)
public class KillauraEntitySelectorGUI {

    private final Map<TargetType, Boolean> targetToggles = new EnumMap<>(TargetType.class);
    private String searchQuery = "";
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public KillauraEntitySelectorGUI() {
        for (TargetType type : TargetType.values()) {
            targetToggles.put(type, type == TargetType.PLAYER); // Default: only players
        }
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query.toLowerCase();
    }

    public void toggle(TargetType type) {
        targetToggles.put(type, !targetToggles.get(type));
    }

    public boolean isEnabled(TargetType type) {
        return targetToggles.getOrDefault(type, false);
    }

    public List<TargetType> getVisibleTargets() {
        return Arrays.stream(TargetType.values())
                .filter(t -> t.name().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        int y = 20;

        TextRenderer textRenderer = mc.textRenderer;
        VertexConsumerProvider.Immediate vertexConsumers = mc.getBufferBuilders().getEntityVertexConsumers();

        // Draw search query
        textRenderer.draw(matrices, Text.of("Search: " + searchQuery), 10f, 10f, 0xFFFFFF, false,
                matrices.peek().getPositionMatrix(), vertexConsumers, TextLayerType.NORMAL, 0, 15728880);

        // Draw target types
        for (TargetType type : getVisibleTargets()) {
            boolean enabled = isEnabled(type);
            String labelStr = (enabled ? "[X] " : "[ ] ") + type.name();
            textRenderer.draw(matrices, Text.of(labelStr), 10f, (float)y, 0xFFFFFF, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextLayerType.NORMAL, 0, 15728880);
            y += 12;
        }

        vertexConsumers.draw(); // flush the buffer
    }

    public void handleClick(double mouseX, double mouseY) {
        int y = 20;
        for (TargetType type : getVisibleTargets()) {
            if (mouseX >= 10 && mouseX <= 110 && mouseY >= y && mouseY <= y + 10) {
                toggle(type);
            }
            y += 12;
        }
    }
}
