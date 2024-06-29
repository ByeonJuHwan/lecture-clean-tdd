package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.Exception.LectureNotFoundException
import dev.lecture_clean_tdd.Exception.UserNotFoundException
import dev.lecture_clean_tdd.application.port.input.LectureStatusUseCase
import dev.lecture_clean_tdd.application.port.output.LectureAttendeeRepository
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
 
@Service 
@Transactional(readOnly = true) 
class LectureStatusService ( 
    private val lectureAttendeeRepository: LectureAttendeeRepository, 
    private val lectureRepository: LectureRepository, 
    private val userRepository : UserRepository, 
) : LectureStatusUseCase{ 
    override fun isLectureRegistered(userId: Long, lectureId: Long): Boolean { 
        val user = getUser(userId) 
        val lecture = getLecture(lectureId) 
 
        return lectureAttendeeRepository.findByUserAndLecture(user, lecture)?.let { true } ?: false 
    } 
 
    private fun getUser(userId: Long): User { 
        return userRepository.findById(userId) ?: throw UserNotFoundException("유저를 찾을 수 없습니다") 
    } 
 
    private fun getLecture(lectureId: Long): Lecture { 
        return lectureRepository.findById(lectureId) ?: throw LectureNotFoundException("강의를 찾을 수 없습니다") 
    } 
} 
