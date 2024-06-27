package dev.lecture_clean_tdd.adapter.persistence

import dev.lecture_clean_tdd.Exception.LectureNotFoundException
import dev.lecture_clean_tdd.adapter.persistence.jpa.JpaLectureRepository
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.domain.entity.Lecture
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class LectureRepositoryImpl(
    private val jpaLectureRepository: JpaLectureRepository,
) : LectureRepository {

    override fun save(lecture: Lecture): Long {
        return jpaLectureRepository.save(lecture).id
    }

    override fun findById(lectureId: Long): Lecture? {
        return jpaLectureRepository.findByIdOrNull(lectureId)
    }

    override fun findByIdWithLock(lectureId: Long): Lecture? {
        return jpaLectureRepository.findByIdWithLock(lectureId)
    }
 
    override fun findAll(): List<Lecture> { 
        return jpaLectureRepository.findAll() 
    } 
} 
