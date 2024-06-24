package dev.lecture_clean_tdd.application.port.input

import dev.lecture_clean_tdd.adapter.web.request.LectureRequestDto

interface RegisterLectureUseCase {
    fun registerLecture(request : LectureRequestDto) : Boolean
}