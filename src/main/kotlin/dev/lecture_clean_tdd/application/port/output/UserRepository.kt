package dev.lecture_clean_tdd.application.port.output

import dev.lecture_clean_tdd.domain.entity.User
import java.util.Optional


interface UserRepository {
    fun findById(userId: Long): User?
    fun save(user : User) : Long
    fun findAll():List<User>
}