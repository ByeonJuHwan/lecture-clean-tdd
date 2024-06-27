package dev.lecture_clean_tdd.application.service

import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.domain.entity.Lecture
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GetLecturesServiceTest {

    @Mock
    private lateinit var lectureRepository: LectureRepository

    @InjectMocks
    private lateinit var getLectureService: GetLecturesService

    @Test
    fun `저장되어 있는 모든 특강의 리스트를 가져온다`() {
        // given
        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00")
        val lectures = listOf(lecture)

        // when
        `when`(lectureRepository.findAll()).thenReturn(lectures)
        val allLectures = getLectureService.getAllLectures()

        //then
        assertThat(allLectures.size).isEqualTo(1)
        assertThat(allLectures[0].title).isEqualTo("테스트")
    }
}