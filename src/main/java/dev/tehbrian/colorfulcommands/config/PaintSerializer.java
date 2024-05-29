package dev.tehbrian.colorfulcommands.config;

import dev.tehbrian.colorfulcommands.paint.Paint;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class PaintSerializer implements TypeSerializer<Paint> {

    public static final PaintSerializer INSTANCE = new PaintSerializer();

    private PaintSerializer() {
    }

    @Override
    public @Nullable Paint deserialize(final Type type, final ConfigurationNode source) {
        if (source.isNull()) {
            return null;
        }
        return Paint.of(source.getString());
    }

    @Override
    public void serialize(final Type type, final @Nullable Paint paint, final ConfigurationNode target) throws SerializationException {
        if (paint == null) {
            target.raw(null);
            return;
        }
        target.set(paint.hex());
    }

}
