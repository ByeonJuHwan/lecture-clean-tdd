package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.Exception.DuplicateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.EarlyLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LectureNotFoundException
import dev.lecture_clean_tdd.Exception.MaxAttendeesReachedException
import dev.lecture_clean_tdd.Exception.UserNotFoundException
import dev.lecture_clean_tdd.adapter.web.request.LectureRequestDto
import dev.lecture_clean_tdd.application.port.input.RegisterLectureUseCase
import dev.lecture_clean_tdd.application.port.output.LectureAttendeeRepository
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.LectureAttendee
import dev.lecture_clean_tdd.util.DateTimeUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterLectureService(
    private val lectureRepository: LectureRepository,
    private val userRepository : UserRepository,
    private val lectureAttendeeRepository: LectureAttendeeRepository,
) : RegisterLectureUseCase{

    @Transactional
    override fun registerLecture(request: LectureRequestDto): Boolean {
        // 예외처리
        val user = userRepository.findById(request.userId) ?: throw UserNotFoundException("유저를 찾을 수 없습니다")
        val lecture = lectureRepository.findByIdWithLock(request.lectureId) ?: throw LectureNotFoundException("강의를 찾을 수 없습니다")

        // 30명 정원이 초과했는지 확인
        checkIfLectureIsFull(lecture)

        // 강의 신청 시작일, 종료일 에 맞게 신청을 하는지 체크
        validateLectureRegistrationDate(lecture.registrationStartDate, lecture.registrationEndDate)


        // 이미 신청한 전적이 있는지 확인
        lectureAttendeeRepository.findByUserAndLecture(user, lecture)?.let {
            throw DuplicateLectureRegistrationException("이미 이 강의에 신청하셨습니다")
        }

        // 정원 초과도 안되고 중복신청도 안했다면 현재 신청인원을 올린다.
        lecture.increaseCurrentAttendee()

        // 어떤 사람이 신청했는지 알 수 있게 이력 관리
        lectureAttendeeRepository.save(LectureAttendee(user, lecture))
        return true
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