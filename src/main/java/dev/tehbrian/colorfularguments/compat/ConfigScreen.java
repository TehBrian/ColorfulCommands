package dev.tehbrian.colorfularguments.compat;

import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.tehbrian.colorfularguments.ColorfulArguments;
import dev.tehbrian.colorfularguments.config.Config;
import dev.tehbrian.colorfularguments.util.Colors;
import dev.tehbrian.colorfularguments.util.DefaultColors;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.util.List;

public class ConfigScreen {

    public static Screen generate(final Screen parent) {
        final Config config = ColorfulArguments.get().config();

        final Option<Color> unparsedOption = Option.<Color>createBuilder()
                .name(Component.literal("Unparsed Color"))
                .description(OptionDescription.of(Component.literal("Color used for unparsed text.")))
                .binding(Colors.textColorToColor(DefaultColors.UNPARSED),
                        () -> Colors.textColorToColor(config.unparsedColor()),
                        val -> config.unparsedColor(Colors.colorToTextColor(val)))
                .controller(ColorControllerBuilder::create)
                .build();

        final Option<Color> literalOption = Option.<Color>createBuilder()
                .name(Component.literal("Literal Color"))
                .description(OptionDescription.of(Component.literal("Color used for literal text.")))
                .binding(Colors.textColorToColor(DefaultColors.LITERAL),
                        () -> Colors.textColorToColor(config.literalColor()),
                        val -> config.literalColor(Colors.colorToTextColor(val)))
                .controller(ColorControllerBuilder::create)
                .build();

        final ListOption<Color> argumentOption = ListOption.<Color>createBuilder()
                .name(Component.literal("Argument Colors"))
                .description(OptionDescription.of(Component.literal("Colors used for argument text.")))
                .binding(DefaultColors.ARGUMENT.stream().map(Colors::textColorToColor).toList(),
                        () -> config.argumentColors().stream().map(Colors::textColorToColor).toList(),
                        val -> config.argumentColors(val.stream().map(Colors::colorToTextColor).toList()))
                .controller(ColorControllerBuilder::create)
                .initial(Color.WHITE)
                .build();

        final var load = new Load();

        load.presetDropdown = Option.<String>createBuilder()
                .name(Component.literal("Presets"))
                .description(OptionDescription.of(Component.literal("Select a preset to load.")))
                .binding("",
                        () -> "",
                        val -> {
                        })
                .controller(option -> DropdownStringControllerBuilder.create(option).allowEmptyValue(true)
                        .values(List.of("1", "2", "omfg I'm a string", "5")))
                .listener((option, value) -> {
                    if (load.button != null) {
                        load.button.setAvailable(!value.isEmpty());
                    }
                })
                .build();

        load.button = ButtonOption.createBuilder()
                .name(Component.literal("Load"))
                .text(Component.empty())
                .description(OptionDescription.of(Component.literal("Load the selected preset.")))
                .action((screen, option) -> ColorfulArguments.logger().info(load.presetDropdown.pendingValue()))
                .available(false)
                .build();

        final var save = new Save();

        save.presetOption = Option.<String>createBuilder()
                .name(Component.literal("Preset Name"))
                .description(OptionDescription.of(Component.literal("The name of the preset to save.\n\nIf a preset by this name already exists, it will be overwritten.")))
                .binding("",
                        () -> "",
                        val -> {
                        })
                .controller(StringControllerBuilder::create)
                .listener((option, value) -> {
                    if (save.button != null) {
                        save.button.setAvailable(!value.isEmpty());
                    }
                })
                .build();

        save.button = ButtonOption.createBuilder()
                .name(Component.literal("Save"))
                .text(Component.empty())
                .description(OptionDescription.of(Component.literal("Save a new preset with the given name.")))
                .action((screen, option) -> ColorfulArguments.logger().info(save.presetOption.pendingValue()))
                .available(false)
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Colorful Arguments Configuration"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Colorful Arguments Configuration"))
                        .tooltip(Component.literal("All configuration for Colorful Arguments."))
                        .option(unparsedOption)
                        .option(literalOption)
                        .option(argumentOption)
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Load Preset"))
                                .description(OptionDescription.of(Component.literal("Load your saved presets.")))
                                .option(load.presetDropdown)
                                .option(load.button)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Save Preset"))
                                .description(OptionDescription.of(Component.literal("Save a preset for later use.")))
                                .option(save.presetOption)
                                .option(save.button)
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }

    private static class Load {
        Option<String> presetDropdown = null;
        ButtonOption button = null;
    }

    private static class Save {
        Option<String> presetOption = null;
        ButtonOption button = null;
    }

}
