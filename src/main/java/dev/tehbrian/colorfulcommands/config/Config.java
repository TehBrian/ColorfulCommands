package dev.tehbrian.colorfulcommands.config;

import dev.tehbrian.colorfulcommands.paint.DefaultPaints;
import dev.tehbrian.colorfulcommands.paint.Paint;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Style;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Objects;

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

    private Paint unparsedPaint = DefaultPaints.UNPARSED;
    private Paint literalPaint = DefaultPaints.LITERAL;
    private List<Paint> argumentPaints = DefaultPaints.ARGUMENT;

    // styles are cached (set at config load) rather than re-generated every xStyle() call.
    private Style unparsedStyle = Style.EMPTY.withColor(this.unparsedPaint().textColor());
    private Style literalStyle = Style.EMPTY.withColor(this.literalPaint().textColor());
    private List<Style> argumentStyles = this.argumentPaints().stream().map(Paint::textColor).map(Style.EMPTY::withColor).toList();

    public Config() {
        this.loader = HoconConfigurationLoader.builder()
                .headerMode(HeaderMode.PRESET)
                .defaultOptions(ConfigurationOptions.defaults().header(HEADER))
                .emitJsonCompatible(true)
                .path(FabricLoader.getInstance().getConfigDir().resolve("colorfulcommands.conf"))
                .defaultOptions(opts -> opts.serializers(b -> b.register(Paint.class, PaintSerializer.INSTANCE)))
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

    public Paint unparsedPaint() {
        return this.unparsedPaint;
    }

    public Paint literalPaint() {
        return this.literalPaint;
    }

    public List<Paint> argumentPaints() {
        return this.argumentPaints;
    }

    public void unparsedPaint(final Paint paint) {
        this.unparsedPaint = paint;
        this.unparsedStyle = Style.EMPTY.withColor(this.unparsedPaint().textColor());
    }

    public void literalPaint(final Paint paint) {
        this.literalPaint = paint;
        this.literalStyle = Style.EMPTY.withColor(this.literalPaint().textColor());
    }

    public void argumentPaints(final List<Paint> paints) {
        this.argumentPaints = paints;
        this.argumentStyles = this.argumentPaints().stream().map(Paint::textColor).map(Style.EMPTY::withColor).toList();
    }

    public void save() throws ConfigurateException {
        final Data data = new Data(new Data.Palette(
                this.literalPaint(),
                this.unparsedPaint(),
                this.argumentPaints()
        ), List.of());

        this.rootNode.set(Data.class, data);
        this.loader.save(this.rootNode);
    }

    public void load() throws ConfigurateException, NullPointerException, NumberFormatException {
        final Data data;
        this.rootNode = this.loader.load();
        data = Objects.requireNonNull(this.rootNode.get(Data.class));

        this.literalPaint(data.active().literalPaint());
        this.unparsedPaint(data.active().unparsedPaint());
        this.argumentPaints(data.active().argumentPaints());
    }

    @ConfigSerializable
    private record Data(Palette active,
                        List<Palette> presets
    ) {

        @ConfigSerializable
        private record Palette(Paint unparsedPaint,
                               Paint literalPaint,
                               List<Paint> argumentPaints
        ) {

        }

    }

}
