
package com.yb.versionchecker;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class VersionInfo {

    private final Map<String, String> osVersions = new HashMap<>();

    private static final String STATE_FILE = "src/main/resources/version_state.json";
    private static final Logger logger = AppLogger.getLogger();

    private Map<String, String> allVersions;

    public Map<String, String> getAllVersions() {
        return allVersions;
    }

    public void setAllVersions(Map<String, String> allVersions) {
        this.allVersions = allVersions;
    }

    public void setVersion(String os, String version) {
        if(allVersions==null)
            allVersions=new HashMap<>();
        this.allVersions.put(os,version);
    }
    public String getVersion(String os) {
        return osVersions.get(os.toLowerCase());
    }
    public Set<String> getAllOSes() {
        return osVersions.keySet();
    }

    public boolean isDifferent(VersionInfo other) {
        if (other == null || other.allVersions == null) return true;
        for (String os : allVersions.keySet()) {
            if (!Objects.equals(allVersions.get(os), other.allVersions.get(os))) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getChangedOSes(VersionInfo other) {
        Map<String, String> changed = new HashMap<>();
        for (String os : allVersions.keySet()) {
            String oldVersion = other.allVersions.get(os);
            String newVersion = allVersions.get(os);
            if (!Objects.equals(oldVersion, newVersion)) {
                changed.put(os, newVersion);
            }
        }
        return changed;
    }

    public static VersionInfo getCurrentVersions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path path = Paths.get(STATE_FILE);
        return mapper.readValue(path.toFile(), VersionInfo.class);
    }

    public static void updateVersionInfo(VersionInfo latest) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path path = Paths.get(STATE_FILE);
        mapper.writeValue(path.toFile(), latest);
    }
}
