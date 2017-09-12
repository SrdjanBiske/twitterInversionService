package service.inversion;

import static dm.dao.InterrogativeUtility.isQuestionWord;
import static dm.dao.Utility.clean;
import static dm.dao.VerbUtility.isFormOfDo;
import static dm.dao.VerbUtility.isFormOfBe;
import static dm.dao.VerbUtility.isFormOfHave;
import static dm.dao.VerbUtility.isModal;
import static dm.dao.VerbUtility.isInfinitive;
import static dm.dao.VerbUtility.isPerfect;
import static dm.dao.VerbUtility.isThirdPerson;
import static dm.dao.VerbUtility.isGerund;
import static dm.dao.VerbUtility.isPastParticiple;
import static dm.dao.VerbUtility.getInfinitive;
import static dm.dao.VerbUtility.isStartOrStop;

/**
 * Class for representing a complete verb form.
 * Level 1 is the first verb in the form and
 * level 4 is the last.
 */
public class Tense {

    /**
     * First verb.
     */
    private String level1;

    /**
     * Second verb.
     */
    private String level2;

    /**
     * Third verb.
     */
    private String level3;

    /**
     * Fourth verb.
     */
    private String level4;

    /**
     * First verb's position.
     */
    private Integer position1;

    /**
     * Second verb's position.
     */
    private Integer position2;

    /**
     * Third verb's position.
     */
    private Integer position3;

    /**
     * Forth verb's position.
     */
    private Integer position4;

    /**
     * Variable that indicates if we
     * can expect infinitive on the next
     * level.
     */
    private boolean infinitive = false;

    /**
     * Variable is true if we turned the
     * verb from negative to positive.
     */
    private boolean toPositive = false;

    /**
     * Variable is true if we turned the
     * verb from positive to negative.
     */
    private boolean toNegative = false;

    /**
     * Variable is true if the sentence
     * is a question. Required to decide
     * which method of negation we will
     * use.
     */
    private boolean isQuestion;

    /**
     * Position of the first word in the
     * sub-sentence.
     */
    private int subSentenceStart;

    private static boolean alreadyChanged;

    /**
     * Empty constructor.
     */
    public Tense() {
    }

    /**
     * Complete constructor. Checks the first word of
     * the sub-sentence and decides if the sentence is
     * a question or not.
     * @param firstWord - first word of the sub-sentence
     * @param sentenceStart - position of the first word
     * @param length - number of words in sentence
     */
    public Tense(final String firstWord,
                 final int sentenceStart,
                 final int length) {

        String copy = clean(firstWord);
        this.subSentenceStart = sentenceStart;

        if (((isQuestionWord(copy))
                || (subSentenceStart == 0 && (isFormOfDo(copy)
                || isModal(copy)))) && subSentenceStart + 1 < length) {

            isQuestion = true;

        } else {
            isQuestion = false;
        }
    }

    /**
     * Method says if the sentence is a question.
     * @return true if the sentence is a question,
     *         false otherwise
     */
    public boolean isQuestion() {
        return isQuestion;
    }

    /**
     * Tries to add a new level to the verb form and
     * reports back if it's successful or not.
     * @param verb - verb to be added to the verb form
     * @param position - position of the verb
     * @return true if the verb is added successfully,
     *         false otherwise
     */
    public boolean levelAddSuccess(final String verb,
                                   final int position) {
        if (level1 == null) {
            return addLevel1(verb, position);

        } else if (level2 == null) {
            return addLevel2(verb, position);

        } else if (level3 == null) {
            return addLevel3(verb, position);

        } else if (level4 == null) {
            return addLevel4(verb, position);
        }
        return false;
    }

    /**
     * Tries to add a verb to the level 1 and
     * reports back if it's successful or not.
     * @param verb - verb to be added to the verb form
     * @param position - position of the verb
     * @return true if the verb is added successfully,
     *         false otherwise
     */
    private boolean addLevel1(final String verb,
                              final int position) {

        if (isFormOfDo(verb)
                || isModal(verb)
                || isInfinitive(verb)
                || isPerfect(verb)
                || isThirdPerson(verb)
                || isFormOfBe(verb)
                || isFormOfHave(verb)) {

            level1 = verb;
            position1 = position;
            return true;

        } else {
            return false;
        }
    }

    /**
     * Tries to add a verb to the level 2 and
     * reports back if it's successful or not.
     * @param verb - verb to be added to the verb form
     * @param position - position of the verb
     * @return true if the verb is added successfully,
     *         false otherwise
     */
    private boolean addLevel2(final String verb,
                              final int position) {

        if ((isInfinitive(verb) && infinitiveOk())
                || (isGerund(verb) && gerundOk())
                || (isPastParticiple(verb) && participleOk())
                || (verb.equals("been") && participleOk())
                || (verb.equals("be") && modalBefore())) {

            level2 = verb;
            position2 = position;
            return true;

        } else {
            return false;
        }
    }

    /**
     * Tries to add a verb to the level 3 and
     * reports back if it's successful or not.
     * @param verb - verb to be added to the verb form
     * @param position - position of the verb
     * @return true if the verb is added successfully,
     *         false otherwise
     */
    private boolean addLevel3(final String verb,
                              final int position) {

        if ((isInfinitive(verb) && (level2.equals("going")
                || getInfinitive(level2).equals("allow")))
                || (isGerund(verb) && (level2.equals("be")
                || isStartOrStop(level2) || level2.equals("stop")))
                || (isPastParticiple(verb) && (level2.equals("have")
                || level2.equals("been") || level2.equals("be")))
                || (verb.equals("been") && level2.equals("have"))) {

            level3 = verb;
            position3 = position;
            return true;

        } else {
            return false;
        }
    }

    /**
     * Tries to add a verb to the level 4 and
     * reports back if it's successful or not.
     * @param verb - verb to be added to the verb form
     * @param position - position of the verb
     * @return true if the verb is added successfully,
     *         false otherwise
     */
    private boolean addLevel4(final String verb,
                              final int position) {

        if ((isGerund(verb) && (level3.equals("been")
                || isStartOrStop(level3)))
                || (isPastParticiple(verb)
                && level3.equals("been"))) {

            level4 = verb;
            position4 = position;
            return true;
        } else {
            return false;
        }
    }

    /**
     * States that the sentence has been turned
     * from negative to positive.
     */
    public void setToPositive() {
        this.toPositive = true;
    }

    /**
     * States that the sentence has been turned
     * from positive to negative.
     */
    public void setToNegative() {
        this.toNegative = true;
    }

    /**
     * Gives back the first level.
     * @return the first level
     */
    public String getLevel1() {
        return level1;
    }

    /**
     * Gives back the second level.
     * @return the second level
     */
    public String getLevel2() {
        return level2;
    }

    /**
     * Gives back the position of the
     * first level.
     * @return the position of the
     *         first level
     */
    public Integer getPosition1() {
        return position1;
    }

    /**
     * Gives back the position of the
     * second level.
     * @return the position of the
     *         second level
     */
    public Integer getPosition2() {
        return position2;
    }

    /**
     * Undo the last change.
     */
    public void undo() {
        if (level4 != null) {
            level4 = null;
            position4 = null;

        } else if (level3 != null) {
            level3 = null;
            position3 = null;

        } else if (level2 != null) {
            level2 = null;
            position2 = null;

        } else if (level1 != null) {
            level1 = null;
            position1 = null;
        }
    }

    /**
     * Gives back the position of the level
     * that is before the last.
     * @return the position of the level
     *         that is before the last
     */
    public int getBeforeLastLvlPos() {
        if (level4 != null) {
            return position3;

        } else if (level3 != null) {
            return position2;

        } else if (level2 != null) {
            return position1;

        } else {
            return -1;
        }
    }

    /**
     * Gives back the last level position.
     * @return the last level position
     */
    public int getLastLvlPos() {
        if (level4 != null) {
            return position4;

        } else if (level3 != null) {
            return position3;

        } else if (level2 != null) {
            return position2;

        } else if (level1 != null) {
            return position1;

        } else {
            return -1;
        }
    }

    /**
     * Checks if the sentence has been
     * turned to positive.
     * @return true if the sentence has
     *         been turned to positive,
     *         false otherwise
     */
    public boolean isToPositive() {
        return toPositive;
    }

    /**
     * Checks if the sentence has been
     * turned to negative.
     * @return true if the sentence has
     *         been turned to negative,
     *         false otherwise
     */
    public boolean isToNegative() {
        return toNegative;
    }

    /**
     * States that the infinitive can be
     * expected on the next level.
     */
    public void activateInfinitive() {
        infinitive = true;
    }

    /**
     * Informs us if the infinitive can be added
     * to the next level.
     * @return true if the infinitive can be added
     *         to the next level, false otherwise
     */
    private boolean infinitiveOk() {
        return isFormOfDo(level1) || modalBefore() || infinitive;
    }

    /**
     * Informs us if there's a modal on level 1.
     * @return true if there's a modal on level 1
     *         false otherwise
     */
    private boolean modalBefore() {
        return isModal(level1);
    }

    /**
     * Informs us if the gerund can be added
     * to the next level.
     * @return true if the gerund can be added
     *         to the next level, false otherwise
     */
    private boolean gerundOk() {
        return isFormOfBe(level1) || isStartOrStop(level1);
    }

    /**
     * Informs us if the past participle can be added
     * to the next level.
     * @return true if the past participle can be added
     *         to the next level, false otherwise
     */
    private boolean participleOk() {
        return isFormOfBe(level1) || isFormOfHave(level1);
    }

    /**
     * Gives back the start of the sub-sentence.
     * @return the start of the sub-sentence
     */
    public int getSubSentenceStart() {
        return subSentenceStart;
    }

    /**
     * Makes the copy of the existing Tense.
     * @return the copy of the existing Tense
     */
    public Tense copy() {
        Tense tense = new Tense();
        tense.level1 = level1;
        tense.level2 = level2;
        tense.level3 = level3;
        tense.level4 = level4;
        tense.position1 = position1;
        tense.position2 = position2;
        tense.position3 = position3;
        tense.position4 = position4;
        tense.infinitive = infinitive;
        tense.toPositive = toPositive;
        tense.toNegative = toNegative;
        tense.isQuestion = isQuestion;
        tense.subSentenceStart = subSentenceStart;
        return tense;
    }

    /**
     * Informs us if the sentence has already
     * been changed or not.
     * @return true if the sentence has been
     *         changed, false otherwise
     */
    public boolean hasChanged() {
        return alreadyChanged;
    }

    /**
     * Used to state if the sentence has
     * been changed or not.
     */
    public void setChanged(final boolean changed) {
        alreadyChanged = changed;
    }

}
