package dev.lecture_clean_tdd.application.port.input

import dev.lecture_clean_tdd.application.service.dto.LectureDto

interface GetLecturesUseCase {
    fun getAllLectures(): List<LectureDto>
}