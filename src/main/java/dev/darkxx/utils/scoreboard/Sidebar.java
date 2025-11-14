package dev.darkxx.utils.scoreboard;

import dev.darkxx.utils.reflection.ReflectionUtils;
import dev.darkxx.utils.scoreboard.SidebarHandler;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scoreboard/Sidebar.class */
public class Sidebar extends SidebarHandler<Component> {
    private static final MethodHandle COMPONENT_METHOD;
    private static final Object EMPTY_COMPONENT;
    private static final boolean ADVENTURE_SUPPORT = ReflectionUtils.optionalClass("io.papermc.paper.adventure.PaperAdventure").isPresent();

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            if (ADVENTURE_SUPPORT) {
                Class<?> paperAdventure = Class.forName("io.papermc.paper.adventure.PaperAdventure");
                Method method = paperAdventure.getDeclaredMethod("asVanilla", Component.class);
                COMPONENT_METHOD = lookup.unreflect(method);
                EMPTY_COMPONENT = (Object) COMPONENT_METHOD.invoke(Component.empty());
            } else {
                Class<?> craftChatMessageClass = ReflectionUtils.obcClass("util.CraftChatMessage");
                COMPONENT_METHOD = lookup.unreflect(craftChatMessageClass.getMethod("fromString", String.class));
                EMPTY_COMPONENT = Array.get((Object) COMPONENT_METHOD.invoke(""), 0);
            }
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    public Sidebar(Player player) {
        super(player);
    }

    public static Sidebar sidebar(Player player) {
        return new Sidebar(player);
    }

    @Override // dev.darkxx.utils.scoreboard.SidebarHandler
    protected void sendLineChange(int score) throws Throwable {
        Component line = lineByScore(score);
        sendTeamPacket(score, SidebarHandler.TeamMode.UPDATE, line, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // dev.darkxx.utils.scoreboard.SidebarHandler
    public Object toMinecraftComponent(Component component) throws Throwable {
        if (component == null) {
            return EMPTY_COMPONENT;
        }
        if (!ADVENTURE_SUPPORT) {
            String legacy = serializeLine(component);
            return Array.get((Object) COMPONENT_METHOD.invoke(legacy), 0);
        }
        return (Object) COMPONENT_METHOD.invoke(component);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // dev.darkxx.utils.scoreboard.SidebarHandler
    public String serializeLine(Component value) {
        return LegacyComponentSerializer.legacySection().serialize(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dev.darkxx.utils.scoreboard.SidebarHandler
    public Component emptyLine() {
        return Component.empty();
    }
}
