package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.gui.items.buttons.BindButton;
import me.alpha432.oyvey.features.gui.items.buttons.EntityListButton;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class Killaura extends Module {

    public Setting<Float> range = this.register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public BindButton bindButton;
    public EntityListButton entityListButton;
    public String targetEntity = "Player";

    public Killaura() {
        super("Killaura", "Automatically attacks entities.", Category.COMBAT);

        // Keybind
        this.bindButton = new BindButton(this.getBind());

        // Entity selector
        this.entityListButton = new EntityListButton(this);
    }

    @Override
    public void onUpdate() {
        if (!isEnabled() || mc.player == null || mc.world == null) return;

        // Find closest target
        LivingEntity target = mc.world.getEntitiesByClass(LivingEntity.class, mc.player.getBoundingBox().expand(range.getValue()), e -> {
            if (targetEntity.equals("Player")) return e instanceof PlayerEntity && e != mc.player;
            if (targetEntity.equals("Phantom")) return e.getType().getName().getString().equalsIgnoreCase("phantom");
            return false;
        }).stream().min((e1, e2) -> Double.compare(mc.player.squaredDistanceTo(e1), mc.player.squaredDistanceTo(e2))).orElse(null);

        if (target != null && mc.player.getAttackCooldownProgress(0f) >= 1.0f) {
            // Smoothly look at the target
            Vec3d eyes = mc.player.getCameraPosVec(1.0f);
            Vec3d targetVec = target.getPos().add(0, target.getHeight() / 2, 0).subtract(eyes);
            float yaw = (float) Math.toDegrees(Math.atan2(targetVec.z, targetVec.x)) - 90;
            float pitch = (float) -Math.toDegrees(Math.atan2(targetVec.y, Math.sqrt(targetVec.x * targetVec.x + targetVec.z * targetVec.z)));
            mc.player.setYaw(mc.player.getYaw() + (yaw - mc.player.getYaw()) * 0.3f);
            mc.player.setPitch(mc.player.getPitch() + (pitch - mc.player.getPitch()) * 0.3f);

            // Attack
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.player.attack(target);
        }
    }
}
