package com.tyro.birthdayreminder.custom_class

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun getMonthAndDay(dateStr: String): Pair<Int, Int> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)
    return date.monthValue to date.dayOfMonth
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfWeek(dateStr: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)

    val birthDate = LocalDate.parse(dateStr, formatter)
    val today = LocalDate.now()
    val thisYearsBirthday = LocalDate.of(today.year, birthDate.month, birthDate.dayOfMonth)

    val targetDate = if (thisYearsBirthday.isBefore(today)) {
        thisYearsBirthday.plusYears(1)
    }else{
        thisYearsBirthday
    }
    val currentYearDay = LocalDate.of(targetDate.year, date.monthValue, date.dayOfMonth)
    return currentYearDay.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

}

@RequiresApi(Build.VERSION_CODES.O)
fun getYear(dateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)
    return date.year
}

@RequiresApi(Build.VERSION_CODES.O)
fun getAge(dateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val birthDate = LocalDate.parse(dateStr, formatter)
    val today = LocalDate.now()
    return Period.between(birthDate, today).years
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysLeft(birthDateStr: String): Pair<Int, Int> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val birthDate = LocalDate.parse(birthDateStr, formatter)
    val today = LocalDate.now()
    val thisYearsBirthday = LocalDate.of(today.year, birthDate.month, birthDate.dayOfMonth)

    val targetDate = if (thisYearsBirthday.isBefore(today)) {
        thisYearsBirthday.plusYears(1)
    } else {
        thisYearsBirthday
    }
    val period = Period.between(today, targetDate)
    return period.months to period.days
}

@RequiresApi(Build.VERSION_CODES.O)
fun getAgeOnNextBirthday(birthDateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val birthDate = LocalDate.parse(birthDateStr, formatter)
    val today = LocalDate.now()

    val thisYearsBirthday = birthDate.withYear(today.year)
    return if (thisYearsBirthday.isAfter(today)) {
        // upcoming this year
        Period.between(birthDate, thisYearsBirthday).years
    } else {
        // birthday already passed this year (or is today) — next birthday is next year
        Period.between(birthDate, thisYearsBirthday.plusYears(1)).years
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonth(dateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)
    return date.monthValue
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfYear(dateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)
    return date.dayOfYear
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfYMonth(dateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)
    return date.dayOfMonth
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDate(dateStr: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(dateStr, formatter)
}

