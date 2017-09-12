package service;

import java.util.List;

import dm.entities.News;
import exception.FetchTwitterDataException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.ml.MachineLearning;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.formatter.Pipe;
import service.formatter.PipeMode;
import service.inversion.InversionService;
import service.twitter.TwitterService;
import twitter4j.Status;

/**
 * Class that puts together the Machine Learning part,
 * Twitter part and the Negation part together.
 */
public final class Service {

    /**
     * Used for logging events that happen in this class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(Service.class);
    }

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Service() {
    }

    /**
     * Finds all tweets that the user ever posted and
     * returns them as a list of News. News are an object
     * which stores both original and opposite version of
     * the tweet.
     * @param statuses  - list of statuses tweeted by the
     *                  specified user
     * @return the list of News
     * @throws FetchTwitterDataException if there's a problem
     *         with fetching the data from Twitter
     */
    private static ObservableList<News> convertStatusesToNews(
            final List<Status> statuses)
            throws FetchTwitterDataException {

        ObservableList<News> news = FXCollections.observableArrayList();

        int counter = 1;
        for (Status status : statuses) {

            String str = Pipe.pipe(status.getText(), PipeMode.TESTING);

            if (MachineLearning.classifyTweet(str)) {

                news.add(new News(counter++, status.getCreatedAt(),
                        removeNewlines(status.getText()),
                        InversionService.createInverseVersion(
                                status.getText())));
            }
        }


        return news;
    }

    /**
     * Calls the methods for fetching all the statuses
     * from the specified Twitter account and returns
     * them in an ObservableList.
     * @param user - Twitter username of the user that
     *             we're interested in
     * @return ObservableList containing all available
     *         statuses from the user.
     */
    public static ObservableList<News> setNewsForUser(
            final String user) {

        TwitterService statusHandler = new TwitterService(user);

        try {
            return convertStatusesToNews(statusHandler.allTweets());

        } catch (FetchTwitterDataException exception) {
            return null;
        }
    }

    /**
     * Makes sure that there are no newlines in the
     * resulting Statuses.
     * @param original - original post
     * @return post without newlines
     */
    private static String removeNewlines(
            final String original) {

        StringBuilder resultingStatus = new StringBuilder();
        String[] sentences = original.split(
                "(?<=[.?!;])\\s+(?=\\p{Lu})");

        for (String sentence : sentences) {
            resultingStatus.append(sentence).append("\n");
        }

        return resultingStatus.toString();
    }

}
