package dev.tehbrian.colorfularguments;

import dev.tehbrian.colorfularguments.config.Config;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;


public class ColorfulArguments implements ClientModInitializer {

    private static ColorfulArguments INSTANCE;
    private static Logger LOGGER;

    private Config config;

    public static ColorfulArguments get() {
        return INSTANCE;
    }

    public static Logger logger() {
        return LOGGER;
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER = LoggerFactory.getLogger("ColorfulArguments");

        this.config = new Config();

        try {
            this.config.load();
        } catch (final ConfigurateException e) {
            LOGGER.error("Failed to load config.", e);
        } catch (final NullPointerException e) {
            // fine, just means that they didn't have a config.
        } catch (final NumberFormatException e) {
            LOGGER.error("Boo!");
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
