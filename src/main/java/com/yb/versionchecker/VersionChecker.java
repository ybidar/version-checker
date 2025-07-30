
package com.yb.versionchecker;

import java.nio.file.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.yb.versionchecker.VersionInfo.updateVersionInfo;

public class VersionChecker {
    private static final Logger logger = AppLogger.getLogger();


    public static void main(String[] args) throws Exception {
        VersionInfo previous = VersionInfo.getCurrentVersions();
        VersionInfo latest = WebScraper.fetchLatestVersions();;

        if(latest.isDifferent(previous)){
            updateVersionInfo(latest);
            logger.log(Level.INFO,"New Version Identified. Please check and take appropriate action.");
        } else {
            logger.log(Level.INFO,"No version change.");
        }

        Notifier.sendNotification(getNotificationMessage(previous,latest),"updates");
    }

    public static String getNotificationMessage(VersionInfo oldVersion, VersionInfo newVersion){
        StringBuilder emailBody = new StringBuilder();
        if(!newVersion.isDifferent(oldVersion)){
            emailBody.append("There are no new updates at this time.");
        }else {
            emailBody.append("New version updates detected:\n\n");

            Map<String, String> changes = newVersion.getChangedOSes(oldVersion);
            for (Map.Entry<String, String> entry : changes.entrySet()) {
                emailBody.append(entry.getKey().toUpperCase())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n");
            }
        }
        return emailBody.toString();
    }
}
