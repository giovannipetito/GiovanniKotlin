package it.giovanni.arkivio.customview.calendarview.model

import it.giovanni.arkivio.customview.calendarview.next
import it.giovanni.arkivio.customview.calendarview.previous
import it.giovanni.arkivio.customview.calendarview.yearMonth
import java.io.Serializable
import java.time.LocalDate
import java.time.YearMonth

data class Day internal constructor(val date: LocalDate, val owner: DayOwner) : Comparable<Day>, Serializable {

    val day = date.dayOfMonth

    // Find the actual month on the calendar that owns this date.
    internal val positionYearMonth: YearMonth
        get() = when (owner) {
            DayOwner.THIS_MONTH -> date.yearMonth
            DayOwner.PREVIOUS_MONTH -> date.yearMonth.next
            DayOwner.NEXT_MONTH -> date.yearMonth.previous
        }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false
        other as Day
        return date == other.date && owner == other.owner
    }

    override fun compareTo(other: Day): Int {
        throw UnsupportedOperationException("Compare using the date parameter. Out and In dates can have the same date values as CalendarDay in another month.")
    }

    override fun hashCode(): Int {
        return 31 * (date.hashCode() + owner.hashCode())
    }

    override fun toString(): String {
        return "CalendarDay { date = $date, owner = $owner}"
    }
}