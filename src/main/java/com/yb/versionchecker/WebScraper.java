
package com.yb.versionchecker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.yb.versionchecker.Notifier.sendNotification;
import static com.yb.versionchecker.VersionInfo.getCurrentVersions;

public class WebScraper {

    private static final Logger logger = AppLogger.getLogger();
    public static VersionInfo fetchLatestVersions() throws Exception {
        try {

            String viOS = getLatestiOSVersion();
            String viPadOS = getLatestiPadOSVersion();
            String vAndroid = getLatestAndroidVersion();
            String pixelBeta = "";
            String pixelpatch = getLatestPixelMonthlyPatchVersion();
            String samsungpatch = getLatestSamsungPatch();

            VersionInfo latestVersions = new VersionInfo();
            latestVersions.setVersion("ios",viOS);
            latestVersions.setVersion("ipados",viPadOS);
            latestVersions.setVersion("android",vAndroid);
            latestVersions.setVersion("pixelpatch",pixelpatch);
            latestVersions.setVersion("samsungpatch",samsungpatch);

            return latestVersions;

        }catch(Exception e){
            logger.log(Level.SEVERE,"Exception caught in webscraper class",e);
            sendNotification("Exception caught in webscraper. Pleae check the logs. \n"+e.toString(),"debug");
            return getCurrentVersions();
        }

    }

    public static String getLatestiOSVersion() throws Exception{
        String releaseInfo = "";
        Document doc = Jsoup.connect("https://developer.apple.com/news/releases/").get();
        Elements articles = doc.select("a[class='article-title external-link']>h2");

        for (Element link : articles) {
            releaseInfo = link.text();
            if (releaseInfo.contains("iOS"))
                break;
        }
        logger.log(Level.INFO,"Latest ios version:" + releaseInfo);
        return releaseInfo;
    }

    public static String getLatestiPadOSVersion() throws Exception{
        String releaseInfo = "";
        Document doc = Jsoup.connect("https://developer.apple.com/news/releases/").get();
        Elements articles = doc.select("a[class='article-title external-link']>h2");
        for (Element link : articles) {
            releaseInfo = link.text();
            if (releaseInfo.contains("iPadOS"))
                break;
        }
        logger.log(Level.INFO,"Latest ipados version:" + releaseInfo);
        return releaseInfo;
    }

    public static String getLatestAndroidVersion() throws Exception{
        String releaseInfo = "";
        Document doc = Jsoup.connect("https://developer.android.com/about/versions/").get();
        Elements items = doc.select("li.devsite-nav-item");

        for (int i = 0; i < items.size(); i++) {
            Element current = items.get(i);
            Element span = current.selectFirst("span");
            if (span != null && span.text().equals("All Android releases")) {
                if (i + 1 < items.size()) {
                    Element nextLi = items.get(i + 1);
                    Element nextSpan = nextLi.selectFirst("span");
                    if (nextSpan != null) {
                        logger.log(Level.INFO,"Latest Android Version: " + (releaseInfo = nextSpan.text()));
                    }
                }
                break;
            }
        }

        return releaseInfo;
    }

    public static String getLatestPixelMonthlyPatchVersion() throws Exception{
        String releaseInfo = "";
        Document doc = Jsoup.connect("https://source.android.com/docs/security/bulletin/pixel").get();
        Element h2 = doc.select("h2").stream()
                .filter(e -> e.text().equalsIgnoreCase("bulletins"))
                .findFirst()
                .orElse(null);

        if (h2 != null) {
            Element table = h2.nextElementSibling();
            while (table != null && !table.tagName().equals("table")) {
                table = table.nextElementSibling();
                logger.log(Level.FINE,"Table Found");
            }

            if (table != null) {
                Element row = table.select("tr").get(1);
                if (row.isBlock()) {
                    logger.log(Level.FINE,"Row Found");
                    Element td = row.select("td").get(3);
                    String value = td.text();
                    logger.log(Level.INFO,"Fourth column value: " + (releaseInfo=value));
                } else {
                    logger.log(Level.SEVERE,"No rows in table.");
                }
            } else {
                logger.log(Level.SEVERE,"No table found after <h2>.");
            }
        } else {

            logger.log(Level.SEVERE,"No <h2> with text 'bulletin' found.");
        }
        return releaseInfo;
    }

    public static String getLatestSamsungPatch() throws Exception{
        String releaseInfo = "";
        Document doc = Jsoup.connect("https://security.samsungmobile.com/securityUpdate.smsb").get();
        Elements links = doc.select("div.accordion_banner > div.wrap_acc > div > a");

        if (!links.isEmpty()) {
            Element firstLink = links.first();
            if(firstLink!=null) {
                String text = firstLink.text();
                logger.log(Level.INFO,"Latest Samsung Monthly Patch available " + (releaseInfo=text));
            }{
                logger.log(Level.SEVERE,"Identified link on Samsung page is empty");
            }
        }else {
            logger.log(Level.SEVERE,"No links matching given locator found on Samsung page");
        }

        return releaseInfo;
    }

}
