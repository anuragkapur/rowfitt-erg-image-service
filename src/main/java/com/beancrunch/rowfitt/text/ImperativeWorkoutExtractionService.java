package com.beancrunch.rowfitt.text;

import com.beancrunch.rowfitt.domain.Workout;
import com.beancrunch.rowfitt.text.handler.FuzzyMatchDateMonthHandler;
import com.beancrunch.rowfitt.text.handler.TrimSpacesHandler;
import com.beancrunch.rowfitt.text.handler.WorkoutTextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImperativeWorkoutExtractionService implements WorkoutExtractionService {

    private static final Logger log = LoggerFactory.getLogger(ImperativeWorkoutExtractionService.class);

    @Override
    public Workout getWorkout(String workoutImageText) {
        Workout workout = new Workout();
        String[] lines = workoutImageText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("time")) {
                if (i+1 < lines.length)
                    setWorkoutKeyDetails(workout, lines[i+1]);
                if (i > 0) {
                    setWorkoutDate(workout, lines[i-1]);
                }
            }
            if (lines[i].contains("Detail")) {
                if (i+1 < lines.length)
                    setWorkoutType(workout, lines[i+1]);
            }
        }
        return workout;
    }

    private void setWorkoutType(Workout workout, String line) {
        if (line.contains("Total")) {
            workout.setWorkoutType(line.substring(0, line.indexOf("Total")).trim().replaceAll("\\s", "/"));
        } else {
            workout.setWorkoutType(line.trim().replaceAll("\\s", "/"));
        }
    }

    private void setWorkoutDate(Workout workout, String line) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            workout.setDate(dateFormat.parse(line));
        } catch (ParseException e) {
            log.error("Failed to parse date extracted from image line %s", line, e);
        }
    }

    private void setWorkoutKeyDetails(Workout workout, String line) {
        String[] tokens = line.split("\\s");
        Pattern patten = Pattern.compile("(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d\\.[0-9]*)");
        if (tokens.length == 4) {
            String time = tokens[0].replaceAll(",", "\\.");
            Matcher matcher = patten.matcher(time);
            if (matcher.matches() && matcher.groupCount() == 3) {
                workout.setTimeHh(Integer.parseInt(matcher.group(1) == null ? "0" : matcher.group(1)));
                workout.setTimeMm(Integer.parseInt(matcher.group(2)));
                workout.setTimeSss(Float.parseFloat(matcher.group(3)));
            }
            workout.setDistance(Integer.parseInt(tokens[1]));
            String split = tokens[2].replaceAll(",", "\\.");
            matcher = patten.matcher(split);
            if (matcher.matches() && matcher.groupCount() == 3) {
                workout.setSplitMm(Integer.parseInt(matcher.group(2)));
                workout.setSplitSss(Float.parseFloat(matcher.group(3)));
            }
            workout.setStrokeRate(Integer.parseInt(tokens[3]));
        }
    }
}
