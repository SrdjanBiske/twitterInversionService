package dm.entities;

import java.util.Date;

/**
 * Class that keeps all the needed
 * information about a single post.
 */
public class News {

    /**
     * Position of the post in the list.
     */
    private Integer position;

    /**
     * Publish Date of the post.
     */
    private Date date;

    /**
     * Original post.
     */
    private String original;

    /**
     * Inverted post.
     */
    private String opposite;

    /**
     * Constructor.
     * @param newPosition - number that says which
     *                 position the news have
     * @param newDate - publish date
     * @param originalTweet - original tweet
     * @param oppositeTweet - opposite version of
     *                      the tweet
     */
    public News(
            final Integer newPosition,
            final Date newDate,
            final String originalTweet,
            final String oppositeTweet) {

        this.position = newPosition;
        this.date = newDate;
        this.original = originalTweet;
        this.opposite = oppositeTweet;
    }

    /**
     * Getter for original post.
     * @return original post
     */
    public String getOriginal() {
        return original;
    }

    /**
     * Getter for inverted post.
     * @return inverted post
     */
    public String getOpposite() {
        return opposite;
    }

    /**
     * Setter for original post.
     * @param originalTweet - original post
     */
    public void setOriginal(final String originalTweet) {
        this.original = originalTweet;
    }

    /**
     * Setter for inverted post.
     * @param oppositeTweet - inverted post
     */
    public void setOpposite(final String oppositeTweet) {
        this.opposite = oppositeTweet;
    }

    /**
     * Getter for publish Date.
     * @return publish Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for publish Date.
     * @param newDate - publish Date
     */
    public void setDate(final Date newDate) {
        this.date = newDate;
    }

    /**
     * Getter for position of the post in the list.
     * @return position of the post in the list.
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Setter for the position of the post in the list.
     * @param newPosition - position of the post in the list
     */
    public void setPosition(final Integer newPosition) {
        this.position = newPosition;
    }
}
