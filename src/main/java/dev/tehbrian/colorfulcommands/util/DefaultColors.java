package dev.tehbrian.colorfulcommands.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DefaultColors {

    // hardcoded default styles as per 1.20.6.
    public static final @NonNull TextColor UNPARSED = Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.RED));
    public static final @NonNull TextColor LITERAL = Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.GRAY));
    public static final List<@NonNull TextColor> ARGUMENT = Stream.of(ChatFormatting.AQUA, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD)
            .map(TextColor::fromLegacyFormat).map(Objects::requireNonNull).toList();

}
