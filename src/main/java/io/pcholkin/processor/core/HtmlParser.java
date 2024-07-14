package io.pcholkin.processor.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {
    private Document doc;

    public HtmlParser(String htmlContent, String baseUrl) {
        this.doc = Jsoup.parse(htmlContent, baseUrl);
    }

    public void inlineCssAndJs(String baseUrl, boolean inlineCss, boolean inlineJs) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        if (inlineCss) {
            Elements links = doc.select("link[rel=stylesheet]");
            for (Element link : links) {
                String cssUrl = link.absUrl("href");
                if (!cssUrl.startsWith("http")) {
                    cssUrl = baseUrl + cssUrl;
                }
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(cssUrl))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                String cssContent = response.body();
                Element styleTag = doc.createElement("style");
                styleTag.appendText(cssContent);
                link.replaceWith(styleTag);
            }
        }

        if (inlineJs) {
            Elements scripts = doc.select("script[src]");
            for (Element script : scripts) {
                String jsUrl = script.absUrl("src");
                if (!jsUrl.startsWith("http")) {
                    jsUrl = baseUrl + jsUrl;
                }
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(jsUrl))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                String jsContent = response.body();
                Element scriptTag = doc.createElement("script");
                scriptTag.appendText(jsContent);
                script.replaceWith(scriptTag);
            }
        }
    }

    public void makeLinksAbsolute(String baseUrl, boolean processImages) {
        if (processImages) {
            Elements elementsWithSrc = doc.select("[src]");
            for (Element element : elementsWithSrc) {
                String absUrl = element.absUrl("src");
                element.attr("src", absUrl);
            }
        }

        Elements elementsWithHref = doc.select("[href]");
        for (Element element : elementsWithHref) {
            String absUrl = element.absUrl("href");
            element.attr("href", absUrl);
        }

        Elements elementsWithStyle = doc.select("[style]");
        Pattern pattern = Pattern.compile("url\\(['\"]?(.*?)['\"]?\\)");
        for (Element element : elementsWithStyle) {
            String style = element.attr("style");
            Matcher matcher = pattern.matcher(style);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String relativeUrl = matcher.group(1);
                String absoluteUrl = element.absUrl(relativeUrl);
                matcher.appendReplacement(sb, "url('" + absoluteUrl + "')");
            }
            matcher.appendTail(sb);
            element.attr("style", sb.toString());
        }
    }

    public Document getDocument() {
        return this.doc;
    }
}