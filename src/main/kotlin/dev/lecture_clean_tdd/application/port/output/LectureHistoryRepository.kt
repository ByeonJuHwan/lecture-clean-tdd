package dev.lecture_clean_tdd.application.port.output

import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureApplicationHistory

interface LectureHistoryRepository { 
    fun save(history: LectureApplicationHistory): LectureApplicationHistory 
    fun countByLecture(lecture: Lecture): Long 
} 
