package dk.lockfuglsang.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TimeUtil {;
    private static final Pattern TIME_PATTERN = Pattern.compile("((?<d>\\d+)d)?((?<h>\\d+)h)?((?<m>\\d+)m)?((?<s>\\d+)s)?");

    private static final long SEC = 1000;
    private static final long MIN = 60*SEC;
    private static final long HOUR = 60*MIN;
    private static final long DAYS = 24*HOUR;

    public static String millisAsString(long millis) {
        long d = millis / DAYS;
        long h = (millis % DAYS) / HOUR;
        long m = (millis % HOUR) / MIN;
        long s = (millis % MIN) / SEC;
        String str = "";
        if (d > 0) {
            str += d + "d";
        }
        if (h > 0) {
            str += " " + h + "h";
        }
        if (m > 0) {
            str += " " + m + "m";
        }
        if (s > 0 || str.isEmpty()) {
            str += " " + s + "s";
        }
        return str.trim();
    }

    public static long millisFromString(String time) {
        Matcher match = TIME_PATTERN.matcher(time);
        if (match.matches()) {

            long d = getLong(match.group("d"));
            long h = getLong(match.group("h"));
            long m = getLong(match.group("m"));
            long s = getLong(match.group("s"));
            return d*DAYS + h*HOUR + m*MIN + s*SEC;
        }
        return 0;
    }

    private static long getLong(String longString) {
        return longString != null && !longString.isEmpty() ? Long.parseLong(longString) : 0;
    }

    public static String ticksAsString(int ticks) {
        return millisAsString(ticks * 50);
    }

    public static long secondsAsTicks(int secs) {
        // 20 ticks per second = 50 ms per tick
        return (secs * 100) / 5;
    }

    public static long secondsAsMillis(long timeout) {
        return timeout * 1000;
    }

    public static int millisAsSeconds(long millis) {
        return (int) Math.ceil(millis / 1000f);
    }
}
