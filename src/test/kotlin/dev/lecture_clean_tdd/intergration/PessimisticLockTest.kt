package dev.lecture_clean_tdd.intergration

import dev.lecture_clean_tdd.Exception.LectureNotFoundException
import dev.lecture_clean_tdd.adapter.persistence.jpa.JpaLectureRepository
import dev.lecture_clean_tdd.adapter.persistence.jpa.JpaUserRepository
import dev.lecture_clean_tdd.adapter.web.request.LectureRequest
import dev.lecture_clean_tdd.adapter.web.request.toDto
import dev.lecture_clean_tdd.application.port.output.LectureAttendeeRepository
import dev.lecture_clean_tdd.application.port.output.LectureHistoryRepository
import dev.lecture_clean_tdd.application.port.output.LectureRepository
import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.application.service.RegisterLectureService
import dev.lecture_clean_tdd.domain.entity.Lecture
import dev.lecture_clean_tdd.domain.entity.User
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class PessimisticLockTest {

    @Autowired
    private lateinit var jpaLectureRepository: JpaLectureRepository

    @Autowired
    private lateinit var jpaUserRepository: JpaUserRepository

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var lectureAttendeeRepository: LectureAttendeeRepository

    @Autowired
    private lateinit var lectureHistoryRepository: LectureHistoryRepository

    @Autowired
    private lateinit var registerLectureService: RegisterLectureService


    @BeforeEach
    fun setUp() {
        // 테스트 강의 생성
        val lecture = Lecture("테스트", "2024-04-10 13:00", "2024-12-31 13:00", "2025-01-01 13:00")
        lectureRepository.save(lecture)

        // 100명의 테스트 사용자 생성
        (1..100).forEach {
            userRepository.save(User(name = "Test User $it"))
        }
    }


    @Test
    fun `1개의 수강신청 요청이 들어왔을때 정상적으로 정원이 1명 증가하는지 확인`() {
        val request = LectureRequest(1L, 1L)
        val result = registerLectureService.registerLecture(request.toDto())

        assertThat(result).isTrue()

        val lecture = lectureRepository.findById(1L)
        assertThat(lecture!!.currentAttendees).isEqualTo(1)
    }


    @Test
    fun `3명이 동시에 수강신청을 했을 경우 정상적으로 현재 신청인원이 3명이 된다`() {
        // given
        val request1 = LectureRequest(1L,1L)
        val request2 = LectureRequest(2L,1L)
        val request3 = LectureRequest(3L,1L)

        // when
        CompletableFuture.allOf(
            CompletableFuture.runAsync{
                registerLectureService.registerLecture(request1.toDto())
            },
            CompletableFuture.runAsync{
                registerLectureService.registerLecture(request2.toDto())
            },
            CompletableFuture.runAsync{
                registerLectureService.registerLecture(request3.toDto())
            },
        ).join() // 제일 오래 끝나는거 끝날떄까지 기다려줌. = 내가 비동기/병렬로 실행한 함수가 전부 끝남을 보장.

        // then
        val result = jpaLectureRepository.findById(1L)
        assertThat(result.get().currentAttendees).isEqualTo(3)
    }

    @Test
    fun `100명이 동시에 신청을 했을 경우 30명만 신청이 가능하다`() {
        // given
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val lectureId = 1L
        val users = jpaUserRepository.findAll()

        // when
        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    val request = LectureRequest(users[i].id, lectureId)
                    registerLectureService.registerLecture(request.toDto())
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        // then
        val result = jpaLectureRepository.findById(1L)
        assertThat(result.get().currentAttendees).isEqualTo(30)
    }

    @Test
    fun `100명이 동시에 신청을 했을 경우 30명의 성공이력이 쌓여야한다`() {
        // given
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val lectureId = 1L
        val users = jpaUserRepository.findAll()

        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    val request = LectureRequest(users[i].id, lectureId)
                    registerLectureService.registerLecture(request.toDto())
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        // when
        val lecture = lectureRepository.findById(lectureId) ?: throw LectureNotFoundException("강의가 없습니다")
        val lectureAttendees = lectureAttendeeRepository.countByLecture(lecture)

        // then
        assertThat(lectureAttendees).isEqualTo(30)
    }

    @Test
    fun `동시에 100명이 요청했을때 100명의 history가 쌓이고 30명만 신청에 성공한다`() {
        // given
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val lectureId = 1L
        val users = jpaUserRepository.findAll()

        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    val request = LectureRequest(users[i].id, lectureId)
                    registerLectureService.registerLecture(request.toDto())
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        // when
        val lecture = lectureRepository.findById(lectureId) ?: throw LectureNotFoundException("강의가 없습니다")
        val lectureAttendees = lectureAttendeeRepository.countByLecture(lecture)
        val lectureHistories = lectureHistoryRepository.countByLecture(lecture)

        // then
        assertThat(lectureAttendees).isEqualTo(30)
        assertThat(lectureHistories).isEqualTo(100)
    }
}