
package com.example.versionchecker;

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
}
