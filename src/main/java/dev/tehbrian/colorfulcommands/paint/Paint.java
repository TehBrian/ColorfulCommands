package dev.tehbrian.colorfulcommands.paint;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;

import java.awt.Color;
import java.util.Locale;
import java.util.Objects;

/**
 * The Color class to rule them all. Because no one can decide on a standard.
 */
public class Paint {

    private final Color color;

    private Paint(final Color color) {
        this.color = color;
    }

    public static Paint of(final Color color) {
        return new Paint(color);
    }

    public static Paint of(final int rgb) {
        return new Paint(new Color(rgb));
    }

    public static Paint of(final TextColor textColor) {
        return Paint.of(textColor.getValue());
    }

    public static Paint of(final ChatFormatting chatFormatting) {
        return Paint.of(Objects.requireNonNull(TextColor.fromLegacyFormat(chatFormatting)));
    }

    public static Paint of(final String hex) {
        return Paint.of(Integer.decode("0x" + hex));
    }

    public Color color() {
        return this.color;
    }

    public int rgb() {
        // the bitmask tosses the alpha, which we do not want.
        return this.color().getRGB() & 0x00_ff_ff_ff;
    }

    public TextColor textColor() {
        return TextColor.fromRgb(this.rgb());
    }

    public String hex() {
        return Integer.toHexString(this.rgb()).toUpperCase(Locale.ROOT);
    }

}
