package dev.lecture_clean_tdd.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtil { 
 
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") 
 
    fun getCurrentTimeFormatted(zoneId: ZoneId = ZoneId.of("Asia/Seoul")): LocalDateTime { 
        val currentTime = ZonedDateTime.now(zoneId).format(formatter) 
        return LocalDateTime.parse(currentTime, formatter) 
    } 
 
    fun isBefore(currentTime: LocalDateTime, targetDate: String): Boolean { 
        return currentTime.isBefore(parseDate(targetDate)) 
    } 
 
    fun isAfter(currentTime: LocalDateTime, targetDate: String): Boolean { 
        return currentTime.isAfter(parseDate(targetDate)) 
    } 
 
    private fun parseDate(date: String): LocalDateTime { 
        return LocalDateTime.parse(date, formatter) 
    } 

}
