package dev.darkxx.utils.resource;

import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.resource.format.ResourceFormat;
import dev.darkxx.utils.resource.format.ResourceFormats;
import dev.darkxx.utils.resource.path.ResourcePath;
import dev.darkxx.utils.resource.yaml.ResourceConfiguration;
import dev.darkxx.utils.scheduler.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/resource/ResourceFile.class */
public class ResourceFile {
    private ResourceFormat resourceFormat;
    private final ResourceOptions recourseOptions;
    private final ResourcePath resourcePath;
    private final String name;

    public ResourceFile(String name, ResourceFormat format, ResourceOptions options, ResourcePath path) {
        this.resourceFormat = format;
        this.recourseOptions = options;
        this.resourcePath = path;
        this.name = name;
        if (!this.resourceFormat.extension().contains(".")) {
            this.resourceFormat = ResourceFormat.format("." + this.resourceFormat.extension());
        }
    }

    public static ResourceFile resource(String name, ResourceFormat format, ResourceOptions options, ResourcePath path) {
        return new ResourceFile(name, format, options, path);
    }

    public static ResourceBuilder builder() {
        return ResourceBuilder.builder();
    }

    public ResourceConfiguration yaml() {
        return new ResourceConfiguration(this);
    }

    public FileConfiguration loadYml() {
        return (FileConfiguration) Schedulers.async().supply(() -> {
            return YamlConfiguration.loadConfiguration(file());
        });
    }

    public FileConfiguration reloadYml() {
        return loadYml();
    }

    public void saveYml() {
        if (Objects.equals(format(), ResourceFormats.yaml()) || Objects.equals(format(), ResourceFormats.yml())) {
            Schedulers.async().execute(task -> {
                try {
                    file().mkdirs();
                    file().createNewFile();
                    YamlConfiguration.loadConfiguration(file()).save(file());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return;
        }
        throw new UnsupportedOperationException("only yaml files are supported for saveOrLoad()");
    }

    public ResourceFile saveOrLoadYml() {
        if (Objects.equals(format(), ResourceFormats.yaml()) || Objects.equals(format(), ResourceFormats.yml())) {
            Schedulers.async().execute(task -> {
                if (file().exists()) {
                    YamlConfiguration.loadConfiguration(file());
                    return;
                }
                if (Utils.plugin().getResource(this.name + this.resourceFormat.extension()) != null) {
                    Utils.plugin().saveResource(this.resourcePath.path().replaceAll(Utils.plugin().getDataFolder().getPath(), "") + this.name + this.resourceFormat.extension(), false);
                }
                try {
                    if (!file().createNewFile()) {
                        Utils.plugin().getLogger().severe("Failed to create resource: " + file().getPath());
                        task.cancel();
                    }
                    YamlConfiguration yamlConfigurationLoadConfiguration = YamlConfiguration.loadConfiguration(file());
                    yamlConfigurationLoadConfiguration.options().copyDefaults(true).parseComments(true).pathSeparator('.');
                    if (this.recourseOptions.header() != null) {
                        yamlConfigurationLoadConfiguration.options().setHeader(this.recourseOptions.header());
                    }
                    if (this.recourseOptions.footer() != null) {
                        yamlConfigurationLoadConfiguration.options().setFooter(this.recourseOptions.footer());
                    }
                    try {
                        yamlConfigurationLoadConfiguration.save(file());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            });
            return this;
        }
        throw new UnsupportedOperationException("only yaml files are supported for saveOrLoad()");
    }

    public File file() {
        return new File(new File(this.resourcePath.path()), this.name + this.resourceFormat.extension());
    }

    public ResourceFormat format() {
        return this.resourceFormat;
    }

    public ResourceOptions options() {
        return this.recourseOptions;
    }

    public ResourcePath path() {
        return this.resourcePath;
    }

    public String name() {
        return this.name;
    }
}
