package dev.tehbrian.colorfularguments.mixin;

import dev.tehbrian.colorfularguments.ColorfulArguments;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.text.Style;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin {

    @Redirect(
            method = "highlight",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/ChatInputSuggestor;INFO_STYLE:Lnet/minecraft/text/Style;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static Style infoStyle() {
        return ColorfulArguments.get().config().infoStyle();
    }

    @Redirect(
            method = "highlight",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/ChatInputSuggestor;ERROR_STYLE:Lnet/minecraft/text/Style;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static Style errorStyle() {
        return ColorfulArguments.get().config().errorStyle();
    }

    @Redirect(
            method = "highlight",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/ChatInputSuggestor;HIGHLIGHT_STYLES:Ljava/util/List;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static List<Style> highlightStyles() {
        return ColorfulArguments.get().config().highlightStyles();
    }

}
