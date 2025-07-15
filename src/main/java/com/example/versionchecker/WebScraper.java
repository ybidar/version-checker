
package com.example.versionchecker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {

    public static VersionInfo fetchLatestVersions() throws Exception {
        String releaseInfo = "",viOS="",viPadOS="";
        Document doc = Jsoup.connect("https://developer.apple.com/news/releases/").get();
        Elements articles = doc.select("a[class='article-title external-link']>h2");

        for(Element link:articles){
            releaseInfo = link.text();
            if(releaseInfo.contains("iOS"))
                break;
        }

        System.out.println("Latest ios version:"+(viOS=releaseInfo));

        for(Element link:articles){
            releaseInfo = link.text();
            if(releaseInfo.contains("iPadOS"))
                break;
        }
        System.out.println("Latest ipados version:"+(viPadOS=releaseInfo));

        String vAndroid="";
        doc = Jsoup.connect("https://developer.android.com/about/versions/").get();
        Elements items = doc.select("li.devsite-nav-item");

        for (int i = 0; i < items.size(); i++) {
            Element current = items.get(i);
            Element span = current.selectFirst("span");
            if (span != null && span.text().equals("All Android releases")) {
                // Get the next <li>
                if (i + 1 < items.size()) {
                    Element nextLi = items.get(i + 1);
                    Element nextSpan = nextLi.selectFirst("span");
                    if (nextSpan != null) {
                        System.out.println("Next span text: " + (vAndroid=nextSpan.text()));
                    }
                }
                break;
            }
        }

        return new VersionInfo(viOS, viPadOS, vAndroid);
    }
}
