package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.Exception.DuplicateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.EarlyLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LectureNotFoundException
import dev.lecture_clean_tdd.Exception.MaxAttendeesReachedException
import dev.lecture_clean_tdd.Exception.UserNotFoundException
import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.adapter.web.request.LectureRequestDto
import dev.lecture_clean_tdd.application.event.dto.LectureHistoryEvent
import dev.lecture_clean_tdd.application.port.output.LectureAttendeeRepository
import dev.lecture_clean_tdd.application.port.output.LectureHistoryRepository
import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.domain.entity.LectureAttendee
import dev.lecture_clean_tdd.domain.entity.User
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockitoExtension::class)
class RegisterLectureServiceTest {

    @Mock
    lateinit var lectureRepository: LectureRepository

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var lectureAttendeeRepository: LectureAttendeeRepository

    @Mock
    lateinit var eventPublisher: ApplicationEventPublisher

    @InjectMocks
    lateinit var registerLectureService: RegisterLectureService

    @Test
    fun `특강을 신청하면 성공한다`() {
        val userId = 1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00")

        val user = User("변주환")

        `when`(userRepository.findById(request.userId)).thenReturn(user)
        `when`(lectureRepository.findByIdWithLock(request.lectureId)).thenReturn(lecture)

        val result = registerLectureService.registerLecture(request)

        assertThat(result).isTrue()
    }

    @Test
    fun `특강 신청시 유효하지 않은 회원이면 UseNotFoundException 을 발생시킨다`() {
        val userId = -1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        assertThatThrownBy {
            registerLectureService.registerLecture(request)
        }.isInstanceOf(UserNotFoundException::class.java)
    }

    @Test
    fun `특강 신청시 유효하지 않은 강의이면 LectureNotFoundException 을 발생시킨다`() {
        val userId = 1L
        val lectureId = -100L
        val request = LectureRequestDto(userId, lectureId)

        val user = User("변주환")

        `when`(userRepository.findById(request.userId)).thenReturn(user)

        assertThatThrownBy {
            registerLectureService.registerLecture(request)
        }.isInstanceOf(LectureNotFoundException::class.java)
    }

    @Test
    fun `특강신청이 성공하면 특강의 currentAttendees 가 1 증가한다`() {
        val userId = 1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00")
        val user = User("변주환")

        `when`(userRepository.findById(request.userId)).thenReturn(user)
        `when`(lectureRepository.findByIdWithLock(request.lectureId)).thenReturn(lecture)

        registerLectureService.registerLecture(request)

        assertThat(lecture.currentAttendees).isEqualTo(1)
    }

    @Test
    fun `특강인원이 30명이상이면 MaxAttendeesReachedException 이 발생한다`() {
        val userId = 1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00")
        val user = User("변주환")

        lecture.currentAttendees = 30

        `when`(userRepository.findById(request.userId)).thenReturn(user)
        `when`(lectureRepository.findByIdWithLock(request.lectureId)).thenReturn(lecture)

        assertThatThrownBy {
            registerLectureService.registerLecture(request)
        }.isInstanceOf(MaxAttendeesReachedException::class.java)
    }

    @Test
    fun `동일한 userId 로 신청이력을 검색하여 동일한 특강을 신청하면 DuplicateLectureRegistrationException 이 발생한다`() {
        val userId = 1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00")
        val user = User("변주환")
        val lectureAttendee = LectureAttendee(user,lecture)

        `when`(userRepository.findById(request.userId)).thenReturn(user)
        `when`(lectureRepository.findByIdWithLock(request.lectureId)).thenReturn(lecture)
        `when`(lectureAttendeeRepository.findByUserAndLecture(user,lecture)).thenReturn(lectureAttendee)

        assertThatThrownBy {
            registerLectureService.registerLecture(request)
        }.isInstanceOf(DuplicateLectureRegistrationException::class.java)
            .hasMessage("이미 이 강의에 신청하셨습니다")
    }

    @Test
    fun `특강 신청 시각보다 먼저 신청시 EarlyLectureRegistrationException 을 발생 시킨다`() {
        val userId = 1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        val lecture = Lecture("테스트", "2025-01-24 21:00", "2026-12-31 13:00", "2027-01-01 13:00")
        val user = User("변주환")

        `when`(userRepository.findById(request.userId)).thenReturn(user)
        `when`(lectureRepository.findByIdWithLock(request.lectureId)).thenReturn(lecture)

        assertThatThrownBy {
            registerLectureService.registerLecture(request)
        }.isInstanceOf(EarlyLectureRegistrationException::class.java)
    }

    @Test
    fun `특강 신청 종료일보다 늦게 신청시 LateLectureRegistrationException 을 발생 시킨다`() {
        val userId = 1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-04-10 15:00", "2025-01-01 13:00")
        val user = User("변주환")

        `when`(userRepository.findById(request.userId)).thenReturn(user)
        `when`(lectureRepository.findByIdWithLock(request.lectureId)).thenReturn(lecture)

        assertThatThrownBy {
            registerLectureService.registerLecture(request)
        }.isInstanceOf(LateLectureRegistrationException::class.java)
    }

    @Test
    fun `특강 신청시 어떤 유저가 신청했는지 History 를 저장하는 이벤트가 발행된다`() {
        // given
        val userId = 1L
        val lectureId = 1L
        val request = LectureRequestDto(userId, lectureId)

        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 15:00", "2025-01-01 13:00")
        val user = User("변주환")

        // when
        `when`(userRepository.findById(request.userId)).thenReturn(user)
        `when`(lectureRepository.findByIdWithLock(request.lectureId)).thenReturn(lecture)

        registerLectureService.registerLecture(request)

        //then
        verify(eventPublisher, times(1)).publishEvent(any(LectureHistoryEvent::class.java))
    }
}