package dev.tehbrian.colorfularguments.config;

import dev.tehbrian.colorfularguments.Color;
import dev.tehbrian.colorfularguments.ColorfulArguments;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class ClothConfigMenu {

    public static Screen generateScreen(final Screen parent) {
        final Config config = ColorfulArguments.get().config();

        final ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("ColorfulArguments Config"))
                .setSavingRunnable(() -> {
                }); //onConfigChanged !!!

        final ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        final ConfigCategory active = builder.getOrCreateCategory(Text.of("Active Colors"));

        active.addEntry(
                entryBuilder.startColorField(
                                Text.of("Info Color"),
                                config.infoColor()
                        )
                        .setDefaultValue(() -> config.infoColor().getRgb())
                        .setSaveConsumer(v -> config.infoColor(TextColor.fromRgb(v)))
                        .build()
        );

        active.addEntry(
                entryBuilder.startColorField(
                                Text.of("Error Color"),
                                config.errorColor()
                        )
                        .setDefaultValue(() -> config.errorColor().getRgb())
                        .setSaveConsumer(v -> config.errorColor(TextColor.fromRgb(v)))
                        .build()
        );

        active.addEntry(
                entryBuilder.startStrList(
                        Text.of("Highlight Colors"),
                        config.highlightColors().stream().map(TextColor::getRgb).map(Object::toString).toList()
                )
                        .setDefaultValue(() -> config.highlightColors().stream().map(TextColor::getRgb).map(Object::toString).toList())
                        .setSaveConsumer(v -> config.highlightColors(v.stream().map(Color::hexToTextColor).toList()))
                        .build()
        );

        final ConfigCategory presets = builder.getOrCreateCategory(Text.of("Presets"));

        return builder.build();
    }

}
