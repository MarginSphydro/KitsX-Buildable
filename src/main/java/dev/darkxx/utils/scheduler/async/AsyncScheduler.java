package dev.darkxx.utils.scheduler.async;

import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.scheduler.SchedulerBase;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/async/AsyncScheduler.class */
public class AsyncScheduler extends SchedulerBase {
    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Consumer<BukkitTask> task) {
        scheduler().runTaskAsynchronously(Utils.plugin(), task);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Runnable runnable) {
        scheduler().runTaskAsynchronously(Utils.plugin(), runnable);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks) {
        scheduler().runTaskLaterAsynchronously(Utils.plugin(), task, afterTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Runnable runnable, int afterTicks) {
        scheduler().runTaskLaterAsynchronously(Utils.plugin(), runnable, afterTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Consumer<BukkitTask> task, int afterTicks, int everyTicks) {
        scheduler().runTaskTimerAsynchronously(Utils.plugin(), task, afterTicks, everyTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    public void execute(@NotNull Runnable runnable, int afterTicks, int everyTicks) {
        scheduler().runTaskTimerAsynchronously(Utils.plugin(), runnable, afterTicks, everyTicks);
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    @Nullable
    public Object execute(@NotNull Supplier<?> supplier) {
        try {
            return CompletableFuture.supplyAsync(supplier).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // dev.darkxx.utils.scheduler.SchedulerBase
    @Nullable
    public <T> T supply(@NotNull Supplier<T> supplier) {
        try {
            return (T) CompletableFuture.supplyAsync(supplier).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
