package dev.darkxx.utils.text;

import dev.darkxx.utils.text.color.TextStyle;
import net.kyori.adventure.text.Component;
import java.util.Objects;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/text/Text.class */
public final class Text {
    private final String string;

    public Text(String string) {
        this.string = string;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(this.string);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Text)) return false;
        Text that = (Text) o;
        return Objects.equals(this.string, that.string);
    }

    public static Text text(String string) {
        return new Text(string);
    }

    public Component component() {
        return TextStyle.color(this.string);
    }

    public String colored() {
        return TextStyle.legacy(this.string);
    }

    public String string() {
        return this.string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
