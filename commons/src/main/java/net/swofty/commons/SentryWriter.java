package net.swofty.commons;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import io.sentry.Sentry;
import io.sentry.SentryLogLevel;
import org.tinylog.Level;
import org.tinylog.core.ConfigurationParser;
import org.tinylog.core.LogEntry;
import org.tinylog.core.LogEntryValue;
import org.tinylog.provider.InternalLogger;
import org.tinylog.writers.AbstractFormatPatternWriter;


public final class SentryWriter extends AbstractFormatPatternWriter {

    private final Level errorLevel;

    public SentryWriter() {
        this(Collections.<String, String>emptyMap());
    }

    /**
     * @param properties
     *            Configuration for writer
     */
    public SentryWriter(final Map<String, String> properties) {
        super(properties);

        // Set the default level for stderr logging
        Level levelStream = Level.WARN;

        // Check stream property
        String stream = getStringValue("stream");
        if (stream != null) {
            // Check whether we have the err@LEVEL syntax
            String[] streams = stream.split("@", 2);
            if (streams.length == 2) {
                levelStream = ConfigurationParser.parse(streams[1], levelStream);
                if (!streams[0].equals("err")) {
                    InternalLogger.log(Level.ERROR, "Stream with level must be \"err\", \"" + streams[0] + "\" is an invalid name");
                }
                stream = null;
            }
        }

        if (stream == null) {
            errorLevel = levelStream;
        } else if ("err".equalsIgnoreCase(stream)) {
            errorLevel = Level.TRACE;
        } else if ("out".equalsIgnoreCase(stream)) {
            errorLevel = Level.OFF;
        } else {
            InternalLogger.log(Level.ERROR, "Stream must be \"out\" or \"err\", \"" + stream + "\" is an invalid stream name");
            errorLevel = levelStream;
        }
    }

    @Override
    public Collection<LogEntryValue> getRequiredLogEntryValues() {
        Collection<LogEntryValue> logEntryValues = super.getRequiredLogEntryValues();
        logEntryValues.add(LogEntryValue.LEVEL);
        return logEntryValues;
    }

    @Override
    public void write(final LogEntry logEntry) {
        SentryLogLevel sentryLevel;
        try {
            sentryLevel = SentryLogLevel.valueOf(logEntry.getLevel().name());
        } catch (IllegalArgumentException e) {
            sentryLevel = SentryLogLevel.INFO;
        }

        Sentry.logger().log(sentryLevel, render(logEntry));
        if (logEntry.getLevel().ordinal() < errorLevel.ordinal()) {
            System.out.print(render(logEntry));
        } else {
            System.err.print(render(logEntry));
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

}