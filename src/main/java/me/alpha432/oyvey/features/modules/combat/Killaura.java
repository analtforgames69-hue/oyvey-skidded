package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.features.settings.Bind;
import me.alpha432.oyvey.features.gui.items.buttons.BindButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.List;

public class Killaura extends Module {

    public Setting<Bind> bind = register(new Setting<>("Keybind", new Bind(-1)));
    public Setting<TargetMode> targetMode = register(new Setting<>("Target", TargetMode.Players));

    private BindButton bindButton;

    public Killaura() {
        super("Killaura", "Automatically attacks nearby entities", Category.COMBAT, true, false, false);
        bindButton = new BindButton(bind);
    }

    public enum TargetMode {
        Players,
        Phantoms
    }

    @Override
    public void onUpdate() {
        if (!this.isEnabled()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        World world = MinecraftClient.getInstance().world;

        if (player == null || world == null) return;

        float cooldown = player.getAttackCooldownProgress(0f);
        if (cooldown < 1.0f) return; // Only attack when cooldown is full

        List<Entity> entities = world.getEntitiesByClass(Entity.class, new Box(
                player.getX() - 4, player.getY() - 4, player.getZ() - 4,
                player.getX() + 4, player.getY() + 4, player.getZ() + 4), 
                e -> e != player && isValidTarget(e)
        );

        for (Entity target : entities) {
            attack(target, player);
        }
    }

    private boolean isValidTarget(Entity entity) {
        switch (targetMode.getValue()) {
            case Players:
                return entity instanceof PlayerEntity;
            case Phantoms:
                return entity instanceof PhantomEntity;
        }
        return false;
    }

    private void attack(Entity target, ClientPlayerEntity player) {
        player.attack(target);
        player.swingHand(Hand.MAIN_HAND);
    }

    public BindButton getBindButton() {
        return bindButton;
    }
}
