
package com.yb.versionchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.*;
import java.util.List;

public class VersionChecker {

    private static final String STATE_FILE = "src/main/resources/version_state.json";

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Path path = Paths.get(STATE_FILE);
        VersionInfo previous = mapper.readValue(path.toFile(), VersionInfo.class);
        VersionInfo latest = WebScraper.fetchLatestVersions();;


            Notifier.sendNotification(latest,previous);
            if(latest.isDifferent(previous)){
            mapper.writeValue(path.toFile(), latest);
            System.out.println("version change.");
        } else {
            System.out.println("No version change.");
        }
    }
}
