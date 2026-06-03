package com.byteoverlay.utils;

import java.util.HashMap;
import java.util.Map;

public class ColorUtils {
    private static final Map<String, String> hexToMine = new HashMap<>();

    static {
        hexToMine.put("#000000", "0");
        hexToMine.put("#0000aa", "1");
        hexToMine.put("#00aa00", "2");
        hexToMine.put("#00aaaa", "3");
        hexToMine.put("#aa0000", "4");
        hexToMine.put("#aa00aa", "5");
        hexToMine.put("#ffaa00", "6");
        hexToMine.put("#aaaaaa", "7");
        hexToMine.put("#555555", "8");
        hexToMine.put("#5555ff", "9");
        hexToMine.put("#55ff55", "a");
        hexToMine.put("#55ffff", "b");
        hexToMine.put("#ff5555", "c");
        hexToMine.put("#ff55ff", "d");
        hexToMine.put("#ffff55", "e");
        hexToMine.put("#ffffff", "f");
    }

    public static String convertHexToMine(String hex) {
        if (hex == null) return "7";
        hex = hex.toLowerCase();
        
        String bestMatch = "7";
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<String, String> entry : hexToMine.entrySet()) {
            double distance = getColorDistance(hex, entry.getKey());
            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = entry.getValue();
            }
        }
        return bestMatch;
    }

    private static double getColorDistance(String hex1, String hex2) {
        int r1 = Integer.valueOf(hex1.substring(1, 3), 16);
        int g1 = Integer.valueOf(hex1.substring(3, 5), 16);
        int b1 = Integer.valueOf(hex1.substring(5, 7), 16);

        int r2 = Integer.valueOf(hex2.substring(1, 3), 16);
        int g2 = Integer.valueOf(hex2.substring(3, 5), 16);
        int b2 = Integer.valueOf(hex2.substring(5, 7), 16);

        return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
    }
}
