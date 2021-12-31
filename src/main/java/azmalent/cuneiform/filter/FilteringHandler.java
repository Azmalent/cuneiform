package azmalent.cuneiform.filter;

import azmalent.cuneiform.CuneiformConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.regex.Pattern;

public class FilteringHandler {
    public static void applyLogFilter() {
        LogFilter filter = new LogFilter();

        java.util.logging.Logger.getLogger("").setFilter(filter);

        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(filter);

        final LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
        for (final LoggerConfig logger : logContext.getConfiguration().getLoggers().values()) {
            logger.addFilter(filter);
        }
    }

    public static boolean shouldTruncateException(Throwable exception) {
        if (exception == null) return false;

        for (Class<?> clazz : CuneiformConfig.Common.Filtering.exceptionsToTruncate.get()) {
            if (clazz.isAssignableFrom(exception.getClass())) return true;
        }

        return false;
    }

    public static void truncateException(Throwable exception) {
        if (shouldTruncateException(exception)) {
            exception.setStackTrace(new StackTraceElement[]{});
        }
    }

    public static boolean isLoggable(String message) {
        try {
            for (String stringToFilter : CuneiformConfig.Common.Filtering.stringsToRemove.get()) {
                if (message.contains(stringToFilter)) {
                    return false;
                }
            }

            for (Pattern pattern : CuneiformConfig.Common.Filtering.patternsToRemove.get()) {
                if (pattern.matcher(message).find()) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            //NO-OP
        }

        return true;
    }
}
