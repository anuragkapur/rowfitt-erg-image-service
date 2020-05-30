package com.beancrunch.rowfitt.text.handler

import java.text.SimpleDateFormat
import java.util.TimeZone

import com.beancrunch.rowfitt.domain.Workout

import scala.util.Try

class DateParseHandler extends WorkoutTextHandler {

  override def handleTextInStep(text: String, workout: Workout): String = {
    val dateFormat = new SimpleDateFormat("MMM dd yyyy")
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
    workout.setDate(Try(dateFormat.parse(text)).getOrElse(null))
    text
  }
}
