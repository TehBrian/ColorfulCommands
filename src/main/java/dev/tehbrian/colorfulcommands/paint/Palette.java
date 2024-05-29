package dev.tehbrian.colorfulcommands.paint;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public record Palette(Paint unparsedPaint,
                      Paint literalPaint,
                      List<Paint> argumentPaints
) {

}