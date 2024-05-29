package dev.tehbrian.colorfulcommands.config;

import dev.tehbrian.colorfulcommands.paint.Default;
import dev.tehbrian.colorfulcommands.paint.Paint;
import dev.tehbrian.colorfulcommands.paint.Palette;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Style;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Config {

    private static final String HEADER = """
            == ColorfulCommands ==
            Made by TehBrian!! ;D
                        
            Modrinth: https://modrinth.com/mod/ColorfulCommands
            GitHub:   https://github.com/TehBrian/ColorfulCommands
            Discord:  https://thbn.me/discord
                        
            The colors are saved in HEX format (without the #).
            """;

    private final HoconConfigurationLoader loader;
    private CommentedConfigurationNode rootNode;

    private Paint unparsedPaint = Default.UNPARSED_PAINT;
    private Paint literalPaint = Default.LITERAL_PAINT;
    private List<Paint> argumentPaints = Default.ARGUMENT_PAINTS;
    private Map<String, Palette> presets = Default.PRESETS;

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

    public Paint unparsedPaint() {
        return this.unparsedPaint;
    }

    public void unparsedPaint(final Paint paint) {
        if (paint == null) {
            this.unparsedPaint = Default.UNPARSED_PAINT;
        } else {
            this.unparsedPaint = paint;
        }
        this.unparsedStyle = Style.EMPTY.withColor(this.unparsedPaint().textColor());
    }

    public Paint literalPaint() {
        return this.literalPaint;
    }

    public void literalPaint(final Paint paint) {
        if (paint == null) {
            this.literalPaint = Default.LITERAL_PAINT;
        } else {
            this.literalPaint = paint;
        }
        this.literalStyle = Style.EMPTY.withColor(this.literalPaint().textColor());
    }

    public List<Paint> argumentPaints() {
        return this.argumentPaints;
    }

    public void argumentPaints(final List<Paint> paints) {
        if (paints == null || paints.isEmpty()) {
            this.argumentPaints = Default.ARGUMENT_PAINTS;
        } else {
            this.argumentPaints = paints;
        }
        this.argumentStyles = this.argumentPaints().stream().map(Paint::textColor).map(Style.EMPTY::withColor).toList();
    }

    public Map<String, Palette> presets() {
        return this.presets;
    }

    public void presets(final Map<String, Palette> presets) {
        if (presets == null || presets.isEmpty()) {
            this.presets = Default.PRESETS;
        } else {
            this.presets = presets;
        }
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

    public void save() throws ConfigurateException {
        final Data data = new Data(new Palette(
                this.literalPaint(),
                this.unparsedPaint(),
                this.argumentPaints()
        ), this.presets());

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
        this.presets(data.presets());
    }

    @ConfigSerializable
    private record Data(Palette active,
                        Map<String, Palette> presets
    ) {

    }

}
