package dev.darkxx.utils.scheduler;

import dev.darkxx.utils.scheduler.async.AsyncScheduler;
import dev.darkxx.utils.scheduler.builder.SchedulerBuilder;
import dev.darkxx.utils.scheduler.stacker.SchedulerStacker;
import dev.darkxx.utils.scheduler.sync.SyncScheduler;
import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/Schedulers.class */
public class Schedulers {
    @NotNull
    public static SyncScheduler sync() {
        return new SyncScheduler();
    }

    @NotNull
    public static AsyncScheduler async() {
        return new AsyncScheduler();
    }

    @NotNull
    public static SyncScheduler synchronous() {
        return sync();
    }

    @NotNull
    public static AsyncScheduler asynchronous() {
        return async();
    }

    @NotNull
    public static SchedulerBuilder builder() {
        return new SchedulerBuilder();
    }

    @NotNull
    public static SchedulerBuilder builder(@Nullable Runnable runnable) {
        if (runnable != null) {
            return new SchedulerBuilder(runnable);
        }
        return new SchedulerBuilder();
    }

    @NotNull
    public static SchedulerBuilder builder(@Nullable Consumer<BukkitTask> task) {
        if (task != null) {
            return new SchedulerBuilder(task);
        }
        return new SchedulerBuilder();
    }

    @NotNull
    public static SchedulerStacker stacker() {
        return new SchedulerStacker();
    }
}
