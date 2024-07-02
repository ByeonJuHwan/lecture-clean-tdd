package dev.lecture_clean_tdd.adapter.web

import dev.lecture_clean_tdd.adapter.web.request.LectureRequest
import dev.lecture_clean_tdd.adapter.web.request.toDto
import dev.lecture_clean_tdd.adapter.web.response.LectureListResponse
import dev.lecture_clean_tdd.application.port.input.GetLecturesUseCase
import dev.lecture_clean_tdd.application.port.input.LectureStatusUseCase
import dev.lecture_clean_tdd.application.port.input.RegisterLectureUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LectureController(
    private val registerLectureUseCase: RegisterLectureUseCase,
    private val getLecturesUseCase: GetLecturesUseCase,
    private val lectureStatusUseCase: LectureStatusUseCase
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/lectures/apply")
    fun registerLecture(
        @RequestBody request: LectureRequest,
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(registerLectureUseCase.registerLecture(request.toDto()))
    }

    @GetMapping("/lectures")
    fun getLectures(): ResponseEntity<LectureListResponse> {
        return ResponseEntity.ok(LectureListResponse(getLecturesUseCase.getAllLectures()))
    }
    @GetMapping("/lectures/{lectureId}/applications/{userId}")
    fun checkLectureApplicationStatus(
        @PathVariable lectureId: Long,
        @PathVariable userId: Long
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(lectureStatusUseCase.isLectureRegistered(userId, lectureId))
    }
}