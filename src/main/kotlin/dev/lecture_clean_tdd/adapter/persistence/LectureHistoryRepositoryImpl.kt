package dev.lecture_clean_tdd.adapter.persistence

import dev.lecture_clean_tdd.adapter.persistence.jpa.JpaLectureApplicationHistoryRepository
import dev.lecture_clean_tdd.application.port.output.LectureHistoryRepository
import dev.lecture_clean_tdd.domain.entity.LectureApplicationHistory
import org.springframework.stereotype.Repository

@Repository
class LectureHistoryRepositoryImpl(
    private val jpaLectureApplicationHistoryRepository: JpaLectureApplicationHistoryRepository
) : LectureHistoryRepository {
    override fun save(history: LectureApplicationHistory): LectureApplicationHistory {
        return jpaLectureApplicationHistoryRepository.save(history)
    }
}