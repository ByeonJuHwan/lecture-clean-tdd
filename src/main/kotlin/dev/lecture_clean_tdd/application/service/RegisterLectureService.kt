package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.Exception.DuplicateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.EarlyLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LectureNotFoundException
import dev.lecture_clean_tdd.Exception.MaxAttendeesReachedException
import dev.lecture_clean_tdd.Exception.UserNotFoundException
import dev.lecture_clean_tdd.application.listener.event.LectureHistoryEvent
import dev.lecture_clean_tdd.application.port.input.RegisterLectureUseCase
import dev.lecture_clean_tdd.application.port.output.LectureAttendeeRepository
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.application.service.dto.LectureRegistryDto
import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureAttendee
import dev.lecture_clean_tdd.domain.entity.User
import dev.lecture_clean_tdd.util.DateTimeUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service 
class RegisterLectureService( 
    private val lectureRepository: LectureRepository, 
    private val userRepository : UserRepository, 
    private val lectureAttendeeRepository: LectureAttendeeRepository, 
    private val eventPublisher: ApplicationEventPublisher, 
) : RegisterLectureUseCase{ 
 
    @Transactional 
    override fun registerLecture(request: LectureRegistryDto): Boolean { 
        val user = getUser(request.userId) 
        val lecture = getLecture(request.lectureId) 
 
        saveLectureApplicationHistory(user, lecture) 
        checkIfLectureIsFull(lecture) 
        validateLectureRegistrationDate(lecture.registrationStartDate, lecture.registrationEndDate) 
        ensureUniqueRegistration(user,lecture) 
 
        lecture.increaseCurrentAttendee() 
        lectureAttendeeRepository.save(LectureAttendee(user, lecture)) 
        return true 
    } 
 
    private fun getUser(userId: Long): User { 
        return userRepository.findById(userId) ?: throw UserNotFoundException("유저를 찾을 수 없습니다") 
    } 

    private fun getLecture(lectureId: Long): Lecture { 
        return lectureRepository.findByIdWithLock(lectureId) ?: throw LectureNotFoundException("강의를 찾을 수 없습니다") 
    } 
 
    private fun ensureUniqueRegistration(user: User, lecture: Lecture) { 
        lectureAttendeeRepository.findByUserAndLecture(user, lecture)?.let { 
            throw DuplicateLectureRegistrationException("이미 이 강의에 신청하셨습니다") 
        } 
    } 
 
    /** 
     * 비동기로 처리해서 아래 로직에서 예외가 발생해도 rollback 이 되자 않고 history 는 저장되도록 처리 
     * 비동기 같은 경우 스프링 AOP 를 사용해서 private 메소드에는 접근이 불가능하므로 접근제한자를 변경 
     */ 
    protected fun saveLectureApplicationHistory(user: User, lecture: Lecture) { 
        eventPublisher.publishEvent(LectureHistoryEvent(user, lecture)) 
    } 
 
    private fun validateLectureRegistrationDate(registrationStartDate: String, registrationEndDate: String) { 
        val currentTime = DateTimeUtil.getCurrentTimeFormatted() 
 
        if (DateTimeUtil.isBefore(currentTime, registrationStartDate)) { 
            throw EarlyLectureRegistrationException("아직 특강 신청 시작일이 아닙니다") 
        } else if (DateTimeUtil.isAfter(currentTime, registrationEndDate)) { 
            throw LateLectureRegistrationException("특강 신청 종료일이 지났습니다") 
        } 
    } 

 
    private fun checkIfLectureIsFull(lecture: Lecture) { 
        if (lecture.currentAttendees >= lecture.maxAttendees) { 
            throw MaxAttendeesReachedException("강의 정원이 초과되었습니다") 
        } 
    } 
} 
