package be.brambasiel.unpacker.gui.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LogStream {
    private final Logger logger;

    private final StringBuilder builder;
    private final List<LogStreamListener> listeners;

    public LogStream(String loggerName) {
        this.builder = new StringBuilder();
        this.listeners = new ArrayList<>();
        this.logger = Logger.getLogger(loggerName);
    }

    public void info(String text){
        logger.info(text);
        push("INFO: " + text);
    }

    public void warn(String text){
        logger.warning(text);
        push("WARN: " + text);
    }

    public void error(String text){
        logger.severe(text);
        push("ERROR: " + text);
    }

    private void push(String text){
        builder.append(text);
        listeners.forEach(listener -> listener.receivedPushedText(text));
    }

    public void addListener(LogStreamListener listener){
        listeners.add(listener);
    }

    public String getText(){
        return builder.toString();
    }
}
