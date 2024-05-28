package dev.tehbrian.colorfularguments.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

public class ModMenu implements ModMenuApi {

    private static final boolean CLOTH_CONFIG = FabricLoader.getInstance().isModLoaded("cloth-config2");

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        if (!CLOTH_CONFIG) {
            return parent -> null;
        } else {
            return ClothConfigMenu::generateScreen;
        }
    }

}
