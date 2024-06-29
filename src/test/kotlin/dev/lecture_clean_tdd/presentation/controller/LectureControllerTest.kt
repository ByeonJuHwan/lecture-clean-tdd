package dev.lecture_clean_tdd.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.lecture_clean_tdd.adapter.web.LectureController
import dev.lecture_clean_tdd.adapter.web.request.LectureRequest
import dev.lecture_clean_tdd.adapter.web.request.toDto
import dev.lecture_clean_tdd.adapter.web.response.LectureListResponse
import dev.lecture_clean_tdd.application.port.input.GetLecturesUseCase
import dev.lecture_clean_tdd.application.port.input.LectureStatusUseCase
import dev.lecture_clean_tdd.application.port.input.RegisterLectureUseCase
import dev.lecture_clean_tdd.application.service.dto.LectureDto
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
    private lateinit var registerLectureUseCase : RegisterLectureUseCase

    @MockBean
    private lateinit var getLecturesUseCase: GetLecturesUseCase

    @MockBean
    private lateinit var lectureStatusUseCase: LectureStatusUseCase

    @Nested 
    @DisplayName("[저장] 특강 신청 컨트롤러 테스트") 
    inner class LectureControllerTests { 
 
        @Test 
        fun `특정유저가 수강신청시 성공시 200 코드와 함께 true 를 반환한다`() { 
            val userId = 1L 
            val lectureId = 1L 
            val registrationResult = true 
            val request = LectureRequest(userId, lectureId) 
 
            given(registerLectureUseCase.registerLecture(request.toDto())).willReturn(registrationResult) 
 
            mockMvc.perform(post("/lectures/apply") 
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(request))) 
                .andExpect(status().isOk) 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(jsonPath("\$").value(registrationResult)) 
        } 
    } 

    @Nested
    @DisplayName("[조회] 특강 리스트 조회 컨트롤러 테스트")
    inner class LectureListControllerTests {

        @Test
        fun `특정유저가 특강목록 조회시 200 코드와 함께 현재 등록되어있는 특강을 반환한다`() {
            val lectures = listOf(
                LectureDto(
                    1L,
                    "테스트",
                    "2024-04-10 13:00",
                    "2024-12-31 13:00",
                    "2025-01-01 13:00",
                    30,
                    0,
                ),
                LectureDto(
                    id = 2L,
                    title = "수학",
                    registrationStartDate = "2024-08-01 00:00",
                    registrationEndDate = "2024-08-05 23:59",
                    lectureDate = "2024-08-10 14:00",
                    currentAttendees = 20,
                    maxAttendees = 30
                )
            )
            val lectureResponse = LectureListResponse(lectures)

            given(getLecturesUseCase.getAllLectures()).willReturn(lectures)

            mockMvc.perform(get("/lectures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(lectureResponse)))
        }
    }

    @Nested
    @DisplayName("[조회] 특강 신청 성공 조회 컨트롤러 테스트")
    inner class LectureStatusControllerTests {

        @Test
        fun `특정유저가 특강신청 성공시 200 코드와 함께 true 를 반환한다`() {
            val result = true
            val userId = 1L
            val lectureId = 1L

            given(lectureStatusUseCase.isLectureRegistered(userId, lectureId)).willReturn(result)

            mockMvc.perform(get("/lectures/{userId}/applications/{lectureId}", userId, lectureId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$").value(result))
        }

        @Test
        fun `특정유저가 특강신청 실패시 200 코드와 함께 false 를 반환한다`() {

            val result = false
            val userId = 1L
            val lectureId = 1L

            given(lectureStatusUseCase.isLectureRegistered(userId, lectureId)).willReturn(result)

            mockMvc.perform(get("/lectures/{userId}/applications/{lectureId}", userId, lectureId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$").value(result))
        }
    }
}
