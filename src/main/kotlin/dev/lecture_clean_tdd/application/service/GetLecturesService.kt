package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.adapter.web.response.LectureListResponse
import dev.lecture_clean_tdd.adapter.web.response.toDto
import dev.lecture_clean_tdd.application.port.input.GetLecturesUseCase
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import org.springframework.stereotype.Service

@Service
class GetLecturesService(
    private val lectureRepository: LectureRepository,
) : GetLecturesUseCase{
    override fun getAllLectures(): LectureListResponse = LectureListResponse(lectureRepository.findAll().map { it.toDto() })
}
