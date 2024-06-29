package dev.lecture_clean_tdd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class LectureCleanTddApplication

fun main(args: Array<String>) {
	runApplication<LectureCleanTddApplication>(*args)
}
