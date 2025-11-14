package dev.darkxx.utils.scheduler.builder.task;

import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/builder/task/SchedulerTask.class */
public class SchedulerTask {

    @Nullable
    private final Runnable runnable;

    @Nullable
    private final Consumer<BukkitTask> task;

    public SchedulerTask() {
        this.runnable = null;
        this.task = null;
    }

    public SchedulerTask(@Nullable Runnable runnable) {
        this.runnable = runnable;
        this.task = null;
    }

    public SchedulerTask(@Nullable Consumer<BukkitTask> task) {
        this.task = task;
        this.runnable = null;
    }

    @NotNull
    public static SchedulerTask wrap(@Nullable Runnable runnable) {
        return new SchedulerTask(runnable);
    }

    @NotNull
    public static SchedulerTask wrap(@Nullable Consumer<BukkitTask> task) {
        return new SchedulerTask(task);
    }

    @Nullable
    public Runnable runnable() {
        return this.runnable;
    }

    @Nullable
    public Consumer<BukkitTask> task() {
        return this.task;
    }

    @Nullable
    public Object appropriate() {
        if (this.task == null && this.runnable != null) {
            return this.runnable;
        }
        if (this.runnable == null && this.task != null) {
            return this.task;
        }
        return null;
    }
}
