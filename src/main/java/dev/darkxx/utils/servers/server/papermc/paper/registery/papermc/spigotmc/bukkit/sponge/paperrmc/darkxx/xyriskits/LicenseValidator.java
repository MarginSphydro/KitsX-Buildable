package dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits;

import dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.util.DatabaseManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/servers/server/papermc/paper/registery/papermc/spigotmc/bukkit/sponge/paperrmc/darkxx/xyriskits/LicenseValidator.class */
public class LicenseValidator {
    private final String a;
    private final DatabaseManager b;
    private final String c;
    private int d;
    private LocalDateTime e;
    private String[] f;
    private int g;
    private String h;
    private LocalDateTime i;
    private final DateTimeFormatter j = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public int getD() {
        return this.d;
    }

    public int getG() {
        return this.g;
    }

    public String getH() {
        return this.h;
    }

    public LicenseValidator(String a, DatabaseManager b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean isValidLicense() throws SQLException {
        PreparedStatement k = this.b.getConnection().prepareStatement(String.format("SELECT license_key, max_servers, license_expire_time, server_count, allowed_ips, license_username, license_creation_date FROM %s WHERE license_key = ?", this.c));
        try {
            k.setString(1, this.a);
            ResultSet l = k.executeQuery();
            if (l.next()) {
                this.d = l.getInt(2);
                this.e = l.getTimestamp(3).toLocalDateTime();
                this.g = l.getInt(4);
                this.f = l.getString(5).equalsIgnoreCase("UNLIMITED") ? null : l.getString(5).split(",");
                this.h = l.getString(6);
                this.i = l.getTimestamp(7).toLocalDateTime();
                if (k != null) {
                    k.close();
                }
                return true;
            }
            if (k != null) {
                k.close();
                return false;
            }
            return false;
        } catch (Throwable th) {
            if (k != null) {
                try {
                    k.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public boolean isLicenseExpired() {
        return LocalDateTime.now().isAfter(this.e);
    }

    public String getFormattedCreationDate() {
        return this.i.format(this.j);
    }

    public String getLicenseUsername() {
        return this.h;
    }

    public String getFormattedExpirationDate() {
        return this.e.format(this.j);
    }

    public String[] getAllowedIPs() throws SQLException {
        PreparedStatement m = this.b.getConnection().prepareStatement(String.format("SELECT allowed_ips FROM %s WHERE license_key = ?", this.c));
        try {
            m.setString(1, this.a);
            ResultSet n = m.executeQuery();
            if (n.next()) {
                String o = n.getString(1);
                String[] strArr = o.equalsIgnoreCase("UNLIMITED") ? new String[]{"*"} : (String[]) Arrays.stream(o.split(",")).map(ip -> {
                    String[] p = ip.split("\\.");
                    p[3] = "***";
                    return String.join(".", p);
                }).toArray(x$0 -> {
                    return new String[x$0];
                });
                if (m != null) {
                    m.close();
                }
                return strArr;
            }
            if (m != null) {
                m.close();
                return null;
            }
            return null;
        } catch (Throwable th) {
            if (m != null) {
                try {
                    m.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public String getMaskedLicenseKey() {
        return (this.a == null || this.a.length() < 4) ? "Invalid License Key" : this.a.substring(0, 4) + "*".repeat(this.a.length() - 4);
    }

    public boolean canAddServer() {
        return this.g < this.d;
    }

    public boolean isIPAllowed() throws IOException {
        if (this.f == null) {
            return true;
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()));
        try {
            String q = r.readLine();
            r.close();
            return Arrays.stream(this.f).anyMatch(ip -> {
                return ip.trim().equals(q);
            });
        } catch (Throwable th) {
            try {
                r.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void incrementServerCount() throws SQLException {
        this.g++;
        updateServerCount();
    }

    public void decrementServerCount() throws SQLException {
        if (this.g > 0) {
            this.g--;
            updateServerCount();
        }
    }

    private void updateServerCount() throws SQLException {
        this.b.ensureConnection();
        int s = 0;
        while (s < 3) {
            try {
                PreparedStatement t = this.b.getConnection().prepareStatement(String.format("UPDATE %s SET server_count = ? WHERE license_key = ?", this.c));
                try {
                    t.setInt(1, this.g);
                    t.setString(2, this.a);
                    t.executeUpdate();
                    if (t != null) {
                        t.close();
                    }
                    return;
                } finally {
                }
            } catch (SQLException e) {
                this.b.connect();
                s++;
                if (s == 3) {
                    throw e;
                }
            }
        }
    }

    public String getPluginName() {
        return (String) Arrays.stream(this.c.split("_")).map(word -> {
            return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        }).reduce((first, second) -> {
            return first + " " + second;
        }).orElse(this.c);
    }
}
