package dev.lecture_clean_tdd.application.port.output

import dev.lecture_clean_tdd.domain.entity.Lecture
import java.util.Optional

interface LectureRepository {
    fun save(lecture : Lecture) : Long
    fun findById(lectureId : Long) : Lecture?
    fun findByIdWithLock(lectureId: Long): Lecture?
}