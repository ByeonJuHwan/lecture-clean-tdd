package dev.lecture_clean_tdd.adapter.web.request

import dev.lecture_clean_tdd.application.service.dto.LectureRegistryDto

data class LectureRequest(
    val userId : Long,
    val lectureId : Long,
)

fun LectureRequest.toDto() = LectureRegistryDto(
    userId = userId,
    lectureId = lectureId
)
