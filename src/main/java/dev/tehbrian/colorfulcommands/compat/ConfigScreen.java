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
import dev.tehbrian.colorfulcommands.paint.Default;
import dev.tehbrian.colorfulcommands.paint.Paint;
import dev.tehbrian.colorfulcommands.paint.Palette;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.configurate.ConfigurateException;

import java.awt.Color;

public class ConfigScreen {

    private static MutableComponent tl(final String string) {
        return Component.translatable("colorfulcommands." + string);
    }

    public static Screen generate(final Screen parent) {
        final Config config = ColorfulCommands.get().config();

        final Option<Color> unparsedOption = Option.<Color>createBuilder()
                .name(tl("unparsed_color"))
                .description(OptionDescription.of(tl("unparsed_color_desc")))
                .binding(Default.UNPARSED_PAINT.color(),
                        () -> config.unparsedPaint().color(),
                        val -> config.unparsedPaint(Paint.of(val)))
                .controller(ColorControllerBuilder::create)
                .build();

        final Option<Color> literalOption = Option.<Color>createBuilder()
                .name(tl("literal_color"))
                .description(OptionDescription.of(tl("literal_color_desc")))
                .binding(Default.LITERAL_PAINT.color(),
                        () -> config.literalPaint().color(),
                        val -> config.literalPaint(Paint.of(val)))
                .controller(ColorControllerBuilder::create)
                .build();

        final ListOption<Color> argumentOption = ListOption.<Color>createBuilder()
                .name(tl("argument_colors"))
                .description(OptionDescription.of(tl("argument_colors_desc")))
                .binding(Default.ARGUMENT_PAINTS.stream().map(Paint::color).toList(),
                        () -> config.argumentPaints().stream().map(Paint::color).toList(),
                        val -> config.argumentPaints(val.stream().map(Paint::of).toList()))
                .controller(ColorControllerBuilder::create)
                .initial(Color.WHITE)
                .build();

        final var load = new Load();

        load.presetDropdown = Option.<String>createBuilder()
                .name(tl("load_presets"))
                .description(OptionDescription.of(tl("load_presets_desc")))
                .binding("",
                        () -> "",
                        val -> {
                        })
                .controller(option -> DropdownStringControllerBuilder.create(option)
                        .allowEmptyValue(true)
                        .values(config.presets().keySet().stream().sorted().toList()))
                .listener((option, value) -> {
                    if (load.button != null) {
                        load.button.setAvailable(!value.isEmpty());
                    }
                    if (load.deleteButton != null) {
                        load.deleteButton.setAvailable(!value.isEmpty());
                    }
                })
                .build();

        load.button = ButtonOption.createBuilder()
                .name(tl("load"))
                .text(Component.empty())
                .description(OptionDescription.of(tl("load_desc")))
                .action((screen, option) -> {
                    final var preset = config.presets().get(load.presetDropdown.pendingValue());
                    unparsedOption.requestSet(preset.unparsedPaint().color());
                    literalOption.requestSet(preset.literalPaint().color());
                    argumentOption.requestSet(preset.argumentPaints().stream().map(Paint::color).toList());
                    load.presetDropdown.forgetPendingValue();
                })
                .available(false)
                .build();

        load.deleteButton = ButtonOption.createBuilder()
                .name(tl("load_delete"))
                .text(Component.empty())
                .description(OptionDescription.of(tl("load_delete_desc")))
                .action((screen, option) -> {
                    config.presets().remove(load.presetDropdown.pendingValue());
                    load.presetDropdown.forgetPendingValue();
                })
                .available(false)
                .build();

        final var save = new Save();

        save.presetOption = Option.<String>createBuilder()
                .name(tl("save_preset"))
                .description(OptionDescription.of(tl("save_preset_desc")))
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
                .name(tl("save"))
                .text(Component.empty())
                .description(OptionDescription.of(tl("save_desc")))
                .action((screen, option) -> {
                    config.presets().put(save.presetOption.pendingValue(), new Palette(
                            Paint.of(unparsedOption.pendingValue()),
                            Paint.of(literalOption.pendingValue()),
                            argumentOption.pendingValue().stream().map(Paint::of).toList()
                    ));
                    save.presetOption.forgetPendingValue();
                })
                .available(false)
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(tl("title"))
                .save(() -> {
                    try {
                        config.save();
                    } catch (final ConfigurateException e) {
                        throw new RuntimeException(e);
                    }
                })
                .category(ConfigCategory.createBuilder()
                        .name(tl("root"))
                        .tooltip(tl("root_tooltip"))
                        .group(OptionGroup.createBuilder()
                                .name(tl("simple"))
                                .description(OptionDescription.of(tl("simple_tooltip")))
                                .option(unparsedOption)
                                .option(literalOption)
                                .build())
                        .group(argumentOption)
                        .group(OptionGroup.createBuilder()
                                .name(tl("load_group"))
                                .description(OptionDescription.of(tl("load_group_tooltip")))
                                .option(load.presetDropdown)
                                .option(load.button)
                                .option(load.deleteButton)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(tl("save_group"))
                                .description(OptionDescription.of(tl("save_group_tooltip")))
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
        ButtonOption deleteButton = null;
    }

    private static class Save {
        Option<String> presetOption = null;
        ButtonOption button = null;
    }

}
