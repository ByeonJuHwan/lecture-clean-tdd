package dev.lecture_clean_tdd.adapter.persistence.jpa

import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureApplicationHistory
import org.springframework.data.jpa.repository.JpaRepository

interface JpaLectureApplicationHistoryRepository : JpaRepository<LectureApplicationHistory, Long> {
    fun countByLecture(lecture: Lecture) : Long
}