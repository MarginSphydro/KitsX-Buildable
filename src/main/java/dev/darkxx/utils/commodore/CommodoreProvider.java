package dev.darkxx.utils.commodore;

import java.util.Objects;
import java.util.function.Function;
import org.bukkit.plugin.Plugin;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/CommodoreProvider.class */
public final class CommodoreProvider {
    private static final Function<Plugin, Commodore> PROVIDER = checkSupported();

    private CommodoreProvider() {
        throw new AssertionError();
    }

    private static Function<Plugin, Commodore> checkSupported() {
        try {
            Class.forName("com.mojang.brigadier.CommandDispatcher");
            try {
                PaperCommodore.ensureSetup();
                return PaperCommodore::new;
            } catch (Throwable e) {
                printDebugInfo(e);
                try {
                    ReflectionCommodore.ensureSetup();
                    return ReflectionCommodore::new;
                } catch (Throwable e2) {
                    printDebugInfo(e2);
                    return null;
                }
            }
        } catch (Throwable e3) {
            printDebugInfo(e3);
            return null;
        }
    }

    private static void printDebugInfo(Throwable e) {
        if (System.getProperty("commodore.debug") != null) {
            System.err.println("Exception while initialising commodore:");
            e.printStackTrace(System.err);
        }
    }

    public static boolean isSupported() {
        return PROVIDER != null;
    }

    public static Commodore getCommodore(Plugin plugin) throws BrigadierUnsupportedException {
        Objects.requireNonNull(plugin, "plugin");
        if (PROVIDER == null) {
            throw new BrigadierUnsupportedException("Brigadier is not supported by the server. Set -Dcommodore.debug=true for debug info.");
        }
        return PROVIDER.apply(plugin);
    }
}
