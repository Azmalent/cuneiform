package azmalent.cuneiform.filter;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

class CuneiformLogFilter extends AbstractFilter implements Filter {
    //Log4j loggers
    @Override
    public Result filter(LogEvent event) {
        boolean loggable = FilteringHandler.checkLogMessage(event.getLoggerName(), event.getMessage().getFormattedMessage(), event.getThrown());
        return loggable ? Result.NEUTRAL : Result.DENY;
    }

    //Java loggers
    @Override
    public boolean isLoggable(LogRecord record) {
        return FilteringHandler.checkLogMessage(record.getLoggerName(), record.getMessage(), record.getThrown());
    }
}
