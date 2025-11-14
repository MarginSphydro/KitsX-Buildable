package dev.darkxx.utils.scheduler;

import dev.darkxx.utils.library.Utils;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/SchedulerBase.class */
public abstract class SchedulerBase {
    public abstract void execute(@NotNull Consumer<BukkitTask> consumer);

    public abstract void execute(@NotNull Runnable runnable);

    public abstract void execute(@NotNull Consumer<BukkitTask> consumer, int i);

    public abstract void execute(@NotNull Runnable runnable, int i);

    public abstract void execute(@NotNull Consumer<BukkitTask> consumer, int i, int i2);

    public abstract void execute(@NotNull Runnable runnable, int i, int i2);

    @Nullable
    public abstract Object execute(@NotNull Supplier<?> supplier);

    @Nullable
    public abstract <T> T supply(@NotNull Supplier<T> supplier);

    @NotNull
    public final BukkitScheduler scheduler() {
        return Utils.plugin().getServer().getScheduler();
    }
}
