package dev.tehbrian.colorfularguments.compat;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.tehbrian.colorfularguments.ColorfulArguments;
import dev.tehbrian.colorfularguments.config.Config;
import dev.tehbrian.colorfularguments.util.Colors;
import dev.tehbrian.colorfularguments.util.DefaultColors;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public class ConfigScreen {

    public static Screen generate(final Screen parent) {
        final Config config = ColorfulArguments.get().config();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Colorful Arguments Configuration"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Main Configuration"))
                        .tooltip(Component.literal("All configuration for Colorful Arguments."))
                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Unparsed Color"))
                                .description(OptionDescription.of(Component.literal("Color used for unparsed text.")))
                                .binding(Colors.textColorToColor(DefaultColors.UNPARSED),
                                        () -> Colors.textColorToColor(config.unparsedColor()),
                                        val -> config.unparsedColor(Colors.colorToTextColor(val)))
                                .controller(ColorControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Literal Color"))
                                .description(OptionDescription.of(Component.literal("Color used for literal text.")))
                                .binding(Colors.textColorToColor(DefaultColors.LITERAL),
                                        () -> Colors.textColorToColor(config.literalColor()),
                                        val -> config.literalColor(Colors.colorToTextColor(val)))
                                .controller(ColorControllerBuilder::create)
                                .build())
                        .option(ListOption.<Color>createBuilder()
                                .name(Component.literal("Argument Colors"))
                                .description(OptionDescription.of(Component.literal("Colors used for argument text.")))
                                .binding(DefaultColors.ARGUMENT.stream().map(Colors::textColorToColor).toList(),
                                        () -> config.argumentColors().stream().map(Colors::textColorToColor).toList(),
                                        val -> config.argumentColors(val.stream().map(Colors::colorToTextColor).toList()))
                                .controller(ColorControllerBuilder::create)
                                .initial(Color.WHITE)
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }

}
