package dev.lecture_clean_tdd.application.port.input

import dev.lecture_clean_tdd.adapter.web.response.LectureListResponse

interface GetLecturesUseCase {
    fun getAllLectures(): LectureListResponse
}