package dev.lecture_clean_tdd.domain.entity

import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class LectureAttendee (
    user :User,
    lecture: Lecture,
){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var user: User = user
        protected set

    @ManyToOne
    @JoinColumn(name = "lecture_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var lecture: Lecture = lecture
        protected set

}