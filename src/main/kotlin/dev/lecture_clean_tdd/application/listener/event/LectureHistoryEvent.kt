package dev.lecture_clean_tdd.application.listener.event

import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.User

data class LectureHistoryEvent(val user: User, val lecture: Lecture) 

