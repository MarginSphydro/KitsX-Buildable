package dev.darkxx.utils.papi;

import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.prettify.ListPrettify;
import java.util.function.BiFunction;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/papi/PlaceholderAPIBuilder.class */
public class PlaceholderAPIBuilder {
    private final String prefix;
    private String version = Utils.plugin().getPluginMeta().getVersion();
    private BiFunction<Player, String, String> playerResult = null;
    private BiFunction<OfflinePlayer, String, String> offlinePlayerResult = null;
    private String author = ListPrettify.strings(Utils.plugin().getPluginMeta().getAuthors());

    public PlaceholderAPIBuilder(@NotNull String prefix) {
        this.prefix = prefix;
    }

    @NotNull
    public static PlaceholderAPIBuilder of(@NotNull String identifier) {
        return new PlaceholderAPIBuilder(identifier);
    }

    @NotNull
    public PlaceholderAPIBuilder author(@NotNull String author) {
        this.author = author;
        return this;
    }

    @NotNull
    public PlaceholderAPIBuilder version(@NotNull String version) {
        this.version = version;
        return this;
    }

    @NotNull
    public PlaceholderAPIBuilder onlineRequest(@Nullable BiFunction<Player, String, String> result) {
        this.playerResult = result;
        return this;
    }

    @NotNull
    public PlaceholderAPIBuilder offlineRequest(@Nullable BiFunction<OfflinePlayer, String, String> result) {
        this.offlinePlayerResult = result;
        return this;
    }

    @NotNull
    public PlaceholderAPIBuilder request(@Nullable BiFunction<Player, String, String> onlineResult, @Nullable BiFunction<OfflinePlayer, String, String> offlineResult) {
        this.playerResult = onlineResult;
        this.offlinePlayerResult = offlineResult;
        return this;
    }

    @Nullable
    public PlaceholderExpansion build() {
        PlaceholderExpansion expansion = new PlaceholderExpansion() { // from class: dev.darkxx.utils.papi.PlaceholderAPIBuilder.1
            @NotNull
            public String getIdentifier() {
                return PlaceholderAPIBuilder.this.prefix;
            }

            @NotNull
            public String getAuthor() {
                return PlaceholderAPIBuilder.this.author;
            }

            @NotNull
            public String getVersion() {
                return PlaceholderAPIBuilder.this.version;
            }

            public String onPlaceholderRequest(Player player, @NotNull String params) {
                if (PlaceholderAPIBuilder.this.playerResult != null) {
                    return PlaceholderAPIBuilder.this.playerResult.apply(player, params);
                }
                return null;
            }

            public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
                if (PlaceholderAPIBuilder.this.offlinePlayerResult != null) {
                    return PlaceholderAPIBuilder.this.offlinePlayerResult.apply(offlinePlayer, params);
                }
                return null;
            }
        };
        if (expansion.canRegister() && !expansion.isRegistered()) {
            expansion.register();
            return expansion;
        }
        return null;
    }
}
