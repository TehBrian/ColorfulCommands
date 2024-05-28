package dev.tehbrian.colorfularguments.util;

import net.minecraft.network.chat.TextColor;

import java.awt.Color;
import java.util.Locale;

public final class Colors {

    public static TextColor hexToTextColor(final String hex) {
        return TextColor.fromRgb(Integer.decode("0x" + hex));
    }

    public static String textColorToHex(final TextColor textColor) {
        return Integer.toHexString(textColor.getValue()).toUpperCase(Locale.ROOT);
    }

    public static TextColor colorToTextColor(final Color color) {
        return TextColor.fromRgb(color.getRGB());
    }

    public static Color textColorToColor(final TextColor textColor) {
        return new Color(textColor.getValue());
    }

}
