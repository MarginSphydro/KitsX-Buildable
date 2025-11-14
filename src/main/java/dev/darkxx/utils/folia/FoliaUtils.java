package dev.darkxx.utils.folia;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/folia/FoliaUtils.class */
public class FoliaUtils {
    public static boolean hasFolia() throws ClassNotFoundException {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isFolia() throws ClassNotFoundException {
        return hasFolia();
    }
}
