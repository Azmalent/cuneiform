package azmalent.cuneiform.filter;

import azmalent.cuneiform.CuneiformConfig.Filtering;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.regex.Pattern;

public class FilteringHandler {
    public static void applyLogFilter() {
        var filter = new CuneiformLogFilter();

        java.util.logging.Logger.getLogger("").setFilter(filter);

        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(filter);

        final LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
        for (final LoggerConfig logger : logContext.getConfiguration().getLoggers().values()) {
            logger.addFilter(filter);
        }
    }

    public static boolean checkLogMessage(String loggerName, String message, Throwable exception) {
        if (exception != null) {
            if (Filtering.exceptionsToIgnore.get().contains(exception.getClass())) {
                return false;
            }

            if (Filtering.exceptionsToTruncate.get().contains(exception.getClass())) {
                exception.setStackTrace(new StackTraceElement[]{});
            }

            return true;
        }

        boolean whitelisted = Filtering.loggerWhitelist.get().contains(loggerName);
        boolean blacklisted = Filtering.loggerBlacklist.get().contains(loggerName);

        return whitelisted || !blacklisted && isLoggable("[" + loggerName + "]: " + message);
    }

    private static boolean isLoggable(String message) {
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
