package dev.lecture_clean_tdd

import dev.lecture_clean_tdd.Exception.DuplicateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.EarlyLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LateLectureRegistrationException
import dev.lecture_clean_tdd.Exception.LectureNotFoundException
import dev.lecture_clean_tdd.Exception.MaxAttendeesReachedException
import dev.lecture_clean_tdd.Exception.UserNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ErrorResponse(val code: String, val message: String)

@RestControllerAdvice
class ApiControllerAdvice : ResponseEntityExceptionHandler() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(LectureNotFoundException::class)
    fun handleLectureNotFoundException(e: LectureNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("404", e.message ?: "강의를 찾을 수 없습니다"),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUseNotFoundException(e: UserNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("404", e.message ?: "유저를 찾을 수 없습니다"),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(MaxAttendeesReachedException::class)
    fun handleMaxAttendeesReachedException(e: MaxAttendeesReachedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("409", e.message ?: "강의 정원이 초과되었습니다"),
            HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(DuplicateLectureRegistrationException::class)
    fun handleDuplicateLectureRegistrationException(e: DuplicateLectureRegistrationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("409", e.message ?: "이미 이 강의에 신청하셨습니다"),
            HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(EarlyLectureRegistrationException::class)
    fun handleEarlyLectureRegistrationException(e: EarlyLectureRegistrationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("409", e.message ?: "아직 특강 신청 시작일이 아닙니다"),
            HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(LateLectureRegistrationException::class)
    fun handleLateLectureRegistrationException(e: LateLectureRegistrationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("409", e.message ?: "특강 신청 종료일이 지났습니다"),
            HttpStatus.CONFLICT
        )
    }


    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("500", "에러가 발생했습니다."),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
