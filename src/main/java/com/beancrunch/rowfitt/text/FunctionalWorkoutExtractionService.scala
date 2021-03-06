package com.beancrunch.rowfitt.text

import com.beancrunch.rowfitt.domain.TimeMetric.TimeMetric
import com.beancrunch.rowfitt.domain.{TimeMetric, Workout}
import com.beancrunch.rowfitt.text.handler.{DateParseHandler, FuzzyMatchDateMonthHandler, TrimSpacesHandler}
import me.xdrop.fuzzywuzzy.FuzzySearch

import scala.annotation.tailrec
import scala.util.Try

class FunctionalWorkoutExtractionService extends WorkoutExtractionService {

  override def getWorkout(workoutImageText: String): Workout = {
    val workout = new Workout()
    extractWorkout(workoutImageText.split("\n").toList, workout)
    workout
  }

def setWorkoutDate(text: String, workout: Workout): Unit = {
  val workoutTextHandler = new TrimSpacesHandler()
  workoutTextHandler
    .setSuccessor(new FuzzyMatchDateMonthHandler())
    .setSuccessor(new DateParseHandler())
  workoutTextHandler.handleTextInChain(text, workout)
}

  def setWorkoutKeyDetails(text: String, workout: Workout): Unit = {
    val tokens = text.split("\\s")
    if (tokens.length == 4) {
      setWorkoutTimeOrSplit(tokens(0), workout, TimeMetric.TotalTime)
      workout.setDistance(Try(tokens(1).toInt).getOrElse(-1))
      setWorkoutTimeOrSplit(tokens(2), workout, TimeMetric.Split)
      workout.setStrokeRate(Try(tokens(3).toInt).getOrElse(-1))
    }
  }

  def setWorkoutTimeOrSplit(text: String, workout: Workout, timeOrSplit: TimeMetric): Unit = {
    val timePattern = """(?:(?:([01]?\d|2[0-3]):)?([0-5]?\d):)?([0-5]?\d\.[0-9]*)""".r
    val time = text.replaceAll(",", "\\.")
    (time, timeOrSplit) match {
      case (timePattern(timeHh, timeMm, timeSss), TimeMetric.TotalTime) =>
        workout.setTimeHh(Try(timeHh.toInt).getOrElse(0))
        workout.setTimeMm(Try(timeMm.toInt).getOrElse(0))
        workout.setTimeSss(Try(timeSss.toFloat).getOrElse(0))
      case (timePattern(_, splitMm, splitSss), TimeMetric.Split) =>
        workout.setSplitMm(Try(splitMm.toInt).getOrElse(0))
        workout.setSplitSss(Try(splitSss.toFloat).getOrElse(0))
      case (_, _) =>
    }
  }

  def setWorkoutType(text: String, workout: Workout): Unit = {
    var workoutType = text.trim()
    if (workoutType.contains("Total")) {
      workoutType = workoutType.substring(0, workoutType.indexOf("Total")).trim()
    }
    workoutType = workoutType.replaceAll("\\s", "/")
    workout.setWorkoutType(workoutType)
  }

  @tailrec
  private def extractWorkout(lines: List[String], workout: Workout): Unit = {
    lines match {
      case Nil =>
      case head :: neck :: body if FuzzySearch.partialRatio(neck, "time meter") > 75 =>
        setWorkoutDate(head, workout)
        setWorkoutKeyDetails(body.head, workout)
        extractWorkout(lines.tail, workout)
      case _ :: neck :: body if FuzzySearch.partialRatio(neck, "View Detail") > 75 =>
        setWorkoutType(body.head, workout)
        extractWorkout(lines.tail, workout)
      case _ =>
        extractWorkout(lines.tail, workout)
    }
  }
}