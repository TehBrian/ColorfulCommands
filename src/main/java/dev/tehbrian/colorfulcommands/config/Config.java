package dev.tehbrian.colorfulcommands.config;

import dev.tehbrian.colorfulcommands.util.Colors;
import dev.tehbrian.colorfulcommands.util.DefaultColors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Objects;

import static dev.tehbrian.colorfulcommands.util.Colors.hexToTextColor;
import static dev.tehbrian.colorfulcommands.util.Colors.textColorToHex;

public class Config {

    private static final String HEADER = """
            == ColorfulCommands ==
            Made by TehBrian!! ;D
            
            Modrinth: https://modrinth.com/mod/ColorfulCommands
            GitHub:   https://github.com/TehBrian/ColorfulCommands
            Discord:  https://thbn.me/discord
            
            Below are a few hand-picked presets.

            -- Minecraft Default --
            unparsed-color: "FF5555"
            literal-color: "AAAAAA"
            argument-colors: [ "55FFFF", "FFFF55", "55FF55", "FF55FF", "FFAA00" ]
            
            -- Cotton Candy --
            argument-colors: [ "5BCEFA", "F5A9B8", "FFFFFF", "F5A9B8", "5BCEFA" ]
            
            -- Synthwave --
            argument-colors: [ "D60270", "9B4F96", "0038A8" ]
            
            -- Sunset --
            argument-colors: [ "D52D00", "EF7627", "FF9A56", "FFFFFF", "D162A4", "B55690", "A30262" ]
            
            -- Hackerman --
            unparsed-color: "FF0000"
            literal-color: "FFFFFF"
            argument-colors: [ "00FF00" ]
            
            -- Pre 1.13 --
            unparsed-color: "FFFFFF"
            literal-color: "FFFFFF"
            argument-colors: [ "FFFFFF" ]
            """;

    private final HoconConfigurationLoader loader;
    private CommentedConfigurationNode rootNode;

    private TextColor unparsedColor = DefaultColors.UNPARSED;
    private TextColor literalColor = DefaultColors.LITERAL;
    private List<TextColor> argumentColors = DefaultColors.ARGUMENT;

    // styles are cached (set at config load) rather than re-generated every xStyle() call.
    private Style unparsedStyle = Style.EMPTY.withColor(unparsedColor);
    private Style literalStyle = Style.EMPTY.withColor(literalColor);
    private List<Style> argumentStyles = argumentColors.stream().map(Style.EMPTY::withColor).toList();

    public Config() {
        this.loader = HoconConfigurationLoader.builder()
                .headerMode(HeaderMode.PRESET)
                .defaultOptions(ConfigurationOptions.defaults().header(HEADER))
                .emitJsonCompatible(true)
                .path(FabricLoader.getInstance().getConfigDir().resolve("colorfulcommands.conf"))
                .build();
    }

    public Style unparsedStyle() {
        return this.unparsedStyle;
    }

    public Style literalStyle() {
        return this.literalStyle;
    }

    public List<Style> argumentStyles() {
        return this.argumentStyles;
    }

    public TextColor unparsedColor() {
        return this.unparsedColor;
    }

    public TextColor literalColor() {
        return this.literalColor;
    }

    public List<TextColor> argumentColors() {
        return this.argumentColors;
    }

    public void unparsedColor(final TextColor color) {
        this.unparsedColor = color;
        this.unparsedStyle = Style.EMPTY.withColor(this.unparsedColor);
    }

    public void literalColor(final TextColor color) {
        this.literalColor = color;
        this.literalStyle = Style.EMPTY.withColor(this.literalColor);
    }

    public void argumentColors(final List<TextColor> colors) {
        this.argumentColors = colors;
        this.argumentStyles = this.argumentColors.stream().map(Style.EMPTY::withColor).toList();
    }

    public void save() throws ConfigurateException {
        final Data data = new Data(
                textColorToHex(this.literalColor()),
                textColorToHex(this.unparsedColor()),
                this.argumentColors().stream().map(Colors::textColorToHex).toList()
        );

        this.rootNode.set(Data.class, data);
        this.loader.save(this.rootNode);
    }

    public void load() throws ConfigurateException, NullPointerException, NumberFormatException {
        final Data data;
        this.rootNode = this.loader.load();
        data = Objects.requireNonNull(this.rootNode.get(Data.class));

        this.literalColor(hexToTextColor(data.literalColor()));
        this.unparsedColor(hexToTextColor(data.unparsedColor()));
        this.argumentColors(data.argumentColors().stream().map(Colors::hexToTextColor).toList());
    }

    @ConfigSerializable
    private record Data(String unparsedColor,
                        String literalColor,
                        List<String> argumentColors) {

    }

}
