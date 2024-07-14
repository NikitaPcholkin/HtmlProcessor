# HtmlProcessor - advanced html page scraper
1. Parse with CSS style
2. Parse with original JS listener
3. Parse with original image assets
4. Customizable target URL & output path

# Screenshot
| ![code](/ART/code.png) | ![result](/ART/result.png) |
|------------------------|----------------------------|

## Add it in your root build.gradle at the end of repositories:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

## Add the dependency
```groovy
dependencies {
    implementation 'com.github.NikitaPcholkin:HtmlProcessor:1.0'
}
```
# Usage
## Add import in your class
```java
import io.pcholkin.pagescrapper.HtmlProcessor;
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
