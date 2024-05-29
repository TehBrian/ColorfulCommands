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
        return new Paint(new Color(textColor.getValue()));
    }

    public static Paint of(final String hex) {
        return new Paint(new Color(Integer.decode("0x" + hex)));
    }

    public static Paint of(final ChatFormatting chatFormatting) {
        return Paint.of(Objects.requireNonNull(TextColor.fromLegacyFormat(chatFormatting)));
    }

    public Color toColor() {
        return this.color;
    }

    public int toRgb() {
        return this.toColor().getRGB();
    }

    public TextColor toTextColor() {
        return TextColor.fromRgb(this.color.getRGB());
    }

    public String toHex() {
        return Integer.toHexString(this.color.getRGB()).toUpperCase(Locale.ROOT);
    }

}
