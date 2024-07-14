package io.pcholkin.pagescrapper;

import io.pcholkin.pagescrapper.core.HtmlFetcher;
import io.pcholkin.pagescrapper.core.HtmlParser;
import io.pcholkin.pagescrapper.types.HtmlSaver;
import io.pcholkin.pagescrapper.types.UrlValidator;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URISyntaxException;

public class HtmlProcessor {
    private HtmlFetcher fetcher;
    private HtmlParser parser;

    private String url;
    private String outputFileName;
    private boolean inlineCss;
    private boolean inlineJs;
    private boolean processImages;

    private HtmlProcessor() {
        this.fetcher = new HtmlFetcher();
        this.inlineCss = true;
        this.inlineJs = true;
        this.processImages = true;
    }

    public static Builder process() {
        return new Builder();
    }

    public static class Builder {
        private HtmlProcessor processor;

        public Builder() {
            this.processor = new HtmlProcessor();
        }

        public Builder buildURL(String url) {
            if (!UrlValidator.isValid(url)) {
                throw new IllegalArgumentException("Invalid URL");
            }
            processor.url = url;
            return this;
        }

        public Builder outputFilePath(String outputFileName) {
            processor.outputFileName = outputFileName;
            return this;
        }

        public Builder parseCssStyle(boolean inlineCss) {
            processor.inlineCss = inlineCss;
            return this;
        }

        public Builder parseJs(boolean inlineJs) {
            processor.inlineJs = inlineJs;
            return this;
        }

        public Builder parseImages(boolean processImages) {
            processor.processImages = processImages;
            return this;
        }

        public HtmlProcessor build() throws IOException, InterruptedException, ScriptException, URISyntaxException {
            if (processor.url == null) {
                throw new IllegalStateException("URL must be set");
            }
            if (processor.outputFileName == null) {
                processor.outputFileName = "output.html";
            }
            processor.processHtml();
            return processor;
        }
    }

    private void processHtml() throws IOException, InterruptedException, ScriptException, URISyntaxException {
        String htmlContent = fetcher.fetchHtml(url);
        this.parser = new HtmlParser(htmlContent, url);
        parser.inlineCssAndJs(url, inlineCss, inlineJs);
        parser.makeLinksAbsolute(url, processImages);
        HtmlSaver.saveHtml(parser.getDocument(), outputFileName);
    }
}
