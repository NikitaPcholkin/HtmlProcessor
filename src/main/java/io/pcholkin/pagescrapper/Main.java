package io.pcholkin.pagescrapper;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        try {
            HtmlProcessor.process()
                    .buildURL("https://github.com/NikitaPcholkin/")
                    .parseCssStyle(true)
                    .parseJs(true)
                    .parseImages(true)
                    .outputFilePath("output.html")
                    .build();
            System.out.println("HTML file saved with name: output.html");
        } catch (IOException | InterruptedException | ScriptException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
