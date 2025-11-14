package dev.darkxx.utils.worldedit;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/worldedit/AsyncWorldEditor.class */
public class AsyncWorldEditor extends SyncWorldEditor {
    @Override // dev.darkxx.utils.worldedit.SyncWorldEditor
    public void paste(Clipboard clipboard, Location corner1, boolean ignoreAir) {
        FaweAPI.getTaskManager().async(() -> {
            super.paste(clipboard, corner1, ignoreAir);
        });
    }

    @Override // dev.darkxx.utils.worldedit.SyncWorldEditor
    public void save(Clipboard clipboard, String path) {
        FaweAPI.getTaskManager().async(() -> {
            super.save(clipboard, path);
        });
    }
}
