package com.beancrunch.rowfitt.gcp;

import com.beancrunch.rowfitt.domain.Workout;
import com.beancrunch.rowfitt.text.FunctionalWorkoutExtractionService;
import com.beancrunch.rowfitt.text.ImperativeWorkoutExtractionService;
import com.beancrunch.rowfitt.text.WorkoutExtractionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static java.nio.file.Paths.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WorkoutExtractionServiceTest {

    private static final String expectedWorkoutsDir =
            "/Users/anuragkapur/tech-stuff/workspace/beancrunch/rowfitt-erg-image-service/src/test/resources/erg" +
                    "-expected-workouts/";

    private ObjectMapper objectMapper = new ObjectMapper();

    private static WorkoutExtractionService workoutExtractionService = new FunctionalWorkoutExtractionService();

    private static List<ExpectedActualPair> pairs = new ArrayList<>();

    @BeforeAll
    static void setup() {
        try {
            new WorkoutExtractionServiceTest().extractWorkoutsFromText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("getPairsStream")
    void testPairs(ExpectedActualPair pair) {
        assertEquals(pair.expected, pair.actual,
                String.format("Actual not as expected for '%s' in %s", pair.field, pair.fileName));
    }

    private static Stream<ExpectedActualPair> getPairsStream() {
        return pairs.stream();
    }

    private void extractWorkoutsFromText() throws Exception {
        Optional<URL> ergTextsDirUrl = Optional.ofNullable(getClass().getClassLoader().getResource("erg-texts"));
        ergTextsDirUrl
                .map(url -> getChildPathStream(get(url.getPath())))
                .orElseThrow(() -> new Exception("No files found to process"))
                .forEach(this::initExpectedActualPairsForFile);
    }

    private static Stream<Path> getChildPathStream(Path path) {
        try {
            return Files.list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    private void initExpectedActualPairsForFile(Path path) {
        try {
            String fileName = path.getFileName().toString();
            String lines = new String(Files.readAllBytes(path));
            Workout actual = workoutExtractionService.getWorkout(lines);
            Workout expected = getExpectedWorkout(path);
            addPairs(fileName, actual, expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPairs(String fileName, Workout actual, Workout expected) {
        pairs.add(new ExpectedActualPair(valueOf(expected.getDate()), valueOf(actual.getDate()), fileName, "date"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getDistance()), valueOf(actual.getDistance()), fileName,
                "distance"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getSplitMm()), valueOf(actual.getSplitMm()), fileName,
                "splitMm"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getSplitSss()), valueOf(actual.getSplitSss()), fileName,
                "SplitSss"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getTimeHh()), valueOf(actual.getTimeHh()), fileName,
                "TimeHh"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getTimeMm()), valueOf(actual.getTimeMm()), fileName,
                "TimeMm"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getTimeSss()), valueOf(actual.getTimeSss()), fileName,
                "TimeSss"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getStrokeRate()), valueOf(actual.getStrokeRate()),
                fileName, "strokeRate"));
        pairs.add(new ExpectedActualPair(valueOf(expected.getHeartRate()), valueOf(actual.getHeartRate()), fileName,
                "heartRate"));
        pairs.add(new ExpectedActualPair(expected.getWorkoutType(), actual.getWorkoutType(), fileName, "workoutType"));
    }

    private Workout getExpectedWorkout(Path path) throws IOException {
        return objectMapper.readValue(new File(expectedWorkoutsDir+getWorkoutFileName(path)), Workout.class);
    }

    private String getWorkoutFileName(Path path) {
        return path.getFileName().toString().replace(".txt", ".json");
    }

    private class ExpectedActualPair {
        private String expected;
        private String actual;
        private String fileName;
        private String field;

        ExpectedActualPair(String expected, String actual, String fileName, String field) {
            this.expected = expected;
            this.actual = actual;
            this.fileName = fileName;
            this.field = field;
        }
    }
}