package dev.lecture_clean_tdd.application.port.output

import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureAttendee
import dev.lecture_clean_tdd.domain.entity.User

interface LectureAttendeeRepository {
    fun save(lectureAttendee: LectureAttendee): LectureAttendee
    fun findByUserAndLecture(user: User, lecture: Lecture): LectureAttendee?
    fun countByLecture(lecture: Lecture) : Long
}