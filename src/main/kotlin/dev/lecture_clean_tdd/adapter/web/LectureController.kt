package dev.lecture_clean_tdd.adapter.web

import dev.lecture_clean_tdd.application.service.RegisterLectureService
import dev.lecture_clean_tdd.adapter.web.request.LectureRequestDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LectureController (
    private val registerLectureService: RegisterLectureService,
){

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/lectures/apply")
    fun registerLecture(
        @RequestBody request: LectureRequestDto,
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(registerLectureService.registerLecture(request))
    }
}