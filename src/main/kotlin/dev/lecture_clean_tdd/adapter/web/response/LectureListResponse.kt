package dev.lecture_clean_tdd.adapter.web.response

import dev.lecture_clean_tdd.domain.entity.Lecture

data class LectureListResponse(
    val lectures : List<LectureDto>
)

data class LectureDto(
    val id: Long,
    val title: String,
    val registrationStartDate: String,
    val registrationEndDate: String,
    val lectureDate: String,
    val maxAttendees: Int,
    val currentAttendees: Int
)

fun Lecture.toDto() = LectureDto(
    id = id,
    title = title,
    registrationStartDate = registrationStartDate,
    registrationEndDate = registrationEndDate,
    lectureDate = lectureDate,
    currentAttendees = currentAttendees,
    maxAttendees = maxAttendees
)