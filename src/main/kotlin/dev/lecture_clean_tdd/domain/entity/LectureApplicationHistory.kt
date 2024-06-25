package dev.lecture_clean_tdd.domain.entity

import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class LectureApplicationHistory(
    user: User,
    lecture: Lecture,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var user: User = user

    @ManyToOne
    @JoinColumn(name = "lecture_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var lecture: Lecture = lecture

    val createdAt: LocalDateTime = LocalDateTime.now()
}