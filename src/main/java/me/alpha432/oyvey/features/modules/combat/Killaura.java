package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.gui.KillauraEntitySelectorGUI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class Killaura extends Module {

    private final Minecraft mc = Minecraft.getInstance();
    public KillauraEntitySelectorGUI selectorGUI;

    public Killaura() {
        super("Killaura", "Attacks nearby entities automatically", Category.COMBAT);
        selectorGUI = new KillauraEntitySelectorGUI();
    }

    @Override
    public void onTick() {
        LocalPlayer player = mc.player;
        if (player == null || !player.isAlive()) return;

        // Get entities within range
        List<LivingEntity> targets = mc.level.entitiesForRendering(LivingEntity.class, entity ->
                entity != player && entity.isAlive() && mc.player.distanceToSqr(entity) <= 16
                        && isValidTarget(entity)
        );

        for (LivingEntity target : targets) {
            attackEntity(target);
        }
    }

    private boolean isValidTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        if (entity instanceof Player && !selectorGUI.isEnabled(TargetType.PLAYER)) return false;
        if (entity.getType().getRegistryName().getPath().contains("monster") && !selectorGUI.isEnabled(TargetType.MOB)) return false;
        if (entity.getType().getRegistryName().getPath().contains("animal") && !selectorGUI.isEnabled(TargetType.ANIMAL)) return false;
        return true;
    }

    private void attackEntity(LivingEntity entity) {
        mc.player.lookAt(entity); // Rotate toward target
        mc.player.swingHand(mc.player.getMainHandItem().getUseAnimation().ordinal()); // Animate swing
        mc.player.attack(entity); // Attack
    }
}
