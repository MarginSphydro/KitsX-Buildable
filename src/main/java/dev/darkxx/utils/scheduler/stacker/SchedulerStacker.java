package dev.darkxx.utils.scheduler.stacker;

import dev.darkxx.utils.scheduler.builder.SchedulerBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/stacker/SchedulerStacker.class */
public class SchedulerStacker {

    @NotNull
    private final List<SchedulerBuilder> builders = new ArrayList();

    @NotNull
    public SchedulerStacker stack(@NotNull SchedulerBuilder builder) {
        this.builders.add(builder);
        return this;
    }

    @NotNull
    public SchedulerStacker stack(@NotNull List<SchedulerBuilder> builders) {
        this.builders.addAll(builders);
        return this;
    }

    @NotNull
    public SchedulerStacker stack(@NotNull SchedulerBuilder... builders) {
        this.builders.addAll(Arrays.asList(builders));
        return this;
    }

    public void execute() {
        for (SchedulerBuilder builder : this.builders) {
            builder.execute();
        }
    }
}
