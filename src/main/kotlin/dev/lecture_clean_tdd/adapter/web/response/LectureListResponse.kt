package dev.lecture_clean_tdd.adapter.web.response

import dev.lecture_clean_tdd.application.service.dto.LectureDto

data class LectureListResponse(
    val lectures : List<LectureDto>
)
