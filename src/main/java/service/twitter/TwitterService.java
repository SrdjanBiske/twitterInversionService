package service.twitter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.Paging;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.ArrayList;

/**
 * Class for performing Twitter
 * operations.
 */
public class TwitterService {

    /**
     * Used for logging events that happen in this class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(TwitterService.class);
    }

    /**
     * User that we're interested in.
     */
    private final String user;

    /**
     * Counter for Twitter statuses.
     */
    private static final int COUNT = 100;

    /**
     * Main constructor. Specifies the desired
     * user.
     * @param newUser - desired user
     */
    public TwitterService(final String newUser) {
        this.user = newUser;
    }

    /**
     * Retrieves all available statuses from the
     * user's profile.
     * @return the List of all retrieved Statuses
     */
    public List<Status> allTweets() {
        Twitter twitter = createTwitterAccess().getInstance();

        List<Status> statuses;
        try {
            int pageNumber = 1;
            statuses = new ArrayList<>();

            while (true) {

                int size = statuses.size();
                Paging page = new Paging(pageNumber++, COUNT);
                statuses.addAll(twitter.getUserTimeline(user, page));

                if (statuses.size() == size) {
                    break;
                }
            }

            if (statuses.size() > 0) {
                LOGGER.info("Statuses successfully fetched.");
            }

            return statuses;

        } catch (TwitterException te) {
            return null;
        }
    }

    /**
     * If Google can't find an account with our desired
     * name, it often returns a Twitter home page. We don't
     * accept that as a result. This method recognizes such
     * pages.
     * @param url - url that Google gives us back
     * @return true if it's one of the Twitter's home pages,
     *         false otherwise
     */
    public static boolean isTwitterOfficialPage(final String url) {
        return url.contains("https://twitter.com/?lang=")
                || url.contains("https://twitter.com/login")
                || url.contains("https://twitter.com/twitter")
                || url.contains("https://twitter.com/search");
    }

    /**
     * Sets the Twitter access data tokens.
     * @return Twitter access utility
     */
    private static TwitterFactory createTwitterAccess() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(
                        "bL8OgxQiUvoetZPNd5JX4lzgs")
                .setOAuthConsumerSecret(
                        "ibD6aMSCyL4s9AJU1A7otkEL0Bv5dUNZxEq8NlsRWV1Cuj5s9N")
                .setOAuthAccessToken(
                        "837361618614960128-ILqBHxiVzVdDRs9OaY6zGiZ2PPJnsVj")
                .setOAuthAccessTokenSecret(
                        "6Ve6yiKYx4LAYwQrI6mR9ruWmMnMZcx6iT26fOh4cQJoR");
        return new TwitterFactory(cb.build());
    }

}
