package dev.lecture_clean_tdd.application.port.input

import dev.lecture_clean_tdd.application.service.dto.LectureRegistryDto

interface RegisterLectureUseCase { 
    fun registerLecture(request : LectureRegistryDto) : Boolean 
} 
