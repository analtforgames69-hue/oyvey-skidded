package me.alpha432.oyvey.util;

public class MathUtil {

    public static float smoothRotation(float current, float target, float speed) {
        float delta = wrapDegrees(target - current);
        if (delta > speed) delta = speed;
        if (delta < -speed) delta = -speed;
        return current + delta;
    }

    public static float wrapDegrees(float value) {
        value = value % 360.0F;
        if (value >= 180.0F) value -= 360.0F;
        if (value < -180.0F) value += 360.0F;
        return value;
    }

    public static float[] calculateLookAt(double x, double y, double z, net.minecraft.entity.Entity fromEntity) {
        double diffX = x - fromEntity.getX();
        double diffY = y - (fromEntity.getY() + fromEntity.getEyeHeight(fromEntity.getPose()));
        double diffZ = z - fromEntity.getZ();
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, dist));
        return new float[]{yaw, pitch};
    }
}
