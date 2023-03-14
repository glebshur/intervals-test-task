package shgo.innowise.trainee.intervalstask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class IntervalsTest {

    @ParameterizedTest
    @MethodSource("provideArgumentsForIntervalConstructionTest")
    public void intervalConstructionShouldReturnExpectedResult(String[] args, String expected) {
        Assertions.assertEquals(expected, Intervals.intervalConstruction(args));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForIntervalConstructionExceptionTest")
    public void intervalConstructionShouldThrowException(String[] args) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Intervals.intervalConstruction(args));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForIntervalIdentificationTest")
    public void intervalIdentificationShouldReturnExpectedResult(String args[], String expected) {
        Assertions.assertEquals(expected, Intervals.intervalIdentification(args));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForIntervalIdentificationExceptionTest")
    public void intervalIdentificationShouldThrowException(String[] args) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Intervals.intervalIdentification(args));
    }

    private static Stream<Arguments> provideArgumentsForIntervalConstructionTest() {
        return Stream.of(
                Arguments.of(new String[]{"M2", "C", "asc"}, "D"),
                Arguments.of(new String[]{"P5", "B", "asc"}, "F#"),
                Arguments.of(new String[]{"m2", "Bb", "dsc"}, "A"),
                Arguments.of(new String[]{"M3", "Cb", "dsc"}, "Abb"),
                Arguments.of(new String[]{"P4", "G#", "dsc"}, "D#"),
                Arguments.of(new String[]{"m3", "B", "dsc"}, "G#"),
                Arguments.of(new String[]{"m2", "Fb", "asc"}, "Gbb"),
                Arguments.of(new String[]{"M2", "E#", "dsc"}, "D#"),
                Arguments.of(new String[]{"P4", "E", "dsc"}, "B"),
                Arguments.of(new String[]{"m2", "D#", "asc"}, "E"),
                Arguments.of(new String[]{"M7", "G", "asc"}, "F#")
        );
    }

    private static Stream<Arguments> provideArgumentsForIntervalConstructionExceptionTest() {
        return Stream.of(
                Arguments.of((Object) new String[]{"M2"}),
                Arguments.of((Object) new String[]{"M2", "C", "asc", "abc"}),
                Arguments.of((Object) new String[]{"Interval", "C", "asc"}),
                Arguments.of((Object) new String[]{"M2", "err", "asc"}),
                Arguments.of((Object) new String[]{"M2", "C", "abc"})
        );
    }

    private static Stream<Arguments> provideArgumentsForIntervalIdentificationTest() {
        return Stream.of(
                Arguments.of(new String[]{"C", "D"}, "M2"),
                Arguments.of(new String[]{"B", "F#", "asc"}, "P5"),
                Arguments.of(new String[]{"Fb", "Gbb"}, "m2"),
                Arguments.of(new String[]{"G", "F#", "asc"}, "M7"),
                Arguments.of(new String[]{"Bb", "A", "dsc"}, "m2"),
                Arguments.of(new String[]{"Cb", "Abb", "dsc"}, "M3"),
                Arguments.of(new String[]{"G#", "D#", "dsc"}, "P4"),
                Arguments.of(new String[]{"E", "B", "dsc"}, "P4"),
                Arguments.of(new String[]{"E#", "D#", "dsc"}, "M2"),
                Arguments.of(new String[]{"B", "G#", "dsc"}, "m3")
        );
    }

    private static Stream<Arguments> provideArgumentsForIntervalIdentificationExceptionTest() {
        return Stream.of(
                Arguments.of((Object) new String[]{"Cb"}),
                Arguments.of((Object) new String[]{"C", "D", "asc", "abc"}),
                Arguments.of((Object) new String[]{"Abbb", "C", "asc"}),
                Arguments.of((Object) new String[]{"C", "D", "abc"})
        );
    }
}
