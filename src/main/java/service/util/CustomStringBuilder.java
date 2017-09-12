package service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * String builder adapted for the needs
 * of this application.
 */
public class CustomStringBuilder {

    /**
    * The last String that is appended.
    */
    private String lastAppended;

    /**
    * List of all appended Strings.
    */
    private List<String> appendings;

    /**
    * Initializes the List of appended
     * Strings.
    */
    public CustomStringBuilder() {
        appendings = new ArrayList<>();
    }

    /**
     * Gives back the last String appended.
     * @return the last String appended
     */
    public String getLastAppended() {
        return lastAppended;
    }

    /**
     * Appends a new String to the List.
     * @param appending - new String to be
     *                  appended
     * @return the resulting CustomStringBuilder
     */
    public CustomStringBuilder append(
            final String appending) {

        lastAppended = appending;
        appendings.add(appending);

        return this;
    }

    /**
     * Gives back the String on the given position.
     * @param position - position of the String that
     *                 we need
     * @return the String on the given position
     */
    public String get(final int position) {
        return appendings.get(position);
    }

    @Override
    public final String toString() {
        StringBuilder result = new StringBuilder();
        for (String string : appendings) {

            if (Objects.equals(string, appendings
                    .get(appendings.size() - 1))) {

                result.append(string);
            } else {
                if (string != null && !string.equals("")) {
                    result.append(string).append(" ");
                }
            }
        }

        return result.toString();
    }

    /**
     * Undo the last appending.
     * @return the resulting CustomStringBuilder
     */
    public CustomStringBuilder undo() {
        if (appendings.size() > 0) {
            appendings.remove(appendings.size() - 1);
        }
        return this;
    }

}
