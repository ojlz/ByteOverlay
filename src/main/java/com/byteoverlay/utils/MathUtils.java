package com.byteoverlay.utils;

import java.text.DecimalFormat;
import java.util.Locale;

public class MathUtils {
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String format(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
