package dev.lecture_clean_tdd.application.port.input
 
interface LectureStatusUseCase { 
    fun isLectureRegistered(userId: Long, lectureId: Long): Boolean 
} 
