package org.bytecamp.program_repair.astor.utils;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.NullAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = StdoutAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class StdoutAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "StdoutAppender";

    @PluginFactory
    public static StdoutAppender createAppender(
            @PluginAttribute(value = "name", defaultString = "null") final String name) {
        return new StdoutAppender(name);
    }

    private StdoutAppender(final String name) {
        super(name, null, null, true, Property.EMPTY_ARRAY);
    }

    @Override
    public void append(final LogEvent event) {
        System.out.format("[%s] %s - %s\n", event.getLevel(), event.getLoggerName(), event.getMessage().getFormattedMessage());
    }

}

