package dev.lecture_clean_tdd.adapter.persistence

import dev.lecture_clean_tdd.adapter.persistence.jpa.JpaLectureAttendeeRepository
import dev.lecture_clean_tdd.application.port.output.LectureAttendeeRepository
import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureAttendee
import dev.lecture_clean_tdd.domain.entity.User
import org.springframework.stereotype.Repository

@Repository
class LectureAttendeeRepositoryImpl(
    private val jpaLectureAttendeeRepository : JpaLectureAttendeeRepository,
) : LectureAttendeeRepository {
    override fun save(lectureAttendee: LectureAttendee): LectureAttendee {
        return jpaLectureAttendeeRepository.save(lectureAttendee)
    }

    override fun findByUserAndLecture(user: User, lecture: Lecture): LectureAttendee? {
        return jpaLectureAttendeeRepository.findByUserAndLecture(user, lecture)
    }

    override fun countByLecture(lecture: Lecture): Long {
        return jpaLectureAttendeeRepository.countByLecture(lecture)
    }
}