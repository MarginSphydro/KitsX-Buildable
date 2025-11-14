package dev.darkxx.utils.scheduler.builder;

import dev.darkxx.utils.scheduler.Schedulers;
import dev.darkxx.utils.scheduler.builder.task.SchedulerTask;
import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/builder/SchedulerBuilder.class */
public class SchedulerBuilder {
    private SchedulerTask schedulerTask;

    @NotNull
    private final SchedulerConfig config;

    public SchedulerBuilder() {
        this.config = new SchedulerConfig();
        this.schedulerTask = null;
    }

    public SchedulerBuilder(@NotNull Runnable runnable) {
        this.config = new SchedulerConfig();
        this.schedulerTask = SchedulerTask.wrap(runnable);
    }

    public SchedulerBuilder(@NotNull Consumer<BukkitTask> task) {
        this.config = new SchedulerConfig();
        this.schedulerTask = SchedulerTask.wrap(task);
    }

    @NotNull
    public SchedulerBuilder config(@NotNull Consumer<SchedulerConfig> configConsumer) {
        configConsumer.accept(this.config);
        return this;
    }

    @NotNull
    public SchedulerBuilder async() {
        this.config.async();
        return this;
    }

    @NotNull
    public SchedulerBuilder sync() {
        this.config.sync();
        return this;
    }

    @NotNull
    public SchedulerBuilder everyTicks(int everyTicks) {
        this.config.everyTicks(Integer.valueOf(everyTicks));
        return this;
    }

    @NotNull
    public SchedulerBuilder afterTicks(int afterTicks) {
        this.config.afterTicks(Integer.valueOf(afterTicks));
        return this;
    }

    @NotNull
    public SchedulerBuilder runnable(@NotNull Runnable runnable) {
        this.schedulerTask = SchedulerTask.wrap(runnable);
        return this;
    }

    @NotNull
    public SchedulerBuilder task(@NotNull Consumer<BukkitTask> task) {
        this.schedulerTask = SchedulerTask.wrap(task);
        return this;
    }

    public void execute() {
        switch (this.config.threadType()) {
            case SYNC:
                if (this.config.everyTicks() == null && this.config.afterTicks() == null) {
                    Object objAppropriate = this.schedulerTask.appropriate();
                    if (objAppropriate instanceof Consumer) {
                        Schedulers.sync().execute((Consumer<BukkitTask>) objAppropriate);
                        return;
                    }
                    Object objAppropriate2 = this.schedulerTask.appropriate();
                    if (objAppropriate2 instanceof Runnable) {
                        Runnable runnable = (Runnable) objAppropriate2;
                        Schedulers.sync().execute(runnable);
                        return;
                    }
                    return;
                }
                if (this.config.everyTicks() == null && this.config.afterTicks() != null) {
                    Object objAppropriate3 = this.schedulerTask.appropriate();
                    if (objAppropriate3 instanceof Consumer) {
                        Schedulers.sync().execute((Consumer<BukkitTask>) objAppropriate3, this.config.afterTicks().intValue());
                        return;
                    }
                    Object objAppropriate4 = this.schedulerTask.appropriate();
                    if (objAppropriate4 instanceof Runnable) {
                        Runnable runnable2 = (Runnable) objAppropriate4;
                        Schedulers.sync().execute(runnable2, this.config.afterTicks().intValue());
                        return;
                    }
                    return;
                }
                if (this.config.everyTicks() != null && this.config.afterTicks() != null) {
                    Object objAppropriate5 = this.schedulerTask.appropriate();
                    if (objAppropriate5 instanceof Consumer) {
                        Schedulers.sync().execute((Consumer<BukkitTask>) objAppropriate5, this.config.afterTicks().intValue(), this.config.everyTicks().intValue());
                        return;
                    }
                    Object objAppropriate6 = this.schedulerTask.appropriate();
                    if (objAppropriate6 instanceof Runnable) {
                        Runnable runnable3 = (Runnable) objAppropriate6;
                        Schedulers.sync().execute(runnable3, this.config.afterTicks().intValue(), this.config.everyTicks().intValue());
                        return;
                    }
                    return;
                }
                throw new UnsupportedOperationException("Not supported");
            case ASYNC:
                if (this.config.everyTicks() == null && this.config.afterTicks() == null) {
                    Object objAppropriate7 = this.schedulerTask.appropriate();
                    if (objAppropriate7 instanceof Consumer) {
                        Schedulers.async().execute((Consumer<BukkitTask>) objAppropriate7);
                        return;
                    }
                    Object objAppropriate8 = this.schedulerTask.appropriate();
                    if (objAppropriate8 instanceof Runnable) {
                        Runnable runnable4 = (Runnable) objAppropriate8;
                        Schedulers.async().execute(runnable4);
                        return;
                    }
                    return;
                }
                if (this.config.everyTicks() == null && this.config.afterTicks() != null) {
                    Object objAppropriate9 = this.schedulerTask.appropriate();
                    if (objAppropriate9 instanceof Consumer) {
                        Schedulers.async().execute((Consumer<BukkitTask>) objAppropriate9, this.config.afterTicks().intValue());
                        return;
                    }
                    Object objAppropriate10 = this.schedulerTask.appropriate();
                    if (objAppropriate10 instanceof Runnable) {
                        Runnable runnable5 = (Runnable) objAppropriate10;
                        Schedulers.async().execute(runnable5, this.config.afterTicks().intValue());
                        return;
                    }
                    return;
                }
                if (this.config.everyTicks() != null && this.config.afterTicks() != null) {
                    Object objAppropriate11 = this.schedulerTask.appropriate();
                    if (objAppropriate11 instanceof Consumer) {
                        Schedulers.async().execute((Consumer<BukkitTask>) objAppropriate11, this.config.afterTicks().intValue(), this.config.everyTicks().intValue());
                        return;
                    }
                    Object objAppropriate12 = this.schedulerTask.appropriate();
                    if (objAppropriate12 instanceof Runnable) {
                        Runnable runnable6 = (Runnable) objAppropriate12;
                        Schedulers.async().execute(runnable6, this.config.afterTicks().intValue(), this.config.everyTicks().intValue());
                        return;
                    }
                    return;
                }
                throw new UnsupportedOperationException("Not supported");
            default:
                return;
        }
    }
}
