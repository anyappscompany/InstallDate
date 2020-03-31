package ua.com.anyapps.installdate;

import android.graphics.drawable.Drawable;

import java.util.Comparator;

public class OneAppInfo{
    public String appName;
    public String applicationLabel;
    public long installDate;
    public long lastUpdateDate;
    public Drawable appIcon;

    public OneAppInfo(String appName, String applicationLabel,long installDate, long lastUpdateDate, Drawable appIcon) {
        this.appName = appName;
        this.installDate = installDate;
        this.lastUpdateDate = lastUpdateDate;
        this.appIcon = appIcon;
        this.applicationLabel = applicationLabel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getApplicationLabel() {
        return applicationLabel;
    }

    public void setApplicationLabel(String applicationLabel) {
        this.applicationLabel = applicationLabel;
    }

    public long getInstallDate() {
        return installDate;
    }

    public void setInstallDate(long installDate) {
        this.installDate = installDate;
    }

    public long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}

