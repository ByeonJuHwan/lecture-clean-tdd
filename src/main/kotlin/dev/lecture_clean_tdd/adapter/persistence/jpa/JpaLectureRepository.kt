package dev.lecture_clean_tdd.adapter.persistence.jpa

import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JpaLectureRepository : JpaRepository<Lecture, Long>  {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Lecture l where l.id = :lectureId")
    fun findByIdWithLock(lectureId: Long) : Lecture
}