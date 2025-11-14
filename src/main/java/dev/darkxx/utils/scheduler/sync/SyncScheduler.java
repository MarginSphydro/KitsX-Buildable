package dev.darkxx.utils.scheduler.sync;

import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.scheduler.SchedulerBase;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/sync/SyncScheduler.class */
public class SyncScheduler extends SchedulerBase {
    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Consumer<BukkitTask> task) {
        scheduler().runTask(Utils.plugin(), task);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Runnable runnable) {
        scheduler().runTask(Utils.plugin(), runnable);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks) {
        scheduler().runTaskLater(Utils.plugin(), task, afterTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Runnable runnable, int afterTicks) {
        scheduler().runTaskLater(Utils.plugin(), runnable, afterTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks, int everyTicks) {
        scheduler().runTaskTimer(Utils.plugin(), task, afterTicks, everyTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Runnable runnable, int afterTicks, int everyTicks) {
        scheduler().runTaskTimer(Utils.plugin(), runnable, afterTicks, everyTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    @Nullable
    public Object execute(@NotNull Supplier<?> supplier) {
        return supplier.get();
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    @Nullable
    public <T> T supply(@NotNull Supplier<T> supplier) {
        return supplier.get();
    }
}
