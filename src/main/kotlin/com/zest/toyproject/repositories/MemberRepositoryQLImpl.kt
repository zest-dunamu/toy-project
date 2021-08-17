package com.zest.toyproject.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.QMember
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class MemberRepositoryQLImpl(jpaQueryFactory: JPAQueryFactory) : QuerydslRepositorySupport(Member::class.java),
    MemberRepositoryQL {
    override fun findByNickname(nickname: String): Member? {
        val member = QMember.member
        return from(member).where(member.nickname.eq(nickname)).fetchOne()
    }
}