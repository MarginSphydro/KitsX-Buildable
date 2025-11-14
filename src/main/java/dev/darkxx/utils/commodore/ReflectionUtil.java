package dev.darkxx.utils.commodore;

import dev.darkxx.utils.reflection.ReflectionUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/commodore/ReflectionUtil.class */
final class ReflectionUtil {
    private static final String SERVER_VERSION = getServerVersion();

    private static String getServerVersion() {
        Class<?> server = Bukkit.getServer().getClass();
        if (!server.getSimpleName().equals("CraftServer") || server.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
            return ".";
        }
        String version = server.getName().substring(ReflectionUtils.CRAFTBUKKIT_PACKAGE.length());
        return version.substring(0, version.length() - "CraftServer".length());
    }

    public static String mc(String name) {
        return "net.minecraft." + name;
    }

    public static String nms(String className) {
        return ReflectionUtils.NMS_PACKAGE + SERVER_VERSION + className;
    }

    public static Class<?> mcClass(String className) throws ClassNotFoundException {
        return Class.forName(mc(className));
    }

    public static Class<?> nmsClass(String className) throws ClassNotFoundException {
        return Class.forName(nms(className));
    }

    public static String obc(String className) {
        return ReflectionUtils.CRAFTBUKKIT_PACKAGE + SERVER_VERSION + className;
    }

    public static Class<?> obcClass(String className) throws ClassNotFoundException {
        return Class.forName(obc(className));
    }

    public static int minecraftVersion() {
        try {
            Matcher matcher = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?( .*)?\\)").matcher(Bukkit.getVersion());
            if (matcher.find()) {
                return Integer.parseInt(matcher.toMatchResult().group(2), 10);
            }
            throw new IllegalArgumentException(String.format("No match found in '%s'", Bukkit.getVersion()));
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Failed to determine Minecraft version", ex);
        }
    }

    private ReflectionUtil() {
    }
}
