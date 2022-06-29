package fr.redboxing.survivalgame.utils;

public class MathHelper {
    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }
}
