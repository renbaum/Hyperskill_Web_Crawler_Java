package crawler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class WebPage{
    String url;
    String html;

    public WebPage(String url) {
        this.url = url;
        getTextFromUrl(url);
    }

    String getTextFromUrl(String url) {
        html = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<String> contentTypeValues = response.headers().allValues("Content-Type");
            for(String s : contentTypeValues) {
                if(s.contains("text/html")) {
                    html = response.body();
                }
            }
        } catch (Exception e) {
        }
        return html;
    }

    String getTitle() {
        return extractTitle(html);
    }

    String extractTitle(String html) {
        String title = "";
        int start = html.indexOf("<title>");
        int end = html.indexOf("</title>");
        if (start != -1 && end != -1) {
            title = html.substring(start + 7, end);
        }
        return title;
    }

    List<String> getLinks(){
        List<String> links = new ArrayList<>()
                ;
        if(html.isEmpty()) {return links;}

            String[] linksFromBody = html.split("<a href=\"|<a href='");

            for (int i = 1; i < linksFromBody.length; i++) {
                String link = linksFromBody[i].split("\"|'")[0];

                if (isRelativeLink(link)) {
                    link = toAbsoluteLink(url, link);
                }
                links.add(link);
            }
        return links;
    }

    boolean isRelativeLink(String link) {
        return !link.startsWith("http://") && !link.startsWith("https://");
    }

    String toAbsoluteLink(String baseUrl, String relativeLink) {
        try {
            URI baseUri = new URI(baseUrl);
            return baseUri.resolve(relativeLink).toString();
        } catch (Exception e) {
            return relativeLink;
        }
    }


}
