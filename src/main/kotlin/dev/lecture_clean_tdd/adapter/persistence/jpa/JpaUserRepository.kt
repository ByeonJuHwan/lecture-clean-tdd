package dev.lecture_clean_tdd.adapter.persistence.jpa

import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<User, Long> {
}