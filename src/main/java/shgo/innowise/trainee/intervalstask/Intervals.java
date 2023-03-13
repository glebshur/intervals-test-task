package shgo.innowise.trainee.intervalstask;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Map.entry;

/**
 * Class that calculates and builds notes intervals.
 */
public class Intervals {

    private static final String MINOR_SECOND = "m2";
    private static final String MAJOR_SECOND = "M2";
    private static final String MINOR_THIRD = "m3";
    private static final String MAJOR_THIRD = "M3";
    private static final String PERFECT_FOURTH = "P4";
    private static final String PERFECT_FIFTH = "P5";
    private static final String MINOR_SIXTH = "m6";
    private static final String MAJOR_SIXTH = "M6";
    private static final String MINOR_SEVENTH = "m7";
    private static final String MAJOR_SEVENTH = "M7";
    private static final String PERFECT_OCTAVE = "P8";

    private static final String ASCENDING = "asc";
    private static final String DESCENDING = "dsc";

    // note's pattern for intervalConstruction method
    private static final Pattern intervalConstructionNotePattern = Pattern.compile("^[A-G](#?|b?)$");

    // map contains semitone for every interval
    private static final Map<String, Integer> intervalsSemitonesMap;
    // map contains degree for every interval
    private static final Map<String, Integer> intervalsDegreesMap;
    // notes map: C--D--E-F--G--A--B-C
    private static final List<String> notes;
    // map contains step for every order type
    private static final Map<String, Integer> orderStepMap;

    static {
        intervalsSemitonesMap = Map.ofEntries(
                entry(MINOR_SECOND, 1),
                entry(MAJOR_SECOND, 2),
                entry(MINOR_THIRD, 3),
                entry(MAJOR_THIRD, 4),
                entry(PERFECT_FOURTH, 5),
                entry(PERFECT_FIFTH, 7),
                entry(MINOR_SIXTH, 8),
                entry(MAJOR_SIXTH, 9),
                entry(MINOR_SEVENTH, 10),
                entry(MAJOR_SEVENTH, 11),
                entry(PERFECT_OCTAVE, 12)
        );

        intervalsDegreesMap = Map.ofEntries(
                entry(MINOR_SECOND, 2),
                entry(MAJOR_SECOND, 2),
                entry(MINOR_THIRD, 3),
                entry(MAJOR_THIRD, 3),
                entry(PERFECT_FOURTH, 4),
                entry(PERFECT_FIFTH, 5),
                entry(MINOR_SIXTH, 6),
                entry(MAJOR_SIXTH, 6),
                entry(MINOR_SEVENTH, 7),
                entry(MAJOR_SEVENTH, 7),
                entry(PERFECT_OCTAVE, 8)
        );

        // '-' is replaced with empty string
        notes = List.of("C", "", "", "D", "", "", "E", "", "F", "", "",
                "G", "", "", "A", "", "", "B", "");

        orderStepMap = Map.of(
                ASCENDING, 1,
                DESCENDING, -1
        );
    }

    /**
     * Calculates note that comes in intervals from a given.
     *
     * @param args - array of arguments:
     *             args[1] - interval name
     *             args[2] - starting note
     *             args[3] (optional) - building order type (ascending and descending)
     * @return note name
     */
    public static String intervalConstruction(final String[] args) {

        // parsing and validating
        if (args.length > 3 || args.length < 2) {
            throw new IllegalArgumentException("Illegal number of elements in input array");
        }

        final String interval = args[0];
        final String startNote = args[1];
        final String order = args.length == 3 ? args[2] : ASCENDING;

        Integer step = orderStepMap.get(order);
        if (step == null) {
            throw new IllegalArgumentException("Order is invalid");
        }

        if (!intervalConstructionNotePattern.matcher(startNote).matches()) {
            throw new IllegalArgumentException("Start note is invalid");
        }

        Integer intervalDegree = intervalsDegreesMap.get(interval);
        Integer intervalSemitones = intervalsSemitonesMap.get(interval);
        if (intervalDegree == null || intervalSemitones == null) {
            throw new IllegalArgumentException("Interval is invalid");
        }

        // note position without semitones
        final int startNotePosition = notes.indexOf(startNote.substring(0, 1));
        // start note's semitones
        final int startNoteSemitone = startNote.length() > 1 ? getNoteSemitone(startNote) : 0;

        // calculation of end note position
        int currentPosition = startNotePosition;
        int notesDistance = 0; // distance between start note and end note
        int currentDegree = 1;
        while (currentDegree != intervalDegree) {
            currentPosition += step;
            notesDistance += 1;

            // cycled list implementation
            if (currentPosition < 0) {
                currentPosition = notes.size() + currentPosition;
            }
            if (currentPosition >= notes.size()) {
                currentPosition = currentPosition - notes.size();
            }

            // looking for notes to calculate the degree
            if (!notes.get(currentPosition).equals("")) {
                currentDegree++;
            }
        }

        // end note offset (semitones number) calculation

        // it is necessary to exclude the positions occupied by the notes
        int distanceInSemitones = notesDistance - (intervalDegree - 1);
        int endNoteSemitones;
        if (order.equals(ASCENDING)) {
            // offset by ascending order is difference between sum of required semitones by interval
            // and semitones of start note and semitones between notes
            endNoteSemitones = intervalSemitones + startNoteSemitone - distanceInSemitones;
        } else {
            // offset by ascending order is sum of difference between semitones between notes
            // and required semitones by interval and semitones of start note
            endNoteSemitones = distanceInSemitones - intervalSemitones + startNoteSemitone;
        }

        return getNoteWithSemitones(notes.get(currentPosition), endNoteSemitones);
    }

    public static String intervalIdentification(String[] args) {
        return null;
    }

    /**
     * Adds semitones characters to note.
     *
     * @param note      - note name (A-G)
     * @param semitones - semitones of the note:
     *                  semitones < 0 if it's lowering
     *                  semitones > 0 if it's raising
     * @return note with semitone (like 'A##')
     */
    private static String getNoteWithSemitones(final String note, final int semitones) {
        final int count = Math.abs(semitones);
        final String semitoneCharacter = semitones < 0 ? "b" : "#";
        return note + semitoneCharacter.repeat(Math.max(0, count));
    }

    /**
     * Calculates semitones of the note.
     *
     * @param note - note name with semitones (like 'Bbb')
     * @return number of semitone:
     * < 0 if it's lowering;
     * > 0 if it's raising
     */
    private static int getNoteSemitone(final String note) {
        // first calculate raising semitones
        int semitone = countMatches(note, "#");
        if (semitone == 0) {
            // if there isn't any, calculate lowering semitones
            semitone = countMatches(note, "b") * (-1);
        }
        return semitone;
    }

    /**
     * Calculates the number of occurrences of a substring in the string.
     *
     * @param string    - string in which the substring is searched for
     * @param substring - target string
     * @return number of occurrences
     */
    private static int countMatches(final String string, final String substring) {
        final String temp = string.replace(substring, "");
        return (string.length() - temp.length()) / substring.length();
    }
}
