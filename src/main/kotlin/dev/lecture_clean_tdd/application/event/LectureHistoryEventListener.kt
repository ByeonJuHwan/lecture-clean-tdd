package dev.lecture_clean_tdd.application.event

import dev.lecture_clean_tdd.application.event.dto.LectureHistoryEvent
import dev.lecture_clean_tdd.application.port.output.LectureHistoryRepository
import dev.lecture_clean_tdd.domain.entity.LectureApplicationHistory
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class LectureHistoryEventListener (
    private val lectureHistoryRepository: LectureHistoryRepository
){
    private val logger = LoggerFactory.getLogger(LectureHistoryEventListener::class.java)

    @Async
    @EventListener
    fun handleLectureHistoryEvent(event: LectureHistoryEvent) {
        val threadName = Thread.currentThread().name
        logger.info("LectureApplicationEvent thread: $threadName")

        val history = LectureApplicationHistory(
            user = event.user,
            lecture = event.lecture,
        )
        lectureHistoryRepository.save(history)
    }
}