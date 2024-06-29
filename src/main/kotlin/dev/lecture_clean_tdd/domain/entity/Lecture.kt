package dev.lecture_clean_tdd.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Lecture(
    title: String,
    registrationStartDate: String,
    registrationEndDate: String,
    lectureDate: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var title: String = title
        protected set

    var registrationStartDate: String = registrationStartDate
        protected set

    var registrationEndDate: String = registrationEndDate
        protected set

    var lectureDate: String = lectureDate
        protected set

    val maxAttendees: Int = 30

    var currentAttendees: Int = 0
 
    fun increaseCurrentAttendee() { 
        this.currentAttendees++ 
    } 
}
