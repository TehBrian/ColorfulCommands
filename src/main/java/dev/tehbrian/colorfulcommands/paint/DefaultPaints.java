package dev.tehbrian.colorfulcommands.paint;

import net.minecraft.ChatFormatting;

import java.util.List;
import java.util.stream.Stream;

public class DefaultPaints {

    // hardcoded default styles as per 1.20.6.
    public static final Paint UNPARSED = Paint.of(ChatFormatting.RED);
    public static final Paint LITERAL = Paint.of(ChatFormatting.GRAY);
    public static final List<Paint> ARGUMENT = Stream.of(ChatFormatting.AQUA, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD).map(Paint::of).toList();

}
