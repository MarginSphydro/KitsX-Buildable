package dev.darkxx.utils.resource;

import dev.darkxx.utils.resource.format.ResourceFormat;
import dev.darkxx.utils.resource.format.ResourceFormats;
import dev.darkxx.utils.resource.path.ResourcePath;
import dev.darkxx.utils.resource.path.ResourcePaths;
import java.util.function.Consumer;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/resource/ResourceBuilder.class */
public class ResourceBuilder {
    private ResourceFormat format = ResourceFormats.yaml();
    private final ResourceOptions options = new ResourceOptions();
    private String name = null;
    private ResourcePath path = ResourcePaths.plugin();

    public static ResourceBuilder builder() {
        return new ResourceBuilder();
    }

    public ResourceFormat format() {
        return this.format;
    }

    public ResourceBuilder format(ResourceFormat format) {
        this.format = format;
        return this;
    }

    public ResourceOptions options() {
        return this.options;
    }

    public ResourceBuilder options(Consumer<ResourceOptions> optionsConsumer) {
        optionsConsumer.accept(this.options);
        return this;
    }

    public String name() {
        return this.name;
    }

    public ResourceBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ResourcePath path() {
        return this.path;
    }

    public ResourceBuilder path(ResourcePath path) {
        this.path = path;
        return this;
    }

    public ResourceFile build() {
        return ResourceFile.resource(this.name, this.format, this.options, this.path);
    }
}
