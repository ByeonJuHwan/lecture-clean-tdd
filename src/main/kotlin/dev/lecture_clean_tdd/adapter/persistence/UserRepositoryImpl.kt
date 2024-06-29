package dev.lecture_clean_tdd.adapter.persistence

import dev.lecture_clean_tdd.Exception.UserNotFoundException
import dev.lecture_clean_tdd.adapter.persistence.jpa.JpaUserRepository
import dev.lecture_clean_tdd.application.port.output.UserRepository
import dev.lecture_clean_tdd.domain.entity.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaUserRepository: JpaUserRepository,
) : UserRepository {
    override fun findById(userId: Long): User? {
        return jpaUserRepository.findByIdOrNull(userId)
    }

    override fun save(user: User): Long {
        return jpaUserRepository.save(user).id
    }

    override fun findAll(): List<User> {
        return jpaUserRepository.findAll()
    }
}