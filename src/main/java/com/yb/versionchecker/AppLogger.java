package com.yb.versionchecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class AppLogger {

    private static Logger logger;

    public static synchronized Logger getLogger() {
        if (logger != null) return logger;

        logger = Logger.getLogger("VersionCheckerLogger");

        Logger rootLogger = Logger.getLogger("");
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }

        try {
            Files.createDirectories(Paths.get("logs"));

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String logFileName = "logs/log_" + timestamp + ".log";

            FileHandler fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.ALL);


            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }

        return logger;
    }
}
