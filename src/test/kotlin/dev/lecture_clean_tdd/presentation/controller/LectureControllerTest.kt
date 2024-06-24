package dev.lecture_clean_tdd.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.lecture_clean_tdd.adapter.web.LectureController
import dev.lecture_clean_tdd.adapter.web.request.LectureRequestDto
import dev.lecture_clean_tdd.application.service.RegisterLectureService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(LectureController::class)
class LectureControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
){

    @MockBean
    private lateinit var registerLectureService : RegisterLectureService

    @Nested
    @DisplayName("[조회] 특강 신청 컨트롤러 테스트")
    inner class LectureControllerTests {

        @Test
        fun `특정유저가 수강신청시 성공시 200 코드와 함께 true 를 반환한다`() {
            val userId = 1L
            val lectureId = 1L
            val registrationResult = true
            val request = LectureRequestDto(userId, lectureId)

            given(registerLectureService.registerLecture(request)).willReturn(registrationResult)

            mockMvc.perform(post("/lectures/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$").value(registrationResult))
        }
    }
}