//7) Design a Logging Framework (like Log4j)
//
//It should allow clients to log messages at various levels (INFO, DEBUG, ERROR, WARN, etc.),
//configure output formats, and send logs to different destinations (console, file, etc.).
//
//        ✅ SUBPARTS :
//
//* Multiple log levels (INFO, DEBUG, ERROR, WARN, etc.)
//
//* Pluggable appenders (Console, File, etc.)
//
//* Thread-safe logging
//
//* Configurable log level per logger
//
//* Singleton Logger
//
//* Extensible with new log levels or appenders

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

enum LogLevel {
    DEBUG(1), INFO(2), WARN(3), ERROR(4);

    private int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

interface Appender {
    void append(String formattedMessage);
}

class ConsoleAppender implements Appender {
    public void append(String formattedMessage) {
        System.out.println(formattedMessage);
    }
}

class FileAppender implements Appender {
    private BufferedWriter writer;

    public FileAppender(String filename) {
        try {
            writer = new BufferedWriter(new FileWriter(filename, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void append(String formattedMessage) {
        try {
            writer.write(formattedMessage);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class LoggingFramework {
    private static LoggingFramework instance;
    private List<Appender> appenders = new ArrayList<>();
    private LogLevel configuredLevel = LogLevel.DEBUG;

    private LoggingFramework() {
    }

    public static synchronized LoggingFramework getInstance() {
        if (instance == null) {
            instance = new LoggingFramework();
        }
        return instance;
    }

    public void setLogLevel(LogLevel level) {
        this.configuredLevel = level;
    }

    public void addAppender(Appender appender) {
        appenders.add(appender);
    }

    public synchronized void log(LogLevel level, String message) {
        if (level.getLevel() < configuredLevel.getLevel()) return;

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String formatted = "[" + timeStamp + "] [" + level.name() + "] " + message;

        for (Appender a : appenders) {
            a.append(formatted);
        }
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public static void main(String[] args) {
        LoggingFramework logger = LoggingFramework.getInstance();

        logger.setLogLevel(LogLevel.INFO);
        logger.addAppender(new ConsoleAppender());
        logger.addAppender(new FileAppender("app.log"));

        logger.debug("This is a DEBUG message - should be ignored");
        logger.info("This is an INFO message");
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
    }
}



