package azmalent.cuneiform.filter;

import azmalent.cuneiform.CuneiformConfig.Common.Filtering;
import azmalent.cuneiform.util.ReflectionUtil;
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

    public static boolean shouldIgnoreException(Throwable ex) {
        return ex != null && Filtering.exceptionsToIgnore.anyMatch(clazz ->
            ReflectionUtil.isSubclass(ex.getClass(), clazz)
        );
    }

    public static boolean shouldTruncateException(Throwable ex) {
        return ex != null && Filtering.exceptionsToTruncate.anyMatch(clazz ->
            ReflectionUtil.isSubclass(ex.getClass(), clazz)
        );
    }

    public static boolean isLoggable(String message) {
        try {
            for (String stringToFilter : Filtering.substringsToRemove.get()) {
                if (message.contains(stringToFilter)) {
                    return false;
                }
            }

            for (Pattern pattern : Filtering.patternsToRemove.get()) {
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
