package dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits;

import dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.util.DatabaseManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import org.bukkit.Bukkit;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/servers/server/papermc/paper/registery/papermc/spigotmc/bukkit/sponge/paperrmc/darkxx/xyriskits/LicenseService.class */
public class LicenseService {
    private static DatabaseManager dbManager;
    private static LicenseValidator licenseValidator;
    private static String dbUrl;
    private static String dbUser;
    private static String dbPass;

    public static LicenseValidator getLicenseValidator() {
        return licenseValidator;
    }

    public static void set(String url, String user, String pass) {
        dbUrl = url;
        dbUser = user;
        dbPass = pass;
    }

    public static void init(String key, String whatpluginbro) {
        try {
            URL url = new URL("https://ayosaar.xyris.fun/thisismyfuckingass.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String encodedDbUrl = in.readLine();
            String encodedDbUser = in.readLine();
            String encodedDbPass = in.readLine();
            in.close();
            dbUrl = decodeBase64(encodedDbUrl);
            dbUser = decodeBase64(encodedDbUser);
            dbPass = decodeBase64(encodedDbPass);
            dbManager = new DatabaseManager(dbUrl, dbUser, dbPass);
            dbManager.connect();
            licenseValidator = new LicenseValidator(key, dbManager, whatpluginbro.toUpperCase() + "_LICENSE");
        } catch (SQLException e) {
            licenseValidator = null;
            Bukkit.getLogger().info("[XyrisAPI] Could not connect to the database. Running in fallback mode.");
        } catch (Exception e2) {
            Bukkit.getLogger().severe("[XyrisAPI] Failed to read or decode credentials.");
            e2.printStackTrace();
        }
    }

    public static void closeConnection() throws SQLException {
        if (dbManager != null) {
            dbManager.closeConnection();
        }
    }

    private static String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
