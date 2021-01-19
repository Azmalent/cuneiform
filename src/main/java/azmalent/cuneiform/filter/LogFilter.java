package azmalent.cuneiform.filter;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

class LogFilter extends AbstractFilter implements Filter {
    //Log4j loggers
    @Override
    public Result filter(LogEvent event) {
        String line = String.format("[%s]: %s", event.getLoggerName(), event.getMessage().getFormattedMessage());

        boolean isLoggable = FilteringUtil.isLoggable(line);
        if (isLoggable) {
            Throwable exception = event.getThrown();
            FilteringUtil.truncateException(exception);
        }

        return isLoggable ? Result.NEUTRAL : Result.DENY;

    }

    //Java loggers
    @Override
    public boolean isLoggable(LogRecord record) {
        String line = String.format("[%s]: %s", record.getLoggerName(), record.getMessage());

        boolean isLoggable = FilteringUtil.isLoggable(line);
        if (isLoggable) {
            Throwable exception = record.getThrown();
            FilteringUtil.truncateException(exception);
        }
        return isLoggable;
    }
}
