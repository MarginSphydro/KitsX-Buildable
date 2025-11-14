package dev.darkxx.utils.prettify;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/prettify/ListPrettify.class */
public class ListPrettify {
    @NotNull
    public static String strings(List<String> input) {
        return input.toString().replaceAll("\\[", "").replaceAll("]", "");
    }

    @NotNull
    public static String players(List<Player> input) {
        List<String> inputToString = (List) input.stream().map((v0) -> {
            return v0.getName();
        }).collect(Collectors.toList());
        return strings(inputToString);
    }
}
