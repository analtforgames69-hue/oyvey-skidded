package me.alpha432.oyvey.features.modules.combat;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.models.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class Killaura extends Module {
    private final Timer timer = new Timer();

    public Killaura() {
        super("Killaura", "Automatically attacks entities around you", Category.COMBAT, true, false, false);
    }

    public Setting<Float> range = register(new Setting<>("Range", 4.0f, 1.0f, 6.0f));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public Setting<Integer> cps = register(new Setting<>("CPS", 10, 1, 20));

    private PlayerEntity getTarget() {
        if (mc.world == null || mc.player == null) return null;
        return mc.world.getEntitiesByClass(PlayerEntity.class, mc.player.getBoundingBox().expand(range.get()), e -> e != mc.player && !e.isDead())
                .stream()
                .min(Comparator.comparingDouble(e -> mc.player.squaredDistanceTo(e)))
                .orElse(null);
    }

    private void attack(PlayerEntity target) {
        if (target == null || !timer.passedMs(1000 / cps.get())) return;

        if (rotate.get()) faceEntity(target);

        mc.player.networkHandler.sendPacket(new PlayerInteractEntityC2SPacket(target, Hand.MAIN_HAND));
        mc.player.swingHand(Hand.MAIN_HAND);
        timer.reset();
    }

    private void faceEntity(Entity entity) {
        if (mc.player == null || entity == null) return;

        Vec3d diff = entity.getPos().subtract(mc.player.getPos());
        float yaw = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(diff.y, Math.sqrt(diff.x * diff.x + diff.z * diff.z)));

        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    @Subscribe
    private void onPacketSend(PacketEvent.Send event) {
        PlayerEntity target = getTarget();
        attack(target);
    }

    @Override
    public String getDisplayInfo() {
        PlayerEntity target = getTarget();
        return target != null ? target.getName().getString() : null;
    }
}
