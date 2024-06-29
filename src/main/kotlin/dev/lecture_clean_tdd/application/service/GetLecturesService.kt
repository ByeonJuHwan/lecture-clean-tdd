package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.application.port.input.GetLecturesUseCase
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.application.service.dto.LectureDto
import dev.lecture_clean_tdd.application.service.dto.toDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
 
@Service 
@Transactional(readOnly = true) 
class GetLecturesService( 
    private val lectureRepository: LectureRepository, 
) : GetLecturesUseCase{ 
    override fun getAllLectures(): List<LectureDto> { 
        return lectureRepository.findAll().map { it.toDto() } 
    } 
} 
