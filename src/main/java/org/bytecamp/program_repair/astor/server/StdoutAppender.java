package org.bytecamp.program_repair.astor.server;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

public class StdoutAppender extends AbstractAppender {
    public static boolean enabled = true;

    public static StdoutAppender createAppender(String name) {
        return new StdoutAppender(name);
    }

    private StdoutAppender(String name) {
        super(name, null, null, true, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(final LogEvent event) {
        System.out.println(event.getMessage().getFormattedMessage());
    }

}