package com.tyro.birthdayreminder.custom_class

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.MonthDay
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
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

    // Handle Feb 29 safely — fallback to Feb 28 if current year isn't leap
    val thisYearsBirthday = try {
        birthDate.withYear(today.year)
    } catch (e: Exception) {
        LocalDate.of(today.year, 2, 28)
    }

    val targetDate = if (thisYearsBirthday.isBefore(today)) {
        try {
            birthDate.withYear(today.year + 1)
        } catch (e: Exception) {
            LocalDate.of(today.year + 1, 2, 28)
        }
    } else {
        thisYearsBirthday
    }

    // Calculate the calendar difference
    var period = Period.between(today, targetDate)

    // Normalize: Period can return negative days if day-of-month in target < today
    if (period.days < 0) {
        val adjustedDate = targetDate.minusMonths(1)
        val adjustedPeriod = Period.between(today, adjustedDate)
        val daysInPrevMonth = adjustedDate.lengthOfMonth()
        period = Period.of(adjustedPeriod.years, adjustedPeriod.months, daysInPrevMonth + period.days)
    }

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
fun getWeekOfMonth(dateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)
    val weekFields = WeekFields.of(Locale.getDefault())
    return date.get(weekFields.weekOfMonth())
}


@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfMonth(dateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateStr, formatter)
    val dayOfMonth = MonthDay.of(date.monthValue, date.dayOfMonth)
    return dayOfMonth.dayOfMonth
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDate(dateStr: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(dateStr, formatter)
}

//@RequiresApi(Build.VERSION_CODES.O)
//fun getDaysLeftActualNumbers(birthDateStr: String): Int {
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    val birthDate = LocalDate.parse(birthDateStr, formatter)
//    val today = LocalDate.now()
//    val thisYearsBirthday = LocalDate.of(today.year, birthDate.month, birthDate.dayOfMonth)
//
//    val targetDate = if (thisYearsBirthday.isBefore(today)) {
//        thisYearsBirthday.plusYears(1)
//    } else {
//        thisYearsBirthday
//    }
//
//    return java.time.temporal.ChronoUnit.DAYS.between(today, targetDate).toInt()
//}


@RequiresApi(Build.VERSION_CODES.O)
fun getDaysLeftActualNumbers(birthDateStr: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val birthDate = LocalDate.parse(birthDateStr, formatter)
    val today = LocalDate.now()

    // Handle Feb 29 birthdays safely
    val thisYearsBirthday = try {
        birthDate.withYear(today.year)
    } catch (e: Exception) {
        // If Feb 29 and not a leap year → fallback to Feb 28
        LocalDate.of(today.year, 2, 28)
    }

    val targetDate = if (thisYearsBirthday.isBefore(today)) {
        try {
            birthDate.withYear(today.year + 1)
        } catch (e: Exception) {
            // Again handle Feb 29 edge case
            LocalDate.of(today.year + 1, 2, 28)
        }
    } else {
        thisYearsBirthday
    }

    return ChronoUnit.DAYS.between(today, targetDate).toInt()
}
