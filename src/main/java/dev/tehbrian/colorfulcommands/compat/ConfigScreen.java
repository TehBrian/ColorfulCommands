package dev.tehbrian.colorfulcommands.compat;

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
import dev.tehbrian.colorfulcommands.ColorfulCommands;
import dev.tehbrian.colorfulcommands.config.Config;
import dev.tehbrian.colorfulcommands.paint.DefaultPaints;
import dev.tehbrian.colorfulcommands.paint.Paint;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.util.List;

public class ConfigScreen {

    public static Screen generate(final Screen parent) {
        final Config config = ColorfulCommands.get().config();

        final Option<Color> unparsedOption = Option.<Color>createBuilder()
                .name(Component.literal("Unparsed Color"))
                .description(OptionDescription.of(Component.literal("Color used for unparsed text.")))
                .binding(DefaultPaints.UNPARSED.toColor(),
                        () -> config.unparsedPaint().toColor(),
                        val -> config.unparsedPaint(Paint.of(val)))
                .controller(ColorControllerBuilder::create)
                .build();

        final Option<Color> literalOption = Option.<Color>createBuilder()
                .name(Component.literal("Literal Color"))
                .description(OptionDescription.of(Component.literal("Color used for literal text.")))
                .binding(DefaultPaints.LITERAL.toColor(),
                        () -> config.literalPaint().toColor(),
                        val -> config.literalPaint(Paint.of(val)))
                .controller(ColorControllerBuilder::create)
                .build();

        final ListOption<Color> argumentOption = ListOption.<Color>createBuilder()
                .name(Component.literal("Argument Colors"))
                .description(OptionDescription.of(Component.literal("Colors used for argument text.")))
                .binding(DefaultPaints.ARGUMENT.stream().map(Paint::toColor).toList(),
                        () -> config.argumentPaints().stream().map(Paint::toColor).toList(),
                        val -> config.argumentPaints(val.stream().map(Paint::of).toList()))
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
                .action((screen, option) -> {
                    ColorfulCommands.logger().info(load.presetDropdown.pendingValue());
                    load.presetDropdown.forgetPendingValue();
                })
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
                .action((screen, option) -> {
                    ColorfulCommands.logger().info(save.presetOption.pendingValue());
                    save.presetOption.forgetPendingValue();
                })
                .available(false)
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Colorful Commands Configuration"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Colorful Commands Configuration"))
                        .tooltip(Component.literal("All configuration for Colorful Commands."))
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Simple Colors"))
                                .description(OptionDescription.of(Component.literal("Simple, non-alternating colors.")))
                                .option(unparsedOption)
                                .option(literalOption)
                                .build())
                        // list option: can't be in a group.
                        .option(argumentOption)
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Load Preset"))
                                .description(OptionDescription.of(Component.literal("Load a saved preset.")))
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
