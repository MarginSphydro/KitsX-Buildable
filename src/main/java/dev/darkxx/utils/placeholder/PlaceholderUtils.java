package dev.darkxx.utils.placeholder;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/placeholder/PlaceholderUtils.class */
public class PlaceholderUtils {
    public static String fill(String text, String... replacements) {
        for (int i = 0; i < replacements.length; i++) {
            text = text.replace("{" + i + "}", replacements[i]);
        }
        return text;
    }

    public static PlaceholderBuilder builder() {
        return new PlaceholderBuilder();
    }
}
