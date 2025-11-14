package dev.darkxx.utils.scoreboard;

import dev.darkxx.utils.reflection.ReflectionUtils;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scoreboard/SidebarHandler.class */
public abstract class SidebarHandler<S> {
    private static final Map<Class<?>, Field[]> PACKETS = new HashMap(8);
    protected static final String[] COLOR_CODES = (String[]) Arrays.stream(ChatColor.values()).map((v0) -> {
        return v0.toString();
    }).toArray(x$0 -> {
        return new String[x$0];
    });
    private static final VersionType VERSION_TYPE;
    private static final Class<?> CHAT_COMPONENT_CLASS;
    private static final Class<?> CHAT_FORMAT_ENUM;
    private static final Object RESET_FORMATTING;
    private static final MethodHandle PLAYER_CONNECTION;
    private static final MethodHandle SEND_PACKET;
    private static final MethodHandle PLAYER_GET_HANDLE;
    private static final ReflectionUtils.PacketConstructor PACKET_SB_OBJ;
    private static final ReflectionUtils.PacketConstructor PACKET_SB_DISPLAY_OBJ;
    private static final ReflectionUtils.PacketConstructor PACKET_SB_SCORE;
    private static final ReflectionUtils.PacketConstructor PACKET_SB_TEAM;
    private static final ReflectionUtils.PacketConstructor PACKET_SB_SERIALIZABLE_TEAM;
    private static final Class<?> DISPLAY_SLOT_TYPE;
    private static final Class<?> ENUM_SB_HEALTH_DISPLAY;
    private static final Class<?> ENUM_SB_ACTION;
    private static final Object SIDEBAR_DISPLAY_SLOT;
    private static final Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
    private static final Object ENUM_SB_ACTION_CHANGE;
    private static final Object ENUM_SB_ACTION_REMOVE;
    private final Player player;
    private final List<S> lines = new ArrayList();
    private S title = emptyLine();
    private boolean deleted = false;
    private final String uniqueId = "sidebar-" + Integer.toHexString(ThreadLocalRandom.current().nextInt());

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scoreboard/SidebarHandler$ObjectiveMode.class */
    public enum ObjectiveMode {
        CREATE,
        REMOVE,
        UPDATE
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scoreboard/SidebarHandler$ScoreboardAction.class */
    public enum ScoreboardAction {
        CHANGE,
        REMOVE
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scoreboard/SidebarHandler$TeamMode.class */
    public enum TeamMode {
        CREATE,
        REMOVE,
        UPDATE,
        ADD_PLAYERS,
        REMOVE_PLAYERS
    }

    protected abstract void sendLineChange(int i) throws Throwable;

    protected abstract Object toMinecraftComponent(S s) throws Throwable;

    protected abstract String serializeLine(S s);

    protected abstract S emptyLine();

    static {
        String str;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            if (ReflectionUtils.isRepackaged()) {
                VERSION_TYPE = VersionType.V1_17;
            } else if (ReflectionUtils.nmsOptionalClass(null, "ScoreboardServer$Action").isPresent()) {
                VERSION_TYPE = VersionType.V1_13;
            } else if (ReflectionUtils.nmsOptionalClass(null, "IScoreboardCriteria$EnumScoreboardHealthDisplay").isPresent()) {
                VERSION_TYPE = VersionType.V1_8;
            } else {
                VERSION_TYPE = VersionType.V1_7;
            }
            Class<?> craftPlayerClass = ReflectionUtils.obcClass("entity.CraftPlayer");
            Class<?> entityPlayerClass = ReflectionUtils.nmsClass("server.level", "EntityPlayer");
            Class<?> playerConnectionClass = ReflectionUtils.nmsClass("server.network", "PlayerConnection");
            Class<?> packetClass = ReflectionUtils.nmsClass("network.protocol", "Packet");
            Class<?> packetSbObjClass = ReflectionUtils.nmsClass("network.protocol.game", "PacketPlayOutScoreboardObjective");
            Class<?> packetSbDisplayObjClass = ReflectionUtils.nmsClass("network.protocol.game", "PacketPlayOutScoreboardDisplayObjective");
            Class<?> packetSbScoreClass = ReflectionUtils.nmsClass("network.protocol.game", "PacketPlayOutScoreboardScore");
            Class<?> packetSbTeamClass = ReflectionUtils.nmsClass("network.protocol.game", "PacketPlayOutScoreboardTeam");
            Class<?> sbTeamClass = VersionType.V1_17.isHigherOrEqual() ? ReflectionUtils.innerClass(packetSbTeamClass, innerClass -> {
                return !innerClass.isEnum();
            }) : null;
            Field playerConnectionField = (Field) Arrays.stream(entityPlayerClass.getFields()).filter(field -> {
                return field.getType().isAssignableFrom(playerConnectionClass);
            }).findFirst().orElseThrow(NoSuchFieldException::new);
            Method sendPacketMethod = (Method) Stream.concat(Arrays.stream(playerConnectionClass.getSuperclass().getMethods()), Arrays.stream(playerConnectionClass.getMethods())).filter(m -> {
                return m.getParameterCount() == 1 && m.getParameterTypes()[0] == packetClass;
            }).findFirst().orElseThrow(NoSuchMethodException::new);
            Optional<Class<?>> displaySlotEnum = ReflectionUtils.nmsOptionalClass("world.scores", "DisplaySlot");
            CHAT_COMPONENT_CLASS = ReflectionUtils.nmsClass("network.chat", "IChatBaseComponent");
            CHAT_FORMAT_ENUM = ReflectionUtils.nmsClass(null, "EnumChatFormat");
            DISPLAY_SLOT_TYPE = displaySlotEnum.orElse(Integer.TYPE);
            RESET_FORMATTING = ReflectionUtils.enumValueOf(CHAT_FORMAT_ENUM, "RESET", 21);
            SIDEBAR_DISPLAY_SLOT = displaySlotEnum.isPresent() ? ReflectionUtils.enumValueOf(DISPLAY_SLOT_TYPE, "SIDEBAR", 1) : 1;
            PLAYER_GET_HANDLE = lookup.findVirtual(craftPlayerClass, "getHandle", MethodType.methodType(entityPlayerClass));
            PLAYER_CONNECTION = lookup.unreflectGetter(playerConnectionField);
            SEND_PACKET = lookup.unreflect(sendPacketMethod);
            PACKET_SB_OBJ = ReflectionUtils.findPacketConstructor(packetSbObjClass, lookup);
            PACKET_SB_DISPLAY_OBJ = ReflectionUtils.findPacketConstructor(packetSbDisplayObjClass, lookup);
            PACKET_SB_SCORE = ReflectionUtils.findPacketConstructor(packetSbScoreClass, lookup);
            PACKET_SB_TEAM = ReflectionUtils.findPacketConstructor(packetSbTeamClass, lookup);
            PACKET_SB_SERIALIZABLE_TEAM = sbTeamClass == null ? null : ReflectionUtils.findPacketConstructor(sbTeamClass, lookup);
            for (Class<?> clazz : Arrays.asList(packetSbObjClass, packetSbDisplayObjClass, packetSbScoreClass, packetSbTeamClass, sbTeamClass)) {
                if (clazz != null) {
                    Field[] fields = (Field[]) Arrays.stream(clazz.getDeclaredFields()).filter(field2 -> {
                        return !Modifier.isStatic(field2.getModifiers());
                    }).toArray(x$0 -> {
                        return new Field[x$0];
                    });
                    for (Field field3 : fields) {
                        field3.setAccessible(true);
                    }
                    PACKETS.put(clazz, fields);
                }
            }
            if (VersionType.V1_8.isHigherOrEqual()) {
                if (VersionType.V1_13.isHigherOrEqual()) {
                    str = "ScoreboardServer$Action";
                } else {
                    str = "PacketPlayOutScoreboardScore$EnumScoreboardAction";
                }
                String enumSbActionClass = str;
                ENUM_SB_HEALTH_DISPLAY = ReflectionUtils.nmsClass("world.scores.criteria", "IScoreboardCriteria$EnumScoreboardHealthDisplay");
                ENUM_SB_ACTION = ReflectionUtils.nmsClass("server", enumSbActionClass);
                ENUM_SB_HEALTH_DISPLAY_INTEGER = ReflectionUtils.enumValueOf(ENUM_SB_HEALTH_DISPLAY, "INTEGER", 0);
                ENUM_SB_ACTION_CHANGE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "CHANGE", 0);
                ENUM_SB_ACTION_REMOVE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "REMOVE", 1);
            } else {
                ENUM_SB_HEALTH_DISPLAY = null;
                ENUM_SB_ACTION = null;
                ENUM_SB_HEALTH_DISPLAY_INTEGER = null;
                ENUM_SB_ACTION_CHANGE = null;
                ENUM_SB_ACTION_REMOVE = null;
            }
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    protected SidebarHandler(Player player) {
        this.player = (Player) Objects.requireNonNull(player, "player");
        try {
            sendObjectivePacket(ObjectiveMode.CREATE);
            sendDisplayObjectivePacket();
        } catch (Throwable t) {
            throw new RuntimeException("Unable to create scoreboard", t);
        }
    }

    public S title() {
        return this.title;
    }

    public void updateTitle(S title) {
        if (this.title.equals(Objects.requireNonNull(title, "title"))) {
            return;
        }
        this.title = title;
        try {
            sendObjectivePacket(ObjectiveMode.UPDATE);
        } catch (Throwable t) {
            throw new RuntimeException("Unable to update scoreboard title", t);
        }
    }

    public List<S> lines() {
        return new ArrayList(this.lines);
    }

    public S line(int line) {
        checkLineNumber(line, true, false);
        return this.lines.get(line);
    }

    public synchronized void updateLine(int line, S text) {
        checkLineNumber(line, false, true);
        try {
            if (line < size()) {
                this.lines.set(line, text);
                sendLineChange(scoreByLine(line));
                return;
            }
            List<S> newLines = new ArrayList<>(this.lines);
            if (line > size()) {
                for (int i = size(); i < line; i++) {
                    newLines.add(emptyLine());
                }
            }
            newLines.add(text);
            updateLines(newLines);
        } catch (Throwable t) {
            throw new RuntimeException("Unable to update scoreboard lines", t);
        }
    }

    public synchronized void removeLine(int line) {
        checkLineNumber(line, false, false);
        if (line >= size()) {
            return;
        }
        List<S> newLines = new ArrayList<>(this.lines);
        newLines.remove(line);
        updateLines(newLines);
    }

    @SafeVarargs
    public final void updateLines(S... lines) {
        updateLines(Arrays.asList(lines));
    }

    public synchronized void updateLines(Collection<S> lines) {
        Objects.requireNonNull(lines, "lines");
        checkLineNumber(lines.size(), false, true);
        List<S> oldLines = new ArrayList<>(this.lines);
        this.lines.clear();
        this.lines.addAll(lines);
        int linesSize = this.lines.size();
        try {
            if (oldLines.size() != linesSize) {
                List<S> oldLinesCopy = new ArrayList<>(oldLines);
                if (oldLines.size() > linesSize) {
                    for (int i = oldLinesCopy.size(); i > linesSize; i--) {
                        sendTeamPacket(i - 1, TeamMode.REMOVE);
                        sendScorePacket(i - 1, ScoreboardAction.REMOVE);
                        oldLines.remove(0);
                    }
                } else {
                    for (int i2 = oldLinesCopy.size(); i2 < linesSize; i2++) {
                        sendScorePacket(i2, ScoreboardAction.CHANGE);
                        sendTeamPacket(i2, TeamMode.CREATE, null, null);
                    }
                }
            }
            for (int i3 = 0; i3 < linesSize; i3++) {
                if (!Objects.equals(lineByScore(oldLines, i3), lineByScore(i3))) {
                    sendLineChange(i3);
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Unable to update scoreboard lines", t);
        }
    }

    public Player player() {
        return this.player;
    }

    public String uniqueId() {
        return this.uniqueId;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public int size() {
        return this.lines.size();
    }

    public void delete() throws Throwable {
        for (int i = 0; i < this.lines.size(); i++) {
            try {
                sendTeamPacket(i, TeamMode.REMOVE);
            } catch (Throwable t) {
                throw new RuntimeException("Unable to delete scoreboard", t);
            }
        }
        sendObjectivePacket(ObjectiveMode.REMOVE);
        this.deleted = true;
    }

    private void checkLineNumber(int line, boolean checkInRange, boolean checkMax) {
        if (line < 0) {
            throw new IllegalArgumentException("Line number must be positive");
        }
        if (checkInRange && line >= this.lines.size()) {
            throw new IllegalArgumentException("Line number must be under " + this.lines.size());
        }
        if (checkMax && line >= COLOR_CODES.length - 1) {
            throw new IllegalArgumentException("Line number is too high: " + line);
        }
    }

    protected int scoreByLine(int line) {
        return (this.lines.size() - line) - 1;
    }

    protected S lineByScore(int score) {
        return lineByScore(this.lines, score);
    }

    protected S lineByScore(List<S> lines, int score) {
        if (score < lines.size()) {
            return lines.get((lines.size() - score) - 1);
        }
        return null;
    }

    protected void sendObjectivePacket(ObjectiveMode mode) throws Throwable {
        Object packet = PACKET_SB_OBJ.invoke();
        field(packet, String.class, this.uniqueId);
        field(packet, Integer.TYPE, Integer.valueOf(mode.ordinal()));
        if (mode != ObjectiveMode.REMOVE) {
            componentField(packet, this.title, 1);
            if (VersionType.V1_8.isHigherOrEqual()) {
                field(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
            }
        } else if (VERSION_TYPE == VersionType.V1_7) {
            field(packet, String.class, "", 1);
        }
        sendPacket(packet);
    }

    protected void sendDisplayObjectivePacket() throws Throwable {
        Object packet = PACKET_SB_DISPLAY_OBJ.invoke();
        field(packet, DISPLAY_SLOT_TYPE, SIDEBAR_DISPLAY_SLOT);
        field(packet, String.class, this.uniqueId);
        sendPacket(packet);
    }

    protected void sendScorePacket(int score, ScoreboardAction action) throws Throwable {
        Object packet = PACKET_SB_SCORE.invoke();
        field(packet, String.class, COLOR_CODES[score], 0);
        if (VersionType.V1_8.isHigherOrEqual()) {
            Object enumAction = action == ScoreboardAction.REMOVE ? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE;
            field(packet, ENUM_SB_ACTION, enumAction);
        } else {
            field(packet, Integer.TYPE, Integer.valueOf(action.ordinal()), 1);
        }
        if (action == ScoreboardAction.CHANGE) {
            field(packet, String.class, this.uniqueId, 1);
            field(packet, Integer.TYPE, Integer.valueOf(score));
        }
        sendPacket(packet);
    }

    protected void sendTeamPacket(int score, TeamMode mode) throws Throwable {
        sendTeamPacket(score, mode, null, null);
    }

    protected void sendTeamPacket(int score, TeamMode mode, S prefix, S suffix) throws Throwable {
        if (mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
            throw new UnsupportedOperationException();
        }
        Object packet = PACKET_SB_TEAM.invoke();
        field(packet, String.class, this.uniqueId + ":" + score);
        field(packet, Integer.TYPE, Integer.valueOf(mode.ordinal()), VERSION_TYPE == VersionType.V1_8 ? 1 : 0);
        if (mode == TeamMode.REMOVE) {
            sendPacket(packet);
            return;
        }
        if (VersionType.V1_17.isHigherOrEqual()) {
            Object team = PACKET_SB_SERIALIZABLE_TEAM.invoke();
            componentField(team, null, 0);
            field(team, CHAT_FORMAT_ENUM, RESET_FORMATTING);
            componentField(team, prefix, 1);
            componentField(team, suffix, 2);
            field(team, String.class, "always", 0);
            field(team, String.class, "always", 1);
            field(packet, Optional.class, Optional.of(team));
        } else {
            componentField(packet, prefix, 2);
            componentField(packet, suffix, 3);
            field(packet, String.class, "always", 4);
            field(packet, String.class, "always", 5);
        }
        if (mode == TeamMode.CREATE) {
            field(packet, Collection.class, Collections.singletonList(COLOR_CODES[score]));
        }
        sendPacket(packet);
    }

    private void sendPacket(Object packet) throws Throwable {
        if (this.deleted) {
            throw new IllegalStateException("This FastBoard is deleted");
        }
        if (this.player.isOnline()) {
            Object entityPlayer = (Object) PLAYER_GET_HANDLE.invoke(this.player);
            Object playerConnection = (Object) PLAYER_CONNECTION.invoke(entityPlayer);
            SEND_PACKET.invoke(playerConnection, packet);
        }
    }

    private void field(Object object, Class<?> fieldType, Object value) throws ReflectiveOperationException, IllegalArgumentException {
        field(object, fieldType, value, 0);
    }

    private void field(Object packet, Class<?> fieldType, Object value, int count) throws ReflectiveOperationException, IllegalArgumentException {
        int i = 0;
        for (Field field : PACKETS.get(packet.getClass())) {
            if (field.getType() == fieldType) {
                int i2 = i;
                i++;
                if (count == i2) {
                    field.set(packet, value);
                }
            }
        }
    }

    private void componentField(Object packet, S value, int count) throws Throwable {
        if (!VersionType.V1_13.isHigherOrEqual()) {
            String line = value != null ? serializeLine(value) : "";
            field(packet, String.class, line, count);
            return;
        }
        int i = 0;
        for (Field field : PACKETS.get(packet.getClass())) {
            if (field.getType() == String.class || field.getType() == CHAT_COMPONENT_CLASS) {
                int i2 = i;
                i++;
                if (count == i2) {
                    field.set(packet, toMinecraftComponent(value));
                }
            }
        }
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scoreboard/SidebarHandler$VersionType.class */
    enum VersionType {
        V1_7,
        V1_8,
        V1_13,
        V1_17;

        public boolean isHigherOrEqual() {
            return SidebarHandler.VERSION_TYPE.ordinal() >= ordinal();
        }
    }
}
