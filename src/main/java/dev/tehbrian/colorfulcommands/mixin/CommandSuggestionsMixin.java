package dev.tehbrian.colorfulcommands.mixin;

import dev.tehbrian.colorfulcommands.ColorfulCommands;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.network.chat.Style;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CommandSuggestions.class)
public class CommandSuggestionsMixin {

    @Redirect(
            method = "formatText",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/CommandSuggestions;UNPARSED_STYLE:Lnet/minecraft/network/chat/Style;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static Style unparsedStyle() {
        return ColorfulCommands.get().config().literalStyle();
    }

    @Redirect(
            method = "formatText",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/CommandSuggestions;LITERAL_STYLE:Lnet/minecraft/network/chat/Style;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static Style literalStyle() {
        return ColorfulCommands.get().config().unparsedStyle();
    }

    @Redirect(
            method = "formatText",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/CommandSuggestions;ARGUMENT_STYLES:Ljava/util/List;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static List<Style> argumentStyles() {
        return ColorfulCommands.get().config().argumentStyles();
    }

}
