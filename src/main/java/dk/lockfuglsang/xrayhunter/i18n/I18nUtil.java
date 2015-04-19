package dk.lockfuglsang.xrayhunter.i18n;

import java.text.MessageFormat;

/**
 * Prepared for GetText
 */
public enum I18nUtil {;
    public static String tr(String format, Object... args) {
        return MessageFormat.format(format, args);
    }
}
