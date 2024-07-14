# HtmlProcessor - advanced html page scraper
1. Parse with CSS style
2. Parse with original JS listener
3. Parse with original image assets
4. Customizable target URL & output path

# Screenshot
| ![code](/ART/code.png) | ![result](/ART/result.png) |
|------------------------|----------------------------|

# Usage
## Add import in your class

```java
import io.pcholkin.processor.HtmlProcessor;
```
## Invoke executor
```java
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
```
