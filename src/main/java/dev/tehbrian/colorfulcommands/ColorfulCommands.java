package dev.tehbrian.colorfulcommands;

import dev.tehbrian.colorfulcommands.config.Config;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;


public class ColorfulCommands implements ClientModInitializer {

    private static ColorfulCommands INSTANCE;
    private static Logger LOGGER;

    private Config config;

    public static ColorfulCommands get() {
        return INSTANCE;
    }

    public static Logger logger() {
        return LOGGER;
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER = LoggerFactory.getLogger("ColorfulCommands");

        this.config = new Config();

        try {
            this.config.load();
        } catch (final ConfigurateException e) {
            LOGGER.error("CE.", e);
        } catch (final NullPointerException e) {
            LOGGER.error("NPE.", e);
        } catch (final NumberFormatException e) {
            LOGGER.error("NFE.", e);
        }

        try {
            this.config.save();
        } catch (final ConfigurateException e) {
            LOGGER.error("Failed to save config.", e);
        }
    }

    public Config config() {
        return this.config;
    }

}
