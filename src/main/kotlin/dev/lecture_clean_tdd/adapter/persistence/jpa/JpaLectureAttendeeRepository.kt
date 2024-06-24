package dev.lecture_clean_tdd.adapter.persistence.jpa

import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureAttendee
import dev.lecture_clean_tdd.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaLectureAttendeeRepository : JpaRepository<LectureAttendee, Long> {
    fun findByUserAndLecture(user: User, lecture: Lecture) : LectureAttendee?
    fun countByLecture(lecture: Lecture) : Long
}