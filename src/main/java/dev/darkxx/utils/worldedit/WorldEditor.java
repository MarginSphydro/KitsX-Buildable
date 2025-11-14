package dev.darkxx.utils.worldedit;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/worldedit/WorldEditor.class */
public class WorldEditor {
    public static AsyncWorldEditor async() {
        return new AsyncWorldEditor();
    }

    public static SyncWorldEditor sync() {
        return new SyncWorldEditor();
    }

    public static AsyncWorldEditor asynchronous() {
        return async();
    }

    public static SyncWorldEditor synchronous() {
        return sync();
    }
}
