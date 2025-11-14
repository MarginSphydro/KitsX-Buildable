package dev.darkxx.utils.scheduler;

import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/TickTimes.class */
public class TickTimes {
    public static long SECOND = oneSecond();
    public static long MINUTE = oneMinute();
    public static long HOUR = oneHour();
    public static long DAY = oneDay();

    public static long oneSecond() {
        return 20L;
    }

    public static long oneMinute() {
        return 1200L;
    }

    public static long oneHour() {
        return 72000L;
    }

    public static long oneDay() {
        return 1728000L;
    }

    public static long secondsToTicks(int seconds) {
        return seconds * oneSecond();
    }

    public static long minutesToTicks(int minutes) {
        return minutes * oneMinute();
    }

    public static long hoursToTicks(int hours) {
        return hours * oneHour();
    }

    public static long daysToTicks(int days) {
        return days * oneDay();
    }

    public static long secondsToTicks(long seconds) {
        return seconds * oneSecond();
    }

    public static long minutesToTicks(long minutes) {
        return minutes * oneMinute();
    }

    public static long hoursToTicks(long hours) {
        return hours * oneHour();
    }

    public static long daysToTicks(long days) {
        return days * oneDay();
    }

    public static long ticksToSeconds(int ticks) {
        return ticks / oneSecond();
    }

    public static long ticksToMinutes(int ticks) {
        return ticks / oneMinute();
    }

    public static long ticksToHours(int ticks) {
        return ticks / oneHour();
    }

    public static long ticksToDays(int ticks) {
        return ticks / oneDay();
    }

    public static long ticksToSeconds(long ticks) {
        return ticks / oneSecond();
    }

    public static long ticksToMinutes(long ticks) {
        return ticks / oneMinute();
    }

    public static long ticksToHours(long ticks) {
        return ticks / oneHour();
    }

    public static long ticksToDays(long ticks) {
        return ticks / oneDay();
    }

    /* renamed from: dev.darkxx.utils.scheduler.TickTimes$1, reason: invalid class name */
    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/scheduler/TickTimes$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$util$concurrent$TimeUnit = new int[TimeUnit.values().length];

        static {
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.NANOSECONDS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MILLISECONDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MICROSECONDS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.SECONDS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MINUTES.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.HOURS.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.DAYS.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public static long ticks(int val, @NotNull TimeUnit unit) {
        switch (AnonymousClass1.$SwitchMap$java$util$concurrent$TimeUnit[unit.ordinal()]) {
            case 1:
            case 2:
            case 3:
                throw new UnsupportedOperationException();
            case 4:
                return secondsToTicks(val);
            case 5:
                return minutesToTicks(val);
            case 6:
                return hoursToTicks(val);
            case 7:
                return daysToTicks(val);
            default:
                throw new IncompatibleClassChangeError();
        }
    }

    public static int val(long ticks, @NotNull TimeUnit unit) {
        switch (AnonymousClass1.$SwitchMap$java$util$concurrent$TimeUnit[unit.ordinal()]) {
            case 1:
            case 2:
            case 3:
                throw new UnsupportedOperationException();
            case 4:
                return (int) ticksToSeconds(ticks);
            case 5:
                return (int) ticksToMinutes(ticks);
            case 6:
                return (int) ticksToHours(ticks);
            case 7:
                return (int) ticksToDays(ticks);
            default:
                throw new IncompatibleClassChangeError();
        }
    }
}
