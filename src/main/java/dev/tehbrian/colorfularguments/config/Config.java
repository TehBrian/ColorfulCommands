package dev.tehbrian.colorfularguments.config;

import dev.tehbrian.colorfularguments.Color;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static dev.tehbrian.colorfularguments.Color.hexToTextColor;
import static dev.tehbrian.colorfularguments.Color.textColorToHex;

public class Config {

    private static final String HEADER = """
            == ColorfulArguments ==
            Made by TehBrian!! ;D
            
            Discord:  https://thbn.me/discord
            GitHub:   https://github.com/TehBrian/ColorfulArguments
            Modrinth: https://modrinth.com/mod/ColorfulArguments
            
            Below are a few hand-picked presets.

            -- Minecraft Default --
            info-color: "AAAAAA"
            error-color: "FF5555"
            highlight-colors: [ "55FFFF", "FFFF55", "55FF55", "FF55FF", "FFAA00" ]
            
            -- Cotton Candy --
            highlight-colors: [ "5BCEFA", "F5A9B8", "FFFFFF", "F5A9B8", "5BCEFA" ]
            
            -- Synthwave --
            highlight-colors: [ "D60270", "9B4F96", "0038A8" ]
            
            -- Sunset --
            highlight-colors: [ "D52D00", "EF7627", "FF9A56", "FFFFFF", "D162A4", "B55690", "A30262" ]
            
            -- Hackerman --
            info-color: "FFFFFF"
            error-color: "FF0000"
            highlight-colors: [ "00FF00" ]
            
            -- Pre 1.13 --
            info-color: "FFFFFF"
            error-color: "FFFFFF"
            highlight-colors: [ "FFFFFF" ]
            """;

    private final HoconConfigurationLoader loader;
    private CommentedConfigurationNode rootNode;

    // hardcoded default styles as per 1.20.6.
    // used as fallback prior to config initialization.
    private TextColor infoColor = TextColor.fromFormatting(Formatting.GRAY);
    private TextColor errorColor = TextColor.fromFormatting(Formatting.RED);
    private List<TextColor> highlightColors = Stream.of(Formatting.AQUA, Formatting.YELLOW, Formatting.GREEN, Formatting.LIGHT_PURPLE, Formatting.GOLD).map(TextColor::fromFormatting).toList();

    // styles are cached (set at config load) rather
    // than re-generated every xStyle() call.
    private Style infoStyle = Style.EMPTY.withColor(infoColor);
    private Style errorStyle = Style.EMPTY.withColor(errorColor);
    private List<Style> highlightStyles = highlightColors.stream().map(Style.EMPTY::withColor).toList();

    public Config() {
        this.loader = HoconConfigurationLoader.builder()
                .headerMode(HeaderMode.PRESET)
                .defaultOptions(ConfigurationOptions.defaults().header(HEADER))
                .emitJsonCompatible(true)
                .path(FabricLoader.getInstance().getConfigDir().resolve("colorfularguments.conf"))
                .build();
    }

    public Style infoStyle() {
        return this.infoStyle;
    }

    public Style errorStyle() {
        return this.errorStyle;
    }

    public List<Style> highlightStyles() {
        return this.highlightStyles;
    }

    public TextColor infoColor() {
        return this.infoColor;
    }

    public TextColor errorColor() {
        return this.errorColor;
    }

    public List<TextColor> highlightColors() {
        return this.highlightColors;
    }

    public void infoColor(final TextColor color) {
        this.infoColor = color;
        this.infoStyle = Style.EMPTY.withColor(this.infoColor);
    }

    public void errorColor(final TextColor color) {
        this.errorColor = color;
        this.errorStyle = Style.EMPTY.withColor(this.errorColor);
    }

    public void highlightColors(final List<TextColor> colors) {
        this.highlightColors = colors;
        this.highlightStyles = this.highlightColors.stream().map(Style.EMPTY::withColor).toList();
    }

    public void save() throws ConfigurateException {
        final Data data = new Data(
                textColorToHex(this.infoColor()),
                textColorToHex(this.errorColor()),
                this.highlightColors().stream().map(Color::textColorToHex).toList()
        );

        this.rootNode.set(Data.class, data);
        this.loader.save(this.rootNode);
    }

    public void load() throws ConfigurateException, NullPointerException, NumberFormatException {
        final Data data;
        this.rootNode = this.loader.load();
        data = Objects.requireNonNull(this.rootNode.get(Data.class));

        this.infoColor(hexToTextColor(data.infoColor()));
        this.errorColor(hexToTextColor(data.errorColor()));
        this.highlightColors(data.highlightColors().stream().map(Color::hexToTextColor).toList());
    }

    @ConfigSerializable
    private record Data(String infoColor,
                        String errorColor,
                        List<String> highlightColors) {

    }

}
