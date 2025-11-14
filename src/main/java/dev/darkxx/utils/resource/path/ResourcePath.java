package dev.darkxx.utils.resource.path;

import dev.darkxx.utils.library.Utils;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/resource/path/ResourcePath.class */
public class ResourcePath {
    private final String path;

    public ResourcePath(String path) {
        this.path = path;
    }

    public ResourcePath() {
        this.path = Utils.plugin().getDataFolder().getPath();
    }

    public static ResourcePath path(String path) {
        return new ResourcePath(path);
    }

    public String path() {
        return this.path;
    }
}
