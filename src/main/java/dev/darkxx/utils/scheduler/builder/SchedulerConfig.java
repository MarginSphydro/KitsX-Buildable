package dev.darkxx.utils.scheduler.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/builder/SchedulerConfig.class */
public class SchedulerConfig {

    @Nullable
    private Integer everyTicks = null;

    @Nullable
    private Integer afterTicks = null;

    @NotNull
    private SchedulerThreadType threadType = SchedulerThreadType.SYNC;

    @Nullable
    public Integer everyTicks() {
        return this.everyTicks;
    }

    @NotNull
    public SchedulerConfig everyTicks(@Nullable Integer everyTicks) {
        this.everyTicks = everyTicks;
        return this;
    }

    @Nullable
    public Integer afterTicks() {
        return this.afterTicks;
    }

    @NotNull
    public SchedulerConfig afterTicks(@Nullable Integer afterTicks) {
        this.afterTicks = afterTicks;
        return this;
    }

    @NotNull
    public SchedulerConfig async() {
        this.threadType = SchedulerThreadType.ASYNC;
        return this;
    }

    @NotNull
    public SchedulerConfig sync() {
        this.threadType = SchedulerThreadType.SYNC;
        return this;
    }

    @NotNull
    public SchedulerThreadType threadType() {
        return this.threadType;
    }

    @NotNull
    public SchedulerConfig threadType(@NotNull SchedulerThreadType threadType) {
        this.threadType = threadType;
        return this;
    }
}
