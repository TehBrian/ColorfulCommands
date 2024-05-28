package dev.tehbrian.colorfularguments;

import net.minecraft.text.TextColor;

import java.util.Locale;

public final class Color {

    public static TextColor hexToTextColor(final String hex) {
        return TextColor.fromRgb(Integer.decode("0x" + hex));
    }

    public static String textColorToHex(final TextColor color) {
        return Integer.toHexString(color.getRgb()).toUpperCase(Locale.ROOT);
    }

}
