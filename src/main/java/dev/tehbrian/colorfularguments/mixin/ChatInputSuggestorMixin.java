package dev.tehbrian.colorfularguments.mixin;

import dev.tehbrian.colorfularguments.ColorfulArguments;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.network.chat.Style;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CommandSuggestions.class)
public class ChatInputSuggestorMixin {

    @Redirect(
            method = "formatText",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/CommandSuggestions;UNPARSED_STYLE:Lnet/minecraft/network/chat/Style;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static Style unparsedStyle() {
        return ColorfulArguments.get().config().literalStyle();
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
        return ColorfulArguments.get().config().unparsedStyle();
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
        return ColorfulArguments.get().config().argumentStyles();
    }

}
