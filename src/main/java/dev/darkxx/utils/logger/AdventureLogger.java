package dev.darkxx.utils.logger;

import dev.darkxx.utils.library.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/logger/AdventureLogger.class */
public class AdventureLogger {
    private static final ComponentLogger LOGGER = Utils.plugin().getComponentLogger();

    public void debug(Component component) {
        LOGGER.debug(component);
    }

    public void warn(Component component) {
        LOGGER.warn(component);
    }

    public void error(Component component) {
        LOGGER.error(component);
    }

    public void info(Component component) {
        LOGGER.info(component);
    }

    public void trace(Component component) {
        LOGGER.trace(component);
    }
}
