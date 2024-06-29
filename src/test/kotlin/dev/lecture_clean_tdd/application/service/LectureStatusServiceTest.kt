package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.application.port.output.LectureAttendeeRepository
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureAttendee
import dev.lecture_clean_tdd.domain.entity.User
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class LectureStatusServiceTest {

    @Mock
    lateinit var lectureRepository: LectureRepository

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    private lateinit var lectureAttendeeRepository: LectureAttendeeRepository

    @InjectMocks
    private lateinit var lectureStatusService: LectureStatusService

    @Test 
    fun `특강 신청에 성공하면 true 를 반환한다`() { 
        val userId = 1L 
        val lectureId = 1L 
        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00") 
        val user = User("변주환") 
        val lectureAttendee = LectureAttendee(user, lecture) 
 
        `when`(userRepository.findById(userId)).thenReturn(user) 
        `when`(lectureRepository.findById(lectureId)).thenReturn(lecture) 
        `when`(lectureAttendeeRepository.findByUserAndLecture(user, lecture)).thenReturn(lectureAttendee) 
        val isRegistered = lectureStatusService.isLectureRegistered(userId, lectureId) 
 
        assertThat(isRegistered).isTrue() 
    } 
 
    @Test 
    fun `특강 신청에 실패하면 false 를 반환한다`() { 
        val userId = 1L 
        val lectureId = 1L 
        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00") 
        val user = User("변주환") 
 
        `when`(userRepository.findById(userId)).thenReturn(user) 
        `when`(lectureRepository.findById(lectureId)).thenReturn(lecture) 
        `when`(lectureAttendeeRepository.findByUserAndLecture(user, lecture)).thenReturn(null) 
        val isRegistered = lectureStatusService.isLectureRegistered(userId, lectureId) 
 
        assertThat(isRegistered).isFalse() 
    } 
}
