package dev.tehbrian.colorfulcommands.paint;

import net.minecraft.ChatFormatting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Default {

    // hardcoded Minecraft default colors as per 1.20.6.
    public static final Paint UNPARSED_PAINT = Paint.of(ChatFormatting.RED);
    public static final Paint LITERAL_PAINT = Paint.of(ChatFormatting.GRAY);
    public static final List<Paint> ARGUMENT_PAINTS = Stream.of(ChatFormatting.AQUA, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD).map(Paint::of).toList();

    // pretty palettes I chose myself.
    // new HashMap<> so that the map is mutable; we mutate it
    // when saving palettes (as opposed to creating new maps).
    public static final Map<String, Palette> PRESETS = new HashMap<>(Map.of(
            "Cotton Candy", new Palette(
                    Paint.of("FF5555"),
                    Paint.of("AAAAAA"),
                    List.of(Paint.of("5BCEFA"), Paint.of("F5A9B8"), Paint.of("FFFFFF"), Paint.of("F5A9B8"), Paint.of("5BCEFA"))
            ),
            "Synthwave", new Palette(
                    Paint.of("FF5555"),
                    Paint.of("AAAAAA"),
                    List.of(Paint.of("D60270"), Paint.of("9B4F96"), Paint.of("0038A8"))
            ),
            "Sunset", new Palette(
                    Paint.of("FF5555"),
                    Paint.of("AAAAAA"),
                    List.of(Paint.of("D52D00"), Paint.of("EF7627"), Paint.of("FF9A56"), Paint.of("FFFFFF"), Paint.of("D162A4"), Paint.of("B55690"), Paint.of("A30262"))
            ),
            "Hackerman", new Palette(
                    Paint.of("FF0000"),
                    Paint.of("FFFFFF"),
                    List.of(Paint.of("00FF00"))
            ),
            "The Olden Days", new Palette(
                    Paint.of("FFFFFF"),
                    Paint.of("FFFFFF"),
                    List.of(Paint.of("FFFFFF"))
            )
    ));

}
