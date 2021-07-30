package com.zest.toyproject.repositories

import com.zest.toyproject.models.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findOneByUsername(username: String?): Optional<Member>
    fun existsByUsername(name: String?): Boolean
}
