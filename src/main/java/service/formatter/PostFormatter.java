package service.formatter;

import org.apache.commons.lang.StringUtils;

import static dm.dao.CharUtility.isCharWithSpaceAfter;
import static dm.dao.CharUtility.isCharWithSpaceBefore;
import static dm.dao.CharUtility.isCharWithSpacesAround;
import static dm.dao.CharUtility.isLetterOrDigit;
import static dm.dao.CharUtility.isNumber;
import static dm.dao.CharUtility.isPunctuation;
import static dm.dao.CharUtility.isSpecialChar;
import static dm.dao.CharUtility.isWhitespace;

/**
 * Class used for formatting the post to a form where
 * it can be normally processed without invalid char
 * position problems (i.e. usa&russia would be one bad
 * example and without this class, it could not be
 * correctly processed).
 */
public final class PostFormatter {

    /**
     * In order to know if we should capitalize letter
     * after punctuation, we have to check if there is
     * a dot on a minimal possible distance before that
     * letter. "U.S. is ok." The distance between letter
     * 'i' and the first dot is 4, and the dot wouldn't
     * be there if it was a normal word and not
     * abbreviation. We need MIN_DIST to differentiate
     * between abbreviations and normal words.
     */
    private static final int MIN_DIST = 4;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private PostFormatter() {
    }

    /**
     * The main method that calls all sub-methods for
     * formatting the post.
     * @param post - retrieved post from Twitter
     * @return formatted post
     */
    public static String format(final String post) {
        String copy = replaceUncoveredChars(post);
        copy = fixSpace(copy);
        copy = fixQuotes(copy);
        copy = deleteUnnecessaryChars(copy);
        copy = clearSpaceAfter(copy);
        copy = clearSpaceBefore(copy);
        copy = makeSpaceAround(copy);
        copy = removeMultipleDots(copy);
        copy = fixNonSpace(copy);
        copy = removeMultipleSpaces(copy);
        copy = isNumberComma(copy);
        copy = fixLinks(copy);
        copy = deleteLeadingSpecialChars(copy);
        copy = removeMultipleDots(copy);
        copy = movePunctuation(copy);
        copy = setUpperCase(copy);

        return copy;
    }

    /**
     * There are some chars that are not covered for the
     * processing. This method replaces them with the
     * covered ones.
     * @param post - post with uncovered chars
     * @return - post with replaced covered chars
     */
    private static String replaceUncoveredChars(final String post) {
        String copy = post.replaceAll("…", ".");
        copy = copy.replaceAll("’", "\'");
        copy = copy.replaceAll("`", "\'");
        copy = copy.replaceAll("—", "-");
        return copy.replaceAll("”", "\"");
    }

    /**
     * Deletes the space between the special chars in order
     * to be able to delete the sufficient chars that are
     * positioned one after another.
     * @param post - post with spaces between special chars
     * @return post without spaces between special chars
     */
    private static String fixSpace(final String post) {
        String copy = post;

        for (int pos1 = 0; pos1 < copy.length() - 1; pos1++) {
            if (isSpecialChar(copy.charAt(pos1))) {

                for (int pos2 = pos1 + 1; pos2 < copy.length(); pos2++) {
                    if (isSpecialChar(copy.charAt(pos2))) {

                        if (isOnlySpace(copy, pos1, pos2)) {
                            copy = copy.substring(0, pos1 + 1)
                                    + copy.substring(pos2, copy.length());
                            break;

                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return copy;
    }

    /**
     * Checks if there are only whitespace chars between
     * the two positions.
     * @param post - post to be checked
     * @param pos1 - starting position
     * @param pos2 - ending position
     * @return true if there are only whitespace chars
     *         between the two positions, false otherwise
     */
    private static boolean isOnlySpace(final String post,
                                       final int pos1,
                                       final int pos2) {

        for (int pos = pos1 + 1; pos < pos2; pos++) {
            if (!isWhitespace(post.charAt(pos))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the number of double quotes in even. If it's
     * not, all of them are removed. If it is, the method makes
     * sure that the spacing around them is correct.
     * @param post - post with possibly incorrect quotes
     * @return post with correct quotes
     */
    private static String fixQuotes(final String post) {
        String copy = post;

        if (StringUtils.countMatches(copy, "\"") % 2 == 1) {
            copy = copy.replaceAll("\"", "");
        }

        int doubleQ = 0;

        for (int pos = 0; pos < copy.length(); pos++) {
            if (copy.charAt(pos) == '\"') {
                copy = setQuotes(copy, pos, ++doubleQ);
                ++pos;
            }
        }

        return copy;
    }

    /**
     * Clears the sufficient space before opening and
     * after closing quotes.
     * @param post - with incorrect space around quotes
     * @param pos - position that is currently being
     *            processed
     * @param counter - quote counter that says which quote
     *                is currently being processed
     * @return post with correct space around the specified
     *         quote
     */
    private static String setQuotes(final String post,
                                    final int pos,
                                    final int counter) {
        String copy = post;

        if (counter % 2 == 1) {
            copy = copy.substring(0, pos) + " "
                    + copy.substring(pos, copy.length());

            copy = clearForward(copy, pos + 1);

        } else {
            copy = copy.substring(0, pos + 1) + " "
                    + copy.substring(pos + 1, copy.length());

            copy = clearBackward(copy, pos - 1);
        }

        return copy;
    }

    /**
     * Deletes all whitespaces that come directly after the
     * given position, including that position.
     * @param post - post to be cleared
     * @param pos - starting position
     * @return - post with no whitespaces directly after
     *           position
     */
    private static String clearForward(final String post,
                                       final int pos) {
        String copy = post;

        if (pos < copy.length()
                && isWhitespace(copy.charAt(pos))) {

            copy = post.substring(0, pos)
                    + copy.substring(pos + 1, copy.length());

            return clearForward(copy, pos);
        }

        return copy;
    }

    /**
     * Deletes all whitespaces that come directly before the
     * given position, including that position.
     * @param post - post to be cleared
     * @param pos - starting position
     * @return - post with no whitespaces directly before
     *           position
     */
    private static String clearBackward(final String post,
                                        final int pos) {
        String copy = post;

        if (pos >= 0
                && isWhitespace(copy.charAt(pos))) {

            copy = copy.substring(0, pos)
                    + copy.substring(pos + 1, copy.length());

            return clearBackward(copy, pos - 1);
        }

        return copy;
    }

    /**
     * Deletes char sequences which aren't in the allowed list.
     * @param post - post with non-allowed char sequences
     * @return post with only allowed char sequences
     */
    private static String deleteUnnecessaryChars(final String post) {
        String copy = post;

        for (int pos = 0; pos < copy.length(); pos++) {
            char c = copy.charAt(pos);

            if (isSpecialChar(c) && pos > 0
                    && isSpecialChar(copy.charAt(pos  - 1))
                    && !isCharSequenceSpecialCase(copy.charAt(pos - 1), c)) {

                copy = copy.substring(0, pos)
                        + copy.substring(pos + 1, copy.length());
                --pos;
            }
        }

        return copy;
    }

    /**
     * These char sequences are allowed one after another.
     * This method checks if c1 and c2 are among allowed
     * char sequences.
     * @param c1 - first char
     * @param c2 - second char
     * @return true if the sequence is allowed, false otherwise
     */
    private static boolean isCharSequenceSpecialCase(final char c1,
                                                     final char c2) {

        return (c1 == '.' && c2 == '.') || (c1 == '.' && c2 == '\"')
                || (c1 == '.' && c2 == '\'') || (c1 == '.' && c2 == ')')
                || (c1 == '.' && c2 == '}') || (c1 == '.' && c2 == ']')
                || (c1 == '\'' && c2 == '.') || (c1 == ')' && c2 == '.')
                || (c1 == '}' && c2 == '.') || (c1 == ']' && c2 == '.')
                || (c1 == '\"' && c2 == '.') || (c1 == '?' && c2 == '\"')
                || (c1 == '?' && c2 == '\'') || (c1 == '?' && c2 == ')')
                || (c1 == '?' && c2 == '}') || (c1 == '?' && c2 == ']')
                || (c1 == '\'' && c2 == '?') || (c1 == ')' && c2 == '?')
                || (c1 == '}' && c2 == '?') || (c1 == ']' && c2 == '?')
                || (c1 == '\"' && c2 == '?') || (c1 == '!' && c2 == '\"')
                || (c1 == '!' && c2 == '\'') || (c1 == '!' && c2 == ')')
                || (c1 == '!' && c2 == '}') || (c1 == '!' && c2 == ']')
                || (c1 == '\'' && c2 == '!') || (c1 == ')' && c2 == '!')
                || (c1 == '}' && c2 == '!') || (c1 == ']' && c2 == '!')
                || (c1 == '\"' && c2 == '!') || (c1 == '\"' && c2 == '}')
                || (c1 == '\"' && c2 == ']') || (c1 == '\"' && c2 == ')')
                || (c1 == '\"' && c2 == '(') || (c1 == '\"' && c2 == '[')
                || (c1 == '\"' && c2 == '{') || (c1 == '\'' && c2 == '}')
                || (c1 == '\'' && c2 == ']') || (c1 == '\'' && c2 == ')')
                || (c1 == '\'' && c2 == '(') || (c1 == '\'' && c2 == '[')
                || (c1 == '\'' && c2 == '{') || (c1 == '}' && c2 == '\"')
                || (c1 == ']' && c2 == '\"') || (c1 == ')' && c2 == '\"')
                || (c1 == '(' && c2 == '\"') || (c1 == '[' && c2 == '\"')
                || (c1 == '{' && c2 == '\"') || (c1 == '}' && c2 == '\'')
                || (c1 == ']' && c2 == '\'') || (c1 == ')' && c2 == '\'')
                || (c1 == '(' && c2 == '\'') || (c1 == '[' && c2 == '\'')
                || (c1 == '{' && c2 == '\'') || (c1 == ':' && c2 == '/')
                || (c1 == '/' && c2 == '/') || (c1 == ',' && c2 == '\"')
                || (c1 == ',' && c2 == '\'') || (c1 == ',' && c2 == ')')
                || (c1 == ',' && c2 == '}') || (c1 == ',' && c2 == ']')
                || (c1 == '\'' && c2 == ',') || (c1 == ')' && c2 == ',')
                || (c1 == '}' && c2 == ',') || (c1 == ']' && c2 == ',')
                || (c1 == '\"' && c2 == ',');
    }

    /**
     * Makes sure that there is no space after the chars with
     * space before.
     * @param post - post with space after the chars with space
     *             before
     * @return post with no space after the chars with space
     *         before
     */
    private static String clearSpaceAfter(final String post) {
        String copy = post.replaceAll("\\(\\s+", " (");
        copy = copy.replaceAll("\\[\\s+", " [");
        return copy.replaceAll("\\{\\s+", " {");
    }

    /**
     * Makes sure that there is no space before the chars with
     * space after.
     * @param post - post with space before the chars with space
     *             after
     * @return post with no space before the chars with space
     *         after
     */
    private static String clearSpaceBefore(final String post) {
        String copy = post.replaceAll("\\s+\\.", ".");
        copy = copy.replaceAll("\\s+\\?", "? ");
        copy = copy.replaceAll("\\s+!", "! ");
        copy = copy.replaceAll("\\s+\\)", ") ");
        copy = copy.replaceAll("\\s+]", "] ");
        copy = copy.replaceAll("\\s+}", "} ");
        copy = copy.replaceAll("\\s+%", "% ");
        copy = copy.replaceAll("\\s+:", ": ");
        copy = copy.replaceAll("\\s+,", ", ");
        return copy.replaceAll("\\s+;", "; ");
    }

    /**
     * Makes sure that there is no space around the chars with
     * space around.
     * @param post - post with space around the chars with space
     *             around
     * @return post with no space around the chars with space
     *         around
     */
    private static String makeSpaceAround(final String post) {
        String copy = post.replaceAll("\\*", " * ");
        copy = copy.replaceAll("\\+", " + ");
        copy = copy.replaceAll("&", " & ");
        copy = copy.replaceAll("/", " / ");
        return copy.replaceAll("-", " - ");
    }

    /**
     * Turns every array of dots into only one dot.
     * @param post - post with possibly arrays of dots
     * @return post with no arrays of dots
     */
    private static String removeMultipleDots(final String post) {
        return post.replaceAll("\\.{2,}", ".");
    }

    /**
     * Makes sure that there are spaces before the chars
     * with space before and after the chars with space after.
     * @param post - post with possibly incorrect spaces
     * @return post with correct spaces
     */
    private static String fixNonSpace(final String post) {
        String copy = post;

        for (int pos = 0; pos < copy.length(); pos++) {
            char toCheck = copy.charAt(pos);

            if (isCharWithSpaceAfter(toCheck)) {

                if (pos + 2 < copy.length()
                        && (isLetterOrDigit(copy.charAt(pos + 1))
                        || (pos > 0 && copy.charAt(pos + 1) == '@'))
                        && copy.charAt(pos + 2) != '.') {

                    copy = copy.substring(0, pos + 1) + " "
                            + copy.substring(pos + 1, copy.length());
                    ++pos;
                }
            }

            if (isCharWithSpaceBefore(toCheck)) {
                if (pos > 0 && (isLetterOrDigit(copy.charAt(pos - 1))
                        || (pos > 0 && copy.charAt(pos - 1) == '@'))) {

                    copy = copy.substring(0, pos) + " "
                            + copy.substring(pos, copy.length());
                    ++pos;
                }
            }
        }

        return copy;
    }

    /**
     * Makes sure that there is no multiple spaces one
     * after another.
     * @param post - post with possibly multiple spaces
     *             one after another
     * @return post with no spaces one after another
     */
    private static String removeMultipleSpaces(final String post) {
        return post.replaceAll("\\s+", " ");
    }

    /**
     * Makes sure that there is no space after
     * dots and commas in numbers.
     * @param post - post with possible spaces after
     *             comas and dots in numbers
     * @return post without spaces after comas and
     *         dots in numbers
     */
    private static String isNumberComma(final String post) {
        String copy = post;
        for (int pos = 0; pos < copy.length(); pos++) {

            if ((copy.charAt(pos) == '.'
                    || copy.charAt(pos) == ',')
                    && pos > 0
                    && isNumber(copy.charAt(pos - 1))
                    && pos + 2 < copy.length()
                    && isNumber(copy.charAt(pos + 2))) {

                copy = copy.substring(0, pos + 1)
                        + copy.substring(pos + 2, copy.length());
            }
        }
        return copy;
    }

    /**
     * Makes sures that the links have no spaces between
     * special characters in them.
     * @param post - post with links with spaces
     * @return post with correctly written links
     */
    private static String fixLinks(final String post) {
        return post.replaceAll(": / / t\\. co / ", "://t.co/");
    }

    /**
     * Deletes special characters from the beginning of the
     * post, since they are usually just written without
     * following the grammatical rules.
     * @param post - with special chars on leading positions
     * @return post without special chars on leading positions
     */
    private static String deleteLeadingSpecialChars(final String post) {
        String copy = post;

        if (copy.length() > 0) {
            char toCheck = copy.charAt(0);

            if (isCharWithSpaceAfter(toCheck)
                    || isCharWithSpaceBefore(toCheck)
                    || isCharWithSpacesAround(toCheck)
                    || isWhitespace(toCheck)) {

                copy = copy.substring(1, copy.length());
                copy = deleteLeadingSpecialChars(copy);
            }
        }

        return copy;
    }

    /**
     * If there are two special chars one after another
     * and the first one is punctuation, it will be
     * moved to the second position.
     * @param post - post with punctuation char before
     *             some other char
     * @return post with punctuation char after
     *             other chars
     */
    private static String movePunctuation(final String post) {
        String copy = post.replaceAll("\\.\\)", ").");
        copy = copy.replaceAll("\\.]", "].");
        copy = copy.replaceAll("\\.}", "}.");
        copy = copy.replaceAll("\\.\"", "\".");
        copy = copy.replaceAll("\\.\'", "\'.");
        copy = copy.replaceAll("\\?\\)", ")?");
        copy = copy.replaceAll("\\?]", "]?");
        copy = copy.replaceAll("\\?}", "}?");
        copy = copy.replaceAll("\\?\"", "\"?");
        copy = copy.replaceAll("\\?\'", "\'?");
        copy = copy.replaceAll("!\\)", ")!");
        copy = copy.replaceAll("!]", "]!");
        copy = copy.replaceAll("!}", "}!");
        copy = copy.replaceAll("!\"", "\"!");
        copy = copy.replaceAll("!\'", "\'!");
        copy = copy.replaceAll(",\\)", "),");
        copy = copy.replaceAll(",]", "],");
        copy = copy.replaceAll(",}", "},");
        copy = copy.replaceAll(",\"", "\",");
        copy = copy.replaceAll(",\'", "\',");
        return copy;
    }

    /**
     * Makes sure that the new sentence starts with an upper
     * case.
     * @param post - post with possibly lower case on the
     *             sentence start
     * @return post with upper case on the sentence start
     */
    private static String setUpperCase(final String post) {
        String copy = post;
        for (int pos = MIN_DIST; pos < copy.length(); pos++) {

            if (isPunctuation(copy.charAt(pos - 2))
                    && copy.charAt(pos - MIN_DIST) != ' '
                    && copy.charAt(pos - MIN_DIST) != '.'
                    && copy.charAt(pos - MIN_DIST) != 'j'
                    && copy.charAt(pos - MIN_DIST + 1) != 'r') {

                copy = copy.substring(0, pos)
                        + ("" + copy.charAt(pos)).toUpperCase()
                        + copy.substring(pos + 1, copy.length());
            }
        }

        return copy;
    }


}
