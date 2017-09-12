package service.google;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import service.twitter.TwitterService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * We use Google Search for finding the right
 * Twitter account for the specified name.
 * This class contains methods for getting the
 * username of the desired account.
 */
public final class GoogleService {

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private GoogleService() {
    }

    /**
    * The method takes the name of target person as an argument
    * and tries to finds his/her Twitter account's url.
    * @param person - name of the target person
    * @return - url of person's Twitter account, NULL if
    *           nothing is found or if an exception is thrown
    */
    public static String findTwitterAccountFor(final String person) {

        String copy = person.replaceAll("\\s+", "+");

        String google = "http://www.google.com/search?q=";
        String search = "+twitter";
        String userAgent = "Mozilla/5.0";

        String url;
        try {
            String searchUrl = google + copy + search + "&num=10";

            Elements links = Jsoup.connect(searchUrl)
                    .userAgent(userAgent).get().select(".g>.r>a");

            for (Element link : links) {
                url = link.absUrl("href");
                url = URLDecoder.decode(url.substring(url.indexOf('=') + 1,
                        url.indexOf('&')), "UTF-8");

                if (!url.startsWith("http")) {
                    continue;
                }

                if (url.contains("twitter.com")) {
                    if (copy.contains("twitter")
                            && TwitterService.isTwitterOfficialPage(url)
                            || !TwitterService.isTwitterOfficialPage(url)) {

                        return findUsernameInHtml(url);
                    }
                }
            }

            return null;

        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Parses the username of the Twitter account
     * from the HTML of the page.
     * @param url - address to connect
     * @return the username of the desired account
     */
    private static String findUsernameInHtml(final String url) {
        String twitterPageHtml = connectAndGetResponse(url);

        Pattern p = Pattern.compile(
                "<b class=\"u-linkComplex-target\">(.*)</b>");
        Matcher matcher = p.matcher(twitterPageHtml);

        if (matcher.find()) {
            try {
                String result = matcher.group(1);
                result = result.substring(0, result.indexOf("</b>"));
                return result;

            } catch (IllegalStateException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
    * This method connects to the website and
    * get it's response.
    * @param address - url of the website
    * @return - the response from the website
    */
    public static String connectAndGetResponse(final String address) {
        try {
            StringBuilder response;

            URL url = new URL(address);
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0");

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }

            rd.close();
            return response.toString();

        } catch (IOException e) {
            return "";
        }
    }

}
