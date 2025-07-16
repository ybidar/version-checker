
package com.yb.versionchecker;

import java.util.ArrayList;
import java.util.List;

public class VersionInfo {
    public String ios;
    public String ipados;
    public String android;

    public VersionInfo() {}

    public VersionInfo(String ios, String ipados, String android) {
        this.ios = ios;
        this.ipados = ipados;
        this.android = android;
    }

    public boolean isDifferent(VersionInfo other) {
        return !this.ios.equals(other.ios) || !this.ipados.equals(other.ipados) || !this.android.equals(other.android);
    }

    public List<String> getChangedOS(VersionInfo other) {
        List<String> changed = new ArrayList<>();
        if (!this.ios.equals(other.ios)) changed.add("iOS");
        if (!this.ipados.equals(other.ipados)) changed.add("iPadOS");
        if (!this.android.equals(other.android)) changed.add("Android");
        return changed;
    }
}
