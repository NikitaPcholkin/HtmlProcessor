package io.pcholkin.pagescrapper.types;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlValidator {
    public static boolean isValid(String url) {
        try {
            new URI(url).parseServerAuthority();
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}