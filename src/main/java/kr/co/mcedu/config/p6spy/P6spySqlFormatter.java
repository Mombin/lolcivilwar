package kr.co.mcedu.config.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6spySqlFormatter implements MessageFormattingStrategy {
    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category,
            final String prepared, final String sql, final String url) {
        return sql;
    }
}
